package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.plausible_deniability.IOverrideImplementation;
import meerkat.modules.serialization.ISerializationImplementation;

/**
 * Ta klasa obsługuje zadanie szyfrowania.
 *
 * @author Maciej Poleski
 */
class EncryptionJob implements IJob {
    private interface IState {
        void start();

        State getState();
    }

    private final EncryptionPipeline pipeline;
    private final ImplementationPack implementationPack;
    private final Runnable handler;
    private final IDialogBuilderFactory dialogBuilderFactory;
    private IState currentState;
    private final IState readyState = new ReadyState();
    private final IState preparingState = new PreparingState();
    private final IState workingState = new WorkingState();
    private final IState abortedState;
    private final IState successedState;
    private final IState failedState;


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
        currentState = readyState;
    }

    @Override
    public synchronized void start() {
        currentState.start();
    }

    @Override
    public synchronized void abort() {
        setState(abortedState);
    }

    @Override
    public synchronized State getState() {
        return currentState.getState();
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

        ISerializationImplementation serialization;
        IEncryptionImplementation encryption;
        IExportImplementation export;
        IOverrideImplementation override;
    }

    private class ReadyState implements IState {
        @Override
        public void start() {
            setState(preparingState);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EncryptionJob.this.start();
                }
            }).start();
        }

        @Override
        public State getState() {
            return State.READY;
        }
    }

    private class PreparingState implements IState {
        @Override
        public void start() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!implementationPack.serialization.prepare(dialogBuilderFactory) || isAborted()) {
                        return;
                    }
                    if (!implementationPack.encryption.prepare(dialogBuilderFactory) || isAborted()) {
                        return;
                    }
                    if (!implementationPack.export.prepare(dialogBuilderFactory) || isAborted()) {
                        return;
                    }
                    if (!implementationPack.override.prepare(dialogBuilderFactory) || isAborted()) {
                        return;
                    }
                    setState(workingState);
                    EncryptionJob.this.start();
                }
            }).start();
        }

        @Override
        public State getState() {
            return State.PREPARING;
        }
    }

    private class WorkingState implements IState {
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
}
