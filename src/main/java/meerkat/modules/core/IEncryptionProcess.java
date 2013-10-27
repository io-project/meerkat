package meerkat.modules.core;

/**
 * Interfejs pozwalający na komunikacje z zadaniem.
 *
 * Implementacje wszystkich metod tego interfejsu powinny być thread-safe.
 *
 * @author Maciej Poleski
 */
public interface IEncryptionProcess {

    public enum State {
        READY,
        WORKING,
        FINISHED,
        ABORTED,
    }

    /**
     * Uruchom proces.
     *
     * @return Stan w jakim znajduje się proces po powrocie z tej funkcji
     */
    State run();

    /**
     * Przerwij proces. Można wywołać tylko gdy proces jest w stanie State.WORKING.
     */
    void abort();
}
