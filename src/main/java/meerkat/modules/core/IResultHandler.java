package meerkat.modules.core;

/**
 * Klasa może zaimplementować ten interfejs aby umożliwić zadaniu zgłoszenie rezultatu.
 *
 * @param <T> typ rezultatu
 * @author Maciej Poleski
 */
public interface IResultHandler<T> {
    /**
     * Jeżeli zadanie się zakończyło - ta metoda zostanie wywołana z argumentem będącym rezultatem.
     *
     * @param result Rezultat zadania
     */
    void handleResult(T result);

    /**
     * Jeżeli zadanie zakończyło się wyjątkiem - ta metoda zostanie wywołana z argumentem będącym tym co rzucono.
     *
     * @param t Obiekt którym rzucono.
     */
    void handleException(Throwable t);
}
