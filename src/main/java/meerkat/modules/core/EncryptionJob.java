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
class EncryptionJob implements IJob {
    private interface IState {
        /**
         * @return Stan do którego należy przejść (this - koniec)
         */
        IState start();

        void abort();

        State getState();
    }

    /**
     * Interfejs znacznikowy. Zadanie znajdujące się w tym stanie można anulować.
     */
    private interface IAbortableState {
    }

    private final EncryptionPipeline pipeline;
    private final ImplementationPack implementationPack;
    private final Runnable handler;
    private final IDialogBuilderFactory dialogBuilderFactory;
    private IState currentState;

    /**
     * Tworzy nowe zadanie szyfrowania. Po stworzeniu znajduje się ono w stanie State.READY
     *
     * @param pipeline             Gotowy pipeline ustalony przez użytkownika
     * @param handler              Handler na który będzie zgłaszana zmiana stanu zadania
     * @param dialogBuilderFactory Fabryka budowniczych okien dialogowych na potrzeby pluginów.
     */
    public EncryptionJob(EncryptionPipeline pipeline, Runnable handler, IDialogBuilderFactory dialogBuilderFactory) {
        this.pipeline = pipeline;
        this.implementationPack = new ImplementationPack(pipeline);
        this.handler = handler;
        this.dialogBuilderFactory = dialogBuilderFactory;
        currentState = new ReadyState();
    }

    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    IState frozenState;
                    synchronized (EncryptionJob.this) {
                        frozenState = currentState;  // W tej iteracji zajmuję się stanem z tej chwili
                    }
                    IState newState = frozenState.start();
                    if (newState == frozenState) {
                        break; // To był ostatni krok - koniec
                    }
                    synchronized (EncryptionJob.this) {
                        if (currentState == frozenState) {
                            currentState = newState;
                            runHandler();
                        }
                        // Jeżeli się udało - ustalam kolejny krok zgodnie z ustaleniem implementacji
                        // Jeżeli się nie udało - ktoś z zewnątrz zadecydował - wykonam jego decyzję (wykorzystane w abort)
                    }
                }
            }
        }).start();
    }

    @Override
    public void abort() {
        synchronized (this) {
            if (currentState instanceof IAbortableState) {
                currentState.abort(); // Być może poprzedni stan jeszcze nie zakończył wykonywać swojego zadania - przerwij je.
                currentState = new AbortedState();
                runHandler(); // Poprzedni stan nie był anulowany - zachodzi zmiana - handler
            }
        }
    }

    @Override
    public State getState() {
        synchronized (this) {
            return currentState.getState();
        }
    }

    /**
     * Ciężka - osobny wątek
     */
    private void runHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.run();
            }
        }).start();
    }

    /**
     * Lekka - ten wątek (handler asynchronicznie)
     *
     * @param newState Po zakończeniu tej funkcji stan obiektu będzie identyczne z newState
     * @deprecated Wygląda na to, że jest niepotrzebna
     */
    private synchronized void setState(IState newState) {
        if (currentState != newState) {
            currentState = newState;
            runHandler();
        }
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

    private class ReadyState implements IState, IAbortableState {
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

    private class WorkingState implements IState, IAbortableState {

        Thread serializationThread;
        Pipe serializationEncryptionPipe;
        Thread encryptionThread;
        Pipe encryptionExportPipe;
        Thread exportThread;
        Thread overrideThread;

        IState nextState = null;

        void abortBecauseOfFailure(Exception e) {
            initializeNextState(new FailedState(e));
        }

        private void initializeNextState(IState nextState) {
            synchronized (this) {
                if (this.nextState == null) {  // Być może już ustalono
                    this.nextState = nextState;
                }
            }
        }

        Runnable wrapRunnable(final meerkat.modules.Runnable runnable) {
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        abortBecauseOfFailure(e);
                    }
                }
            };
        }

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
                    ByteBuffer pluginSet = Memento.getMementoByteBuffer(memento);
                    exportThread.start();
                    encryptionExportPipe.sink().write(pluginSet); // Teraz jest pewność, że te dane trafią do eksportu jako pierwsze

                    // Start
                    serializationThread.start();
                    encryptionThread.start();
                    overrideThread.start();
                }

                // End
                serializationThread.join();
                encryptionThread.join();
                exportThread.join();
                overrideThread.join();

                initializeNextState(new FinishedState());

            } catch (IOException e) {
                abortBecauseOfFailure(e); // TODO: Może jakiś opis?
            } catch (InterruptedException e) {
                abortBecauseOfFailure(e);
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

    private class AbortedState implements IState {
        @Override
        public IState start() {
            return this; // Pozostajemy w tym stanie do końca - kontekst wykryje to jako sygnał do zakończenia
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia - już anulowano
        }

        @Override
        public State getState() {
            return State.ABORTED;
        }
    }

    private class FinishedState implements IState {
        @Override
        public IState start() {
            return this; // Pozostajemy w tym stanie do końca - kontekst wykryje to jako sygnał do zakończenia
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia - zadanie już zakończono.
        }

        @Override
        public State getState() {
            return State.FINISHED;
        }
    }

    private class FailedState implements IState {
        public FailedState() {
        }

        public FailedState(Exception e) {
            //TODO: Wykorzystać wyjątek aby dostarczyć informacje
        }

        @Override
        public IState start() {
            return this; // Pozostajemy w tym stanie do końca - kontekst wykryje to jako sygnał do zakończenia
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia - zadanie już zakończono.
        }

        @Override
        public State getState() {
            return State.FAILED;
        }
    }
}
