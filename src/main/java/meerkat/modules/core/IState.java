package meerkat.modules.core;

/**
 * Interfejs stanu wewnętrznego zadań.
 * <p/>
 * Zdecydowanie nie API
 *
 * @author Maciej Poleski
 */
interface IState {
    /**
     * @return Stan do którego należy przejść (this - koniec)
     */
    IState start();

    void abort();

    IJob.State getState();
}

/**
 * Interfejs znacznikowy. Zadanie znajdujące się w tym stanie można anulować.
 */
interface IAbortableState {
}