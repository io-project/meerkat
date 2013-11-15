package meerkat.modules.core;

import meerkat.modules.IPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Struktura przechowujące informacje o stanie danego pluginu.
 *
 * @author Maciej Poleski
 */
public class PluginHealthStatus {
    public static final String PLUGIN_HAS_NO_NAME = "Plugin nie ma nazwy";
    public static final String PLUGIN_HAS_EMPTY_NAME = "Plugin ma pustą nazwę";
    public static final String PLUGIN_HAS_NO_ID = "Plugin nie ma identyfikatora";
    public static final String PLUGIN_HAS_EMPTY_ID = "Plugin ma pusty identyfikator";
    public final IPlugin plugin;
    final List<String> messages = new ArrayList<>();

    PluginHealthStatus(IPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isReady() {
        return messages.isEmpty();
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
