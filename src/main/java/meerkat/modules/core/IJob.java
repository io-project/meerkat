package meerkat.modules.core;

/**
 * Interfejs pozwalający na komunikacje z zadaniem. Jeden z dwóch najważniejszych interfejsów tego modułu (drugim jest
 * {@code ICore}). Każde utworzone zadanie początkowo znajduje się w stanie {@code State.READY}. Aby faktycznie
 * uruchomić zadanie należy wywołać metodę {@code start()}. Zadanie jest wykonywane asynchronicznie. W każdej chwili
 * można zapytać w jakim stanie znajduje się zadanie oraz spróbować je anulować.
 * <p/>
 * Implementacje wszystkich metod tego interfejsu powinny być thread-safe.
 *
 * @author Maciej Poleski
 */
public interface IJob {

    /**
     * Uruchamia zadanie. Metoda działa nie blokująco. Faktyczne zadanie jest wykonywane asynchronicznie.
     */
    void start();

    /**
     * Anuluje zadanie. Nie ma gwarancji że zadanie będzie anulowane. Przykładowo zakończonego zadania prawdopodobnie
     * nie uda się anulować. Metoda działa nie blokująco. Faktyczne anulowanie jest wykonywane asynchronicznie.
     */
    void abort();

    /**
     * Zwraca obecny stan zadania. Tak naprawdę już po zakończeniu tej funkcji stan może ulec zmianie. Nie można
     * opierać się na jej rezultacie. Jest to jedynie luźna informacja dla użytkownika.
     *
     * @return Obecny stan zadania.
     */
    State getState();

    /**
     * Typ wyliczeniowy opisuje zgrubsza w jakim stanie może się znajdować zadanie. Jest to jedynie przybliżona
     * informacja.
     */
    public enum State {
        READY,
        PREPARING,
        WORKING,
        FINISHED,
        FAILED,
        ABORTED,
    }
}
