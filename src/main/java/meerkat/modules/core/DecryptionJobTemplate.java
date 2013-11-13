package meerkat.modules.core;

import meerkat.modules.PluginNotFoundException;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.import_export.IImportImplementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;

/**
 * Ta klasa obsługuje zadanie deszyfrowania.
 * <p/>
 * To nie jest część API. W żadnym wypadku nie wolno zakładać że dysponujesz obiektem tej klasy. To jest detal
 * implementacyjny.
 *
 * @param <T> typ ImplementationPack
 * @param <U> typ rezultatu (dla handlera)
 * @author Maciej Poleski
 */
class DecryptionJobTemplate<T, U> extends JobWithStates<U> {

    private final IDialogBuilderFactory dialogBuilderFactory;
    private final IPluginManager pluginManager;
    private final IDecryptionImplementationProvider<T, IState, U> decryptionImplementationProvider;
    private final IAbortedStateFactory abortedStateFactory = new IAbortedStateFactory() {
        @Override
        public IState newAbortedState() {
            return new AbortedState();
        }
    };

    /**
     * Tworzy nowe zadanie deszyfrowania. Po stworzeniu znajduje się ono w stanie State.READY
     *
     * @param importPlugin         Plugin importu wybrany przez użytkownika.
     * @param handler              Handler na który będzie zgłaszana zmiana stanu zadania
     * @param dialogBuilderFactory Fabryka budowniczych okien dialogowych na potrzeby pluginów.
     */
    public DecryptionJobTemplate(IImportExportPlugin importPlugin, IJobObserver handler, IDialogBuilderFactory dialogBuilderFactory, IPluginManager pluginManager, IDecryptionImplementationProvider<T, IState, U> decryptionImplementationProvider, IResultHandler<U> resultHandler) {
        super(handler, resultHandler);
        this.dialogBuilderFactory = dialogBuilderFactory;
        this.pluginManager = pluginManager;
        this.decryptionImplementationProvider = decryptionImplementationProvider;
        this.currentState = new ReadyState(importPlugin);
    }

    IDialogBuilderFactory getDialogBuilderFactory() {
        return dialogBuilderFactory;
    }

    @Override
    public void abort() {
        abort(abortedStateFactory);
    }

    private class ReadyState implements IState {
        private final IImportExportPlugin importExportPlugin;

        private ReadyState(IImportExportPlugin importExportPlugin) {
            this.importExportPlugin = importExportPlugin;
        }

        @Override
        public IState start() {
            return new PreparingImportState(importExportPlugin);
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia
        }

        @Override
        public State getState() {
            return State.READY;
        }
    }

    private class PreparingImportState extends BranchingState {
        private final IImportExportPlugin importExportPlugin;

        public PreparingImportState(IImportExportPlugin importExportPlugin) {
            this.importExportPlugin = importExportPlugin;
        }

        @Override
        public IState start() {
            IImportImplementation importImplementation = importExportPlugin.getImportImplementation();
            if (!importImplementation.prepare(dialogBuilderFactory) || isAborted()) {
                initializeNextState(new AbortedState());
                return nextState;
            }
            initializeNextState(new ExtractingPipelineState(importExportPlugin, importImplementation));
            return getCurrentState();
        }

        @Override
        public void abort() {
            initializeNextState(new AbortedState());
        }

        @Override
        public State getState() {
            return State.PREPARING;
        }
    }

    private class ExtractingPipelineState extends BranchingState {
        private final IImportExportPlugin importExportPlugin;
        private final IImportImplementation importImplementation;

        public ExtractingPipelineState(IImportExportPlugin importExportPlugin, IImportImplementation importImplementation) {
            this.importExportPlugin = importExportPlugin;
            this.importImplementation = importImplementation;
        }

        @Override
        public IState start() {
            try {
                Thread importThread = new Thread(wrapRunnable(importImplementation));
                Pipe importDecryptPipe = SelectorProvider.provider().openPipe();
                importImplementation.setOutputChannel(importDecryptPipe.sink());
                importThread.start();

                ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
                importDecryptPipe.source().read(sizeBuffer);
                sizeBuffer.flip();
                int size = sizeBuffer.getInt();

                ByteBuffer mementoBytes = ByteBuffer.allocate(size);
                importDecryptPipe.source().read(mementoBytes);
                mementoBytes.flip();
                Memento memento = Memento.byteBufferToMemento(mementoBytes, size);

                DecryptionPipeline decryptionPipeline = memento.getDecryptionPipeline(importExportPlugin, pluginManager);

                T implementationPack = decryptionImplementationProvider.getImplementationPackFromDecryptionPipeline(decryptionPipeline, importImplementation);

                initializeNextState(decryptionImplementationProvider.getPrepareImplementationState(DecryptionJobTemplate.this, implementationPack, importThread, importDecryptPipe));

            } catch (IOException | PluginNotFoundException e) {
                abortBecauseOfFailure(e);
            }
            return getCurrentState();
        }

        @Override
        public void abort() {
            initializeNextState(new AbortedState());
        }

        @Override
        public State getState() {
            return State.PREPARING;
        }
    }
}
