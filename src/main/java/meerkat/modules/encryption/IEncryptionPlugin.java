package meerkat.modules.encryption;

import meerkat.modules.IPlugin;

/**
 * Pluginy obsługujące proces szyfrowania muszą implementować ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface IEncryptionPlugin extends IPlugin {

    /**
     * Zwraca implementacje funkcjonalności szyfrowania.
     */
    IEncryptionImplementation getEncryptionImplementation();

    /**
     * Zwraca implementacje funkcjonalności deszyfrowania.
     */
    IDecryptionImplementation getDecryptionImplementation();
}
