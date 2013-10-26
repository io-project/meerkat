package projekt.modules;

/**
 * Wyjątek sygnalizuje próbę załadowaniu pluginu o identyfikatorze już zajętym w danym module.
 *
 * @author Maciej Poleski
 */
public class PluginCollisionException extends RuntimeException {
    public PluginCollisionException() {
    }

    public PluginCollisionException(String message) {
        super(message);
    }

    public PluginCollisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginCollisionException(Throwable cause) {
        super(cause);
    }

    public PluginCollisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
