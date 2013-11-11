package meerkat.modules.core;

/**
 * Wyabstrahowana logika zarządzania stanami wewnętrznymi "zadań".
 *
 * @param <T> typ rezultatu
 * @author Maciej Poleski
 */
abstract class JobWithStates<T> implements IJob {
    private final IJobObserver handler;
    private final IResultHandler<T> resultHandler;
    protected IState currentState;
    protected T result;
    protected Throwable resultException;

    public JobWithStates(IJobObserver handler, IResultHandler<T> resultHandler) {
        this.handler = handler;
        this.resultHandler = resultHandler;
    }

    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    IState frozenState;
                    synchronized (JobWithStates.this) {
                        frozenState = currentState;  // W tej iteracji zajmuję się stanem z tej chwili
                    }
                    IState newState = frozenState.start();
                    if (newState == frozenState) {
                        break; // To był ostatni krok - koniec
                    }
                    synchronized (JobWithStates.this) {
                        if (currentState == frozenState) {
                            currentState = newState;
                            runHandler(currentState.getState());
                        }
                        // Jeżeli się udało - ustalam kolejny krok zgodnie z ustaleniem implementacji
                        // Jeżeli się nie udało - ktoś z zewnątrz zadecydował - wykonam jego decyzję (wykorzystane w abort)
                    }
                }
                if (resultException == null) {
                    if (!isAborted()) {
                        resultHandler.handleResult(result);
                    }
                } else {
                    resultHandler.handleException(resultException);
                }
            }
        }).start();
    }

    protected void abort(IAbortedStateFactory abortedStateFactory) {
        synchronized (this) {
            if (currentState instanceof IAbortableState) {
                currentState.abort(); // Być może poprzedni stan jeszcze nie zakończył wykonywać swojego zadania - przerwij je.
                currentState = abortedStateFactory.newAbortedState();
                runHandler(currentState.getState()); // Poprzedni stan nie był anulowany - zachodzi zmiana - handler
            }
        }
    }

    /**
     * Czy to zadanie zostało anulowane.
     * <p/>
     * Lekka - ten wątek
     *
     * @return true - wtedy i tylko wtedy gdy to zadanie zostało anulowane
     */
    protected boolean isAborted() {
        return getState() == State.ABORTED;
    }

    @Override
    public State getState() {
        synchronized (this) {
            return currentState.getState();
        }
    }

    /**
     * Ciężka - osobny wątek
     *
     * @param newState Nowy stan obecnego obiektu.
     */
    private void runHandler(final State newState) {
        if (handler != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.update(JobWithStates.this, newState);
                }
            }).start();
        }
    }

    /**
     * Klasa bazowa stanów które już nie ulegną zmianie.
     */
    private abstract class DeadEndState implements IState {
        @Override
        public IState start() {
            return this; // Pozostajemy w tym stanie do końca - kontekst wykryje to jako sygnał do zakończenia
        }

        @Override
        public void abort() {
            // Nie ma nic do zrobienia - już zakończono
        }
    }

    protected class AbortedState extends DeadEndState {

        @Override
        public State getState() {
            return State.ABORTED;
        }
    }

    protected class FinishedState extends DeadEndState {

        @Override
        public State getState() {
            return State.FINISHED;
        }
    }

    protected class FailedState extends DeadEndState {

        private final Exception exception;

        public FailedState(Exception e) {
            exception = e;
        }

        @Override
        public IState start() {
            resultException = exception;
            return super.start();
        }

        @Override
        public State getState() {
            return State.FAILED;
        }
    }

    /**
     * Klasa bazowa stanów o nie określonej z góry przyszłości (rozgałęziających się). Dostarcza przydatne metody
     * do obsługi takiego stanu.
     *
     * @author Maciej Poleski
     */
    protected abstract class BranchingState implements IState, IAbortableState {
        protected IState nextState = null;

        void abortBecauseOfFailure(Exception e) {
            initializeNextState(new FailedState(e));
        }

        protected void initializeNextState(IState nextState) {
            synchronized (this) {
                if (this.nextState == null) {  // Być może już ustalono
                    this.nextState = nextState;
                }
            }
        }

        protected synchronized IState getCurrentState() {
            return nextState;
        }

        /**
         * Tworzy Runnable na bazie Runnable rzucającego wyjątki tak aby wyjątek powodował anulowanie zadania.
         *
         * @param runnable Runnable rzucające wyjątki.
         * @return Runnable opakowane - bezpieczne.
         */
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
    }
}
