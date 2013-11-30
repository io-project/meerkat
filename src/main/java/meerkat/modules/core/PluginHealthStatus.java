package meerkat.modules.core;

import meerkat.modules.IPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Struktura przechowujące informacje o stanie danego pluginu. Wykorzystywana w praktyce do raportowania informacji
 * o niesprawnych pluginach.
 *
 * @author Maciej Poleski
 */
public class PluginHealthStatus {
    /*
     * Predefiniowane błędy - można założyć że zostaną wykorzystane dokładnie te instancje.
     */
    public static final String PLUGIN_HAS_NO_NAME = "Plugin nie ma nazwy";
    public static final String PLUGIN_HAS_EMPTY_NAME = "Plugin ma pustą nazwę";
    public static final String PLUGIN_HAS_NO_ID = "Plugin nie ma identyfikatora";
    public static final String PLUGIN_HAS_EMPTY_ID = "Plugin ma pusty identyfikator";
    /**
     * Plugin którego dotyczy ten raport.
     */
    public final IPlugin plugin;
    final List<String> messages = new ArrayList<>();

    PluginHealthStatus(IPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Zwraca inforamcję o tym, czy plugin którego dotyczy ten raport jest sprawny.
     *
     * @return true - jeżeli plugin jest sprawny, false - w przeciwnym wypadku.
     */
    public boolean isReady() {
        return messages.isEmpty();
    }

    /**
     * @return Lista wiadomości dotyczących danego pluginu.
     */
    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
