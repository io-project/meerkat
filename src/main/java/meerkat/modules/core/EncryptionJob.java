package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.plausible_deniability.IOverrideImplementation;
import meerkat.modules.serialization.ISerializationImplementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;

/**
 * Ta klasa obsługuje zadanie szyfrowania.
 * <p/>
 * To nie jest część API
 *
 * @author Maciej Poleski
 */
class EncryptionJob extends JobWithStates {

    private final EncryptionPipeline pipeline;
    private final ImplementationPack implementationPack;
    private final IDialogBuilderFactory dialogBuilderFactory;
    private final IAbortedStateFactory abortedStateFactory = new IAbortedStateFactory() {
        @Override
        public IState newAbortedState() {
            return new AbortedState();
        }
    };

    /**
     * Tworzy nowe zadanie szyfrowania. Po stworzeniu znajduje się ono w stanie State.READY
     *
     * @param pipeline             Gotowy pipeline ustalony przez użytkownika
     * @param handler              Handler na który będzie zgłaszana zmiana stanu zadania
     * @param dialogBuilderFactory Fabryka budowniczych okien dialogowych na potrzeby pluginów.
     */
    public EncryptionJob(EncryptionPipeline pipeline, IJobObserver handler, IDialogBuilderFactory dialogBuilderFactory) {
        super(handler);
        this.pipeline = pipeline;
        this.implementationPack = new ImplementationPack(pipeline);
        this.dialogBuilderFactory = dialogBuilderFactory;
        currentState = new ReadyState();
    }

    /**
     * Czy to zadanie zostało anulowane.
     * <p/>
     * Lekka - ten wątek
     *
     * @return true - wtedy i tylko wtedy gdy to zadanie zostało anulowane
     */
    private boolean isAborted() {
        return getState() == State.ABORTED;
    }

    @Override
    public void abort() {
        abort(abortedStateFactory);
    }

    private static class ImplementationPack {
        private ImplementationPack(EncryptionPipeline pipeline) {
            this.serialization = pipeline.getSerializationPlugin().getSerializationImplementation();
            this.encryption = pipeline.getEncryptionPlugin().getEncryptionImplementation();
            this.export = pipeline.getImportExportPlugin().getExportImplementation();
            this.override = pipeline.getOverridePlugin().getOverrideImplementation();
        }

        final ISerializationImplementation serialization;
        final IEncryptionImplementation encryption;
        final IExportImplementation export;
        final IOverrideImplementation override;
    }

    private class ReadyState implements IState {
        @Override
        public IState start() {
            return new PreparingState();
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

    private class PreparingState implements IState, IAbortableState {
        @Override
        public IState start() {
            if (!implementationPack.serialization.prepare(dialogBuilderFactory) || isAborted()) {
                return new AbortedState(); // FIXME: Jeżeli stan jest już anulowany - w ten sposób zachodzi zmiana stanu
            }
            if (!implementationPack.encryption.prepare(dialogBuilderFactory) || isAborted()) {
                return new AbortedState();
            }
            if (!implementationPack.export.prepare(dialogBuilderFactory) || isAborted()) {
                return new AbortedState();
            }
            if (!implementationPack.override.prepare(dialogBuilderFactory) || isAborted()) {
                return new AbortedState();
            }
            return new WorkingState();
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia - metoda start sprawdza w kontekście czy anulowano zadanie
        }

        @Override
        public State getState() {
            return State.PREPARING;
        }
    }

    private class WorkingState extends BranchingState {

        Thread serializationThread;
        Pipe serializationEncryptionPipe;
        Thread encryptionThread;
        Pipe encryptionExportPipe;
        Thread exportThread;
        Thread overrideThread;

        @Override
        public IState start() {
            try {
                synchronized (this) {
                    // Pipe
                    serializationEncryptionPipe = SelectorProvider.provider().openPipe();
                    encryptionExportPipe = SelectorProvider.provider().openPipe();
                    // Channel
                    implementationPack.serialization.setOutputChannel(serializationEncryptionPipe.sink());
                    implementationPack.encryption.setInputChannel(serializationEncryptionPipe.source());
                    implementationPack.encryption.setOutputChannel(encryptionExportPipe.sink());
                    implementationPack.export.setInputChannel(encryptionExportPipe.source());
                    // Thread
                    serializationThread = new Thread(wrapRunnable(implementationPack.serialization));
                    encryptionThread = new Thread(wrapRunnable(implementationPack.encryption));
                    exportThread = new Thread(wrapRunnable(implementationPack.export));
                    overrideThread = new Thread(wrapRunnable(implementationPack.override));

                    // Memento
                    Memento memento = new Memento(pipeline);
                    ByteBuffer pluginSet = memento.mementoToByteBuffer();
                    ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
                    lengthBuffer.putInt(pluginSet.position());
                    pluginSet.flip();
                    lengthBuffer.flip();
                    exportThread.start();
                    encryptionExportPipe.sink().write(lengthBuffer);
                    encryptionExportPipe.sink().write(pluginSet); // Teraz jest pewność, że te dane trafią do eksportu jako pierwsze

                    // Start
                    serializationThread.start();
                    encryptionThread.start();
                }

                // End
                serializationThread.join();
                encryptionThread.join();
                exportThread.join();

                synchronized (this) {
                    if (nextState == null) {
                        overrideThread.start();
                        overrideThread.join();
                    }
                }

                initializeNextState(new FinishedState());

            } catch (IOException | InterruptedException e) {
                abortBecauseOfFailure(e); // TODO: Może jakiś opis?
            }
            synchronized (this) {
                return nextState;
            }
        }

        @Override
        public void abort() {
            synchronized (this) {
                initializeNextState(new AbortedState());
                serializationThread.interrupt();
                encryptionThread.interrupt();
                exportThread.interrupt();
                overrideThread.interrupt();
            }
        }

        @Override
        public State getState() {
            return State.WORKING;
        }
    }

}
