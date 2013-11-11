package meerkat.modules.core;

import meerkat.modules.PluginNotFoundException;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 * Klasa implementująca ten interfejs dostarcza funkcjonalności menażera pluginów.
 *
 * @author Maciej Poleski
 */
public interface IPluginManager {
    /**
     * Zwraca plugin serializacji o podanym id.
     *
     * @param id ID pluginu.
     * @return Plugin o podanym ID.
     * @throws PluginNotFoundException Jeżeli nie zarejestrowano pluginu serializacji o podanym ID.
     */
    ISerializationPlugin getSerializationPluginForId(String id) throws PluginNotFoundException;

    /**
     * Zwraca plugin szyfrowania o podanym id.
     *
     * @param id ID pluginu.
     * @return Plugin o podanym ID.
     * @throws PluginNotFoundException Jeżeli nie zarejestrowano pluginu szyfrowania o podanym ID.
     */
    IEncryptionPlugin getEncryptionPluginForId(String id) throws PluginNotFoundException;
}
