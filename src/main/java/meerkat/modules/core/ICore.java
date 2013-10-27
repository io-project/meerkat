package meerkat.modules.core;

/**
 * Funkcjonalność oferowana przez Core dla pozostałych modułów.
 *
 * @author Maciej Poleski
 */
public interface ICore {
    /**
     * Tworzy obiekt służący do komunikacji z zadaniem szyfrowania
     *
     * @param pipeline Zainicjalizowany pipeline wybranymi przez użytkownika pluginami.
     * @return Obiekt pozwalający na komunikację z procesem szyfrowania.
     */
    IEncryptionProcess prepareEncryptionProcess(EncryptionPipeline pipeline);
}
