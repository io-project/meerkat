package projekt.modules.core;

import projekt.modules.core.ICore;
import projekt.modules.IPlugin;
import projekt.modules.NoGuiPluginRegistered;
import projekt.modules.PluginCollisionException;
import projekt.modules.encryption.IEncryptionPlugin;
import projekt.modules.gui.IGuiPlugin;
import projekt.modules.import_export.IImportExportPlugin;
import projekt.modules.plausible_deniability.IOverridePlugin;
import projekt.modules.serialization.ISerializationPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Zarządza modułami i uruchamia zdefiniowaną w GUI logike.
 *
 * @author Maciej Poleski
 */
public class Core implements ICore {
    List<ISerializationPlugin> serializationPlugins = new ArrayList<ISerializationPlugin>();
    List<IEncryptionPlugin> encryptionPlugins = new ArrayList<IEncryptionPlugin>();
    List<IImportExportPlugin> importExportPlugins = new ArrayList<IImportExportPlugin>();
    List<IOverridePlugin> overridePlugins = new ArrayList<IOverridePlugin>();
    IGuiPlugin guiPlugin;

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

    void registerPlugin(IGuiPlugin p)
    {
        if(guiPlugin!=null)
        {
            throw new PluginCollisionException("GUI module already registered");
        }
        guiPlugin=p;
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

        if(guiPlugin==null)
        {
            throw new NoGuiPluginRegistered();
        }

        guiPlugin.provideCore(this);
        guiPlugin.getImplementation().start();

        System.out.println("Hello World!");
    }
}
