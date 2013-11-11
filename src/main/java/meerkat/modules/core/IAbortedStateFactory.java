package meerkat.modules.core;

/**
 * Fabryka stanów anulowalnych na potrzeby klasy abstrakcyjnej JobWithStates.
 *
 * @author Maciej Poleski
 */
interface IAbortedStateFactory {
    /**
     * Tworzy i zwraca nowy stan anulowany.
     *
     * @return Nowy stan anulowany.
     */
    IState newAbortedState();
}
