package meerkat.modules.core;

/**
 * Wyabstrahowana logika zarządzania stanami wewnętrznymi "zadań".
 * <p/>
 * To nie jest część API. W żadnym wypadku nie wolno zakładać że dysponujesz obiektem tej klasy. To jest nieistotny
 * detal implementacyjny.
 *
 * @param <T> typ rezultatu
 * @author Maciej Poleski
 */
abstract class JobWithStates<T> implements IJob {
    private final IJobObserver handler;
    private final IResultHandler<T> resultHandler;
    IState currentState;
    T result;
    Throwable resultThrowable;

    JobWithStates(IJobObserver handler, IResultHandler<T> resultHandler) {
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
                if (resultThrowable == null) {
                    if (!isAborted()) {
                        resultHandler.handleResult(result);
                    }
                } else {
                    resultHandler.handleException(resultThrowable);
                }
            }
        }, "Job Controller Thread").start();
    }

    void abort(IAbortedStateFactory abortedStateFactory) {
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
    boolean isAborted() {
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
            }, "Job Observer Handler Thread").start();
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

        private final Throwable throwable;

        public FailedState(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public IState start() {
            resultThrowable = throwable;
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
    abstract class BranchingState implements IState, IAbortableState {
        IState nextState = null;

        void abortBecauseOfFailure(Throwable t) {
            initializeNextState(new FailedState(t));
            currentState.abort();
        }

        void initializeNextState(IState nextState) {
            synchronized (this) {
                if (this.nextState == null) {  // Być może już ustalono
                    this.nextState = nextState;
                }
            }
        }

        synchronized IState getCurrentState() {
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
                    } catch (Exception e) {      // Takie rzeczy się zdarzają - obsługujemy
                        abortBecauseOfFailure(e);
                    } catch (Throwable t) {      // Poważny błąd - raportujemy, ale pozwalamy zniszczyć wątek
                        abortBecauseOfFailure(t);
                        // To jest najprawdopodobniej błąd
                        throw t;
                    }
                }
            };
        }
    }
}
