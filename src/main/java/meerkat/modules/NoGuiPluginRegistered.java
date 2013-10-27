package meerkat.modules;

/**
 * Sygnalizuje rozruch aplikacji bez dostarczenia jakiegokolwiek GUI.
 *
 * @author Maciej Poleski
 */
public class NoGuiPluginRegistered extends RuntimeException {
    public NoGuiPluginRegistered() {
    }

    public NoGuiPluginRegistered(String message) {
        super(message);
    }

    public NoGuiPluginRegistered(String message, Throwable cause) {
        super(message, cause);
    }

    public NoGuiPluginRegistered(Throwable cause) {
        super(cause);
    }

    public NoGuiPluginRegistered(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
