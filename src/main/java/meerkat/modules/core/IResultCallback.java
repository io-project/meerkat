package meerkat.modules.core;

/**
 * Klasa może zaimplementować ten interfejs aby otrzymać rezultat operacji.
 *
 * @param <T> typ rezultatu
 * @author Maciej Poleski
 */
public interface IResultCallback<T> {
    void setResult(T result);
}
