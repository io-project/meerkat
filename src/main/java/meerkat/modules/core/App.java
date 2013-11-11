package meerkat.modules.core;

import meerkat.modules.encryption.xor.XorPlugin;
import meerkat.modules.import_export.basic.BasicPlugin;
import meerkat.modules.serialization.simpleSerialization.SimpleSerializationPlugin;

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
        core.registerPlugin(new SimpleSerializationPlugin());
    }

    public static void main(String[] args) {
        Core core = new Core();
        registerAllPlugins(core);
        core.start();
    }
}
