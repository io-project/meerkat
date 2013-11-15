package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 * Klasa typu utility. Dostarcza funkcjonalności pomocne w zarządzaniu pluginami.
 *
 * @author Maciej Poleski
 */
class Plugins {

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

    public static PluginHealthStatus getHealthStatus(IEncryptionPlugin encryptionPlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) encryptionPlugin);
        if (encryptionPlugin.getEncryptionImplementation() == null)
            result.messages.add("Plugin nie zaimplementował szyfrowania");
        if (encryptionPlugin.getDecryptionImplementation() == null)
            result.messages.add("Plugin nie zaimplementował deszyfrowania");
        return result;
    }

    public static PluginHealthStatus getHealthStatus(IImportExportPlugin importExportPlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) importExportPlugin);
        if (importExportPlugin.getExportImplementation() == null)
            result.messages.add("Plugin nie zaimplementował eksportu");
        if (importExportPlugin.getImportImplementation() == null)
            result.messages.add("Plugin nie zaimplementował importu");
        return result;
    }

    public static PluginHealthStatus getHealthStatus(IOverridePlugin overridePlugin) {
        PluginHealthStatus result = getHealthStatus((IPlugin) overridePlugin);
        if (overridePlugin.getOverrideImplementation() == null)
            result.messages.add("Plugin nie zaimplementował nadpisywania");
        return result;
    }
}
