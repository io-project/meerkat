package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.plausible_deniability.IOverrideImplementation;
import meerkat.modules.serialization.ISerializationImplementation;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Ta klasa obsługuje zadanie szyfrowania.
 *
 * @author Maciej Poleski
 */
class EncryptionJob implements IJob {
    private interface IState {
        /**
         * @return Stan do którego należy przejść (this - koniec)
         */
        IState start();

        State getState();
    }

    /**
     * Zadanie znajdujące się w tym stanie można anulować.
     */
    private interface IAbortableState {

        void abort();
    }

    private final EncryptionPipeline pipeline;
    private final ImplementationPack implementationPack;
    private final Runnable handler;
    private final IDialogBuilderFactory dialogBuilderFactory;
    private AtomicReference<IState> currentState;

    /**
     * Tworzy nowe zadanie szyfrowania. Po stworzeniu znajduje się ono w stanie State.READY
     *
     * @param pipeline
     * @param handler
     * @param dialogBuilderFactory
     */
    public EncryptionJob(EncryptionPipeline pipeline, Runnable handler, IDialogBuilderFactory dialogBuilderFactory) {
        this.pipeline = pipeline;
        this.implementationPack = new ImplementationPack(pipeline);
        this.handler = handler;
        this.dialogBuilderFactory = dialogBuilderFactory;
        currentState.set(new ReadyState());
    }

    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    IState frozenState = currentState.get();  // W tej iteracji zajmuję się stanem z tej chwili
                    IState newState = frozenState.start();
                    if (newState == frozenState) {
                        break; // To był ostatni krok - koniec
                    }
                    currentState.compareAndSet(frozenState, newState);
                    // Jeżeli się udało - ustalam kolejny krok zgodnie z ustaleniem implementacji
                    // Jeżeli się nie udało - ktoś z zewnątrz zadecydował - wykonam jego decyzję (wykorzystane w abort)
                    runHandler();
                }
            }
        }).start();
    }

    @Override
    public void abort() {
        IState c = currentState.getAndSet(new AbortedState()); // Od teraz kolejne zadania nie będą uruchamiane
        if (c instanceof IAbortableState) {
            ((IAbortableState) c).abort(); // Być może poprzedni stan jeszcze nie zakończył wykonywać swojego zadania - przerwij je.
            runHandler(); // Poprzedni stan nie być anulowany - zachodzi zmiana - handler
        }
    }

    @Override
    public State getState() {
        return currentState.get().getState();
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
     */
    private synchronized void setState(IState newState) {
        IState c = currentState.getAndSet(newState);
        if (c != newState) {
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

        ISerializationImplementation serialization;
        IEncryptionImplementation encryption;
        IExportImplementation export;
        IOverrideImplementation override;
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
                return new AbortedState();
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
        @Override
        public void start() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //FIXME: implementation
                }
            }).start();
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
}
