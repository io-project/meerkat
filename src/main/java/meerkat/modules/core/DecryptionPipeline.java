package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 * Dobór pluginów w celu realizacji całokształtu działań związanych z deszyfrowaniem. Jest on odtwarzany podczas
 * importowania aby zwolnić użytkownika z konieczności ręcznego wprowadzania tych danych.
 * <p/>
 * To nie jest część API. To nieistotny detal implementacyjny.
 *
 * @author Maciej Poleski
 */
class DecryptionPipeline {
    private ISerializationPlugin serializationPlugin;
    private IEncryptionPlugin encryptionPlugin;
    private IImportExportPlugin importExportPlugin;

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
