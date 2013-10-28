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
        ABORTED,
    }

    void start();

    void abort();

    State getState();
}
