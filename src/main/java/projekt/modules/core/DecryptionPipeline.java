package projekt.modules.core;

import projekt.modules.encryption.IEncryptionPlugin;
import projekt.modules.import_export.IImportExportPlugin;
import projekt.modules.serialization.ISerializationPlugin;

/**
 * Dobór pluginów w celu realizacji całokształtu działań związanych z deszyfrowaniem
 *
 * @author Maciej Poleski
 */
public class DecryptionPipeline {
    ISerializationPlugin serializationPlugin;
    IEncryptionPlugin encryptionPlugin;
    IImportExportPlugin importExportPlugin;

    public DecryptionPipeline() {
    }

    public DecryptionPipeline(ISerializationPlugin serializationPlugin, IEncryptionPlugin encryptionPlugin, IImportExportPlugin importExportPlugin) {

        this.serializationPlugin = serializationPlugin;
        this.encryptionPlugin = encryptionPlugin;
        this.importExportPlugin = importExportPlugin;
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
}
