package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 * Klasa typu utility. Dostarcza funkcjonalności pomocne w zarządzaniu pluginami.
 * <p/>
 * To nie jest część API. To nieistotny detal implementacyjny.
 *
 * @author Maciej Poleski
 */
final class Plugins {

    /**
     * Sprawdza podstawowe cechy pluginu - wspólne dla wszystkich.
     *
     * @param plugin Plugin który zostanie sprawdzony
     * @return Podstawowe informacje o pluginie.
     */
    static PluginHealthStatus getHealthStatus(IPlugin plugin) {
        PluginHealthStatus result = new PluginHealthStatus(plugin);
        String name = plugin.getUserVisibleName();
        if (name == null)
            result.messages.add(PluginHealthStatus.PLUGIN_HAS_NO_NAME);
        else if ("".equals(name))
            result.messages.add(PluginHealthStatus.PLUGIN_HAS_EMPTY_NAME);
        String id = plugin.getUniquePluginId();
        if (id == null)
            result.messages.add(PluginHealthStatus.PLUGIN_HAS_NO_ID);
        else if ("".equals(id))
            result.messages.add(PluginHealthStatus.PLUGIN_HAS_EMPTY_ID);
        return result;
    }

    /**
     * Wykonuje podstawowe testy pluginu serializacji.
     *
     * @param serializationPlugin Plugin serializacji który ma zostać przetestowany.
     * @return Raport dotyczący stanu pluginu {@code serializationPlugin}.
     */
    static PluginHealthStatus getHealthStatus(ISerializationPlugin serializationPlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) serializationPlugin);
        if (serializationPlugin.getSerializationImplementation() == null)
            result.messages.add("Plugin nie zaimplementował serializacji");
        if (serializationPlugin.getDeserializationImplementation() == null)
            result.messages.add("Plugin nie zaimplementował deserializacji");
        if (serializationPlugin.getDeserializationPreviewImplementation() == null)
            result.messages.add("Plugin nie zaimplementował podglądu deserializacji");
        return result;
    }

    /**
     * Wykonuje podstawowe testy pluginu szyfrowania.
     *
     * @param encryptionPlugin Plugin szyfrowania który ma zostać przetestowany.
     * @return Raport dotyczący stanu pluginu {@code encryptionPlugin}.
     */
    public static PluginHealthStatus getHealthStatus(IEncryptionPlugin encryptionPlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) encryptionPlugin);
        if (encryptionPlugin.getEncryptionImplementation() == null)
            result.messages.add("Plugin nie zaimplementował szyfrowania");
        if (encryptionPlugin.getDecryptionImplementation() == null)
            result.messages.add("Plugin nie zaimplementował deszyfrowania");
        return result;
    }

    /**
     * Wykonuje podstawowe testy pluginu importu/eksportu.
     *
     * @param importExportPlugin Plugin importu/eksportu który ma zostać przetestowany.
     * @return Raport dotyczący stanu pluginu {@code importExportPlugin}.
     */
    public static PluginHealthStatus getHealthStatus(IImportExportPlugin importExportPlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) importExportPlugin);
        if (importExportPlugin.getExportImplementation() == null)
            result.messages.add("Plugin nie zaimplementował eksportu");
        if (importExportPlugin.getImportImplementation() == null)
            result.messages.add("Plugin nie zaimplementował importu");
        return result;
    }

    /**
     * Wykonuje podstawowe testy pluginu nadpisywania.
     *
     * @param overridePlugin Plugin nadpisywania który ma zostać przetestowany.
     * @return Raport dotyczący stanu pluginu {@code overridePlugin}.
     */
    public static PluginHealthStatus getHealthStatus(IOverridePlugin overridePlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) overridePlugin);
        if (overridePlugin.getOverrideImplementation() == null)
            result.messages.add("Plugin nie zaimplementował nadpisywania");
        return result;
    }
}
