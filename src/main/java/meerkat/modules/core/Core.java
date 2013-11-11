package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.NoGuiPluginRegistered;
import meerkat.modules.PluginCollisionException;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.gui.IGuiImplementation;
import meerkat.modules.gui.IGuiPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Zarządza modułami i uruchamia zdefiniowaną w GUI logike.
 *
 * @author Maciej Poleski
 */
class Core implements ICore {
    private final List<ISerializationPlugin> serializationPlugins = new ArrayList<>();
    private final List<IEncryptionPlugin> encryptionPlugins = new ArrayList<>();
    private final List<IImportExportPlugin> importExportPlugins = new ArrayList<>();
    private final List<IOverridePlugin> overridePlugins = new ArrayList<>();
    private IGuiPlugin guiPlugin;
    private IGuiImplementation guiImplementation;

    void registerPlugin(ISerializationPlugin p) {
        checkForPluginIdCollision(serializationPlugins, p);
        serializationPlugins.add(p);
    }

    void registerPlugin(IEncryptionPlugin p) {
        checkForPluginIdCollision(encryptionPlugins, p);
        encryptionPlugins.add(p);
    }

    void registerPlugin(IImportExportPlugin p) {
        checkForPluginIdCollision(importExportPlugins, p);
        importExportPlugins.add(p);
    }

    void registerPlugin(IOverridePlugin p) {
        checkForPluginIdCollision(overridePlugins, p);
        overridePlugins.add(p);
    }

    void registerPlugin(IGuiPlugin p) {
        if (guiPlugin != null) {
            throw new PluginCollisionException("GUI module already registered");
        }
        guiPlugin = p;
    }

    private void checkForPluginIdCollision(List<? extends IPlugin> pluginList, IPlugin plugin) {
        for (IPlugin p : pluginList) {
            if (p.getUniquePluginId().equals(plugin.getUniquePluginId())) {
                throw new PluginCollisionException("Plugin ID " + plugin.getUniquePluginId() + " is already registered");
            }
        }
    }

    /**
     * Rozruch zainicjalizowanego modułu Core.
     */
    public void start() {
        // Application entry point.

        if (guiPlugin == null) {
            throw new NoGuiPluginRegistered();
        }

        guiImplementation = guiPlugin.getImplementation(this);

        System.out.println("Hello World!");         // TODO: usunąć tą linie
    }

    @Override
    public IJob prepareEncryptionJob(EncryptionPipeline pipeline, IJobObserver observer) {
        return new EncryptionJob(pipeline, observer, guiImplementation.getDialogBuilderFactory());
    }

    @Override
    public IJob prepareDecryptionJob(IImportExportPlugin importPlugin, IJobObserver observer) {
        return new DecryptionJobTemplate(importPlugin, observer, guiImplementation.getDialogBuilderFactory());
    }
}
