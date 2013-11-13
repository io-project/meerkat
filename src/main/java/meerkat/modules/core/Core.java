package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.NoGuiPluginRegistered;
import meerkat.modules.PluginCollisionException;
import meerkat.modules.PluginNotFoundException;
import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.gui.IGuiImplementation;
import meerkat.modules.gui.IGuiPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

import javax.swing.tree.TreeModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Zarządza modułami i uruchamia zdefiniowaną w GUI logike.
 *
 * @author Maciej Poleski
 */
class Core implements ICore, IPluginManager {
    private final List<ISerializationPlugin> serializationPlugins = new ArrayList<>();
    private final List<IEncryptionPlugin> encryptionPlugins = new ArrayList<>();
    private final List<IImportExportPlugin> importExportPlugins = new ArrayList<>();
    private final List<IOverridePlugin> overridePlugins = new ArrayList<>();
    private IGuiPlugin guiPlugin;
    private IGuiImplementation guiImplementation;

    @Override
    public List<ISerializationPlugin> getSerializationPlugins() {
        return Collections.unmodifiableList(serializationPlugins);
    }

    @Override
    public List<IEncryptionPlugin> getEncryptionPlugins() {
        return Collections.unmodifiableList(encryptionPlugins);
    }

    @Override
    public List<IImportExportPlugin> getImportExportPlugins() {
        return Collections.unmodifiableList(importExportPlugins);
    }

    @Override
    public List<IOverridePlugin> getOverridePlugins() {
        return Collections.unmodifiableList(overridePlugins);
    }

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
    public IJob prepareEncryptionJob(EncryptionPipeline pipeline, IJobObserver observer, IResultHandler<Void> resultHandler) {
        return new EncryptionJob(pipeline, observer, guiImplementation.getDialogBuilderFactory(), resultHandler);
    }

    @Override
    public IJob prepareDecryptionJob(IImportExportPlugin importPlugin, IJobObserver observer, IResultHandler<Void> resultHandler) {
        return new DecryptionJobTemplate<>(importPlugin, observer, guiImplementation.getDialogBuilderFactory(), this, new DecryptionImplementationProvider(), resultHandler);
    }

    @Override
    public IJob prepareDecryptionPreviewJob(IImportExportPlugin importPlugin, IJobObserver observer, IResultHandler<TreeModel> resultHandler) {
        return new DecryptionJobTemplate<>(importPlugin, observer, guiImplementation.getDialogBuilderFactory(), this, new DecryptionPreviewImplementationProvider(), resultHandler);
    }

    private <T extends IPlugin> T getPluginForId(String id, List<T> plugins) throws PluginNotFoundException {
        for (T p : plugins) {
            if (p.getUniquePluginId().equals(id)) {
                return p;
            }
        }
        throw new PluginNotFoundException(id);
    }

    @Override
    public ISerializationPlugin getSerializationPluginForId(String id) throws PluginNotFoundException {
        return getPluginForId(id, serializationPlugins);
    }

    @Override
    public IEncryptionPlugin getEncryptionPluginForId(String id) throws PluginNotFoundException {
        return getPluginForId(id, encryptionPlugins);
    }
}
