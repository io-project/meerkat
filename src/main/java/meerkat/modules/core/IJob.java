package meerkat.modules.core;

/**
 * Interfejs pozwalający na komunikacje z zadaniem.
 * <p/>
 * Implementacje wszystkich metod tego interfejsu powinny być thread-safe.
 *
 * @author Maciej Poleski
 */
public interface IJob {

    public enum State {
        READY,
        PREPARING,
        WORKING,
        FINISHED,
        FAILED,
        ABORTED,
    }

    /**
     * Uruchamia zadanie.
     */
    void start();

    /**
     * Anuluje zadanie.
     */
    void abort();

    /**
     * Zwraca obecny stan zadania
     *
     * @return Obecny stan zadania
     */
    State getState();
}
