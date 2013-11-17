package meerkat.modules.core;

import meerkat.modules.encryption.none.NoEncryptionPlugin;
import meerkat.modules.encryption.xor.XorPlugin;
import meerkat.modules.gui.simple.GuiPlugin;
import meerkat.modules.import_export.basic.BasicPlugin;
import meerkat.modules.import_export.dropbox.DropboxPlugin;
import meerkat.modules.plausible_deniability.none.NoOverridePlugin;
import meerkat.modules.serialization.singleFile.SingleFileSerializationPlugin;
import meerkat.modules.serialization.standardSerialization.StandardSerializationPlugin;

/**
 * Ta klasa służy do uruchomienia aplikacji.
 *
 * @author Maciej Poleski
 */
public class App {
    private static void registerAllPlugins(Core core) {
        // Tutaj należy rejestrować pluginy
        // core.registerPlugin(...);
        core.registerPlugin(new XorPlugin());
        core.registerPlugin(new BasicPlugin());
        core.registerPlugin(new StandardSerializationPlugin());
        core.registerPlugin(new NoOverridePlugin());
        core.registerPlugin(new GuiPlugin());
        core.registerPlugin(new SingleFileSerializationPlugin());
        core.registerPlugin(new NoEncryptionPlugin());
        core.registerPlugin(new DropboxPlugin());
    }

    public static void main(String[] args) {
        Core core = new Core();
        registerAllPlugins(core);
        core.start();
    }
}
