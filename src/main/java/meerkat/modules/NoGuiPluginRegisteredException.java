package meerkat.modules;

/**
 * Sygnalizuje rozruch aplikacji bez dostarczenia jakiegokolwiek GUI.
 *
 * @author Maciej Poleski
 */
public class NoGuiPluginRegisteredException extends RuntimeException {
    public NoGuiPluginRegisteredException() {
    }

    public NoGuiPluginRegisteredException(String message) {
        super(message);
    }

    public NoGuiPluginRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoGuiPluginRegisteredException(Throwable cause) {
        super(cause);
    }

    public NoGuiPluginRegisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
