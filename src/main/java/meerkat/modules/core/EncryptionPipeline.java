package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 * Dobór pluginów w celu realizacji całokształtu działań związanych z szyfrowaniem
 *
 * @author Maciej Poleski
 */
public class EncryptionPipeline {
    private ISerializationPlugin serializationPlugin;
    private IEncryptionPlugin encryptionPlugin;
    private IImportExportPlugin importExportPlugin;
    private IOverridePlugin overridePlugin;

    public EncryptionPipeline() {
    }

    public EncryptionPipeline(ISerializationPlugin serializationPlugin, IEncryptionPlugin encryptionPlugin, IImportExportPlugin importExportPlugin, IOverridePlugin overridePlugin) {

        this.serializationPlugin = serializationPlugin;
        this.encryptionPlugin = encryptionPlugin;
        this.importExportPlugin = importExportPlugin;
        this.overridePlugin = overridePlugin;
    }

    public ISerializationPlugin getSerializationPlugin() {
        return serializationPlugin;
    }

    public void setSerializationPlugin(ISerializationPlugin serializationPlugin) {
        this.serializationPlugin = serializationPlugin;
    }

    public IEncryptionPlugin getEncryptionPlugin() {
        return encryptionPlugin;
    }

    public void setEncryptionPlugin(IEncryptionPlugin encryptionPlugin) {
        this.encryptionPlugin = encryptionPlugin;
    }

    public IImportExportPlugin getImportExportPlugin() {
        return importExportPlugin;
    }

    public void setImportExportPlugin(IImportExportPlugin importExportPlugin) {
        this.importExportPlugin = importExportPlugin;
    }

    public IOverridePlugin getOverridePlugin() {
        return overridePlugin;
    }

    public void setOverridePlugin(IOverridePlugin overridePlugin) {
        this.overridePlugin = overridePlugin;
    }
}
