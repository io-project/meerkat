package meerkat.modules.core;

/**
 * Ta klasa służy do uruchomienia aplikacji.
 *
 * @author Maciej Poleski
 */
public class App {
    private static void registerAllPlugins(Core core) {
        // Tutaj należy rejestrować pluginy
        // core.registerPlugin(...);
    }

    public static void main(String[] args) {
        Core core = new Core();
        registerAllPlugins(core);
        core.start();
    }
}
