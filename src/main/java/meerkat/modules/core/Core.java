package meerkat.modules.core;

import meerkat.modules.IPlugin;
import meerkat.modules.NoGuiPluginRegisteredException;
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
 * <p/>
 * To nie jest część API. To nieistotny detal implementacyjny.
 *
 * @author Maciej Poleski
 */
class Core implements ICore, IPluginManager {
    private final List<ISerializationPlugin> serializationPlugins = new ArrayList<>();
    private final List<IEncryptionPlugin> encryptionPlugins = new ArrayList<>();
    private final List<IImportExportPlugin> importExportPlugins = new ArrayList<>();
    private final List<IOverridePlugin> overridePlugins = new ArrayList<>();
    /**
     * Lista pluginów które spróbowano załadować, ale moduł kontrolny nie pozwolił na to z powodu wykrytych uszkodzeń.
     */
    private final List<PluginHealthStatus> brokenPlugins = new ArrayList<>();
    /**
     * Plugin dostarczający funkcjonalność interfejsu użytkownika może (musi) być tylko (dokładnie) jeden.
     */
    private IGuiPlugin guiPlugin;
    /**
     * To pole służy do cache-owania implementacji interfejsu użytkownika.
     */
    private IGuiImplementation guiImplementation;

    /**
     * Odszukuje plugin o wskazanym ID w liście zarejestrowanych pluginów.
     *
     * @param id      ID poszukiwanego pluginu.
     * @param plugins Lista pluginów wśród których powinien być plugin o ID {@code id}.
     * @param <T>     Konkretny typ (kategoria) pluginu.
     * @return Plugin o ID {@code id}.
     * @throws PluginNotFoundException Jeżeli na liście {@code plugins} nie ma pluginu o ID {@code id}.
     */
    private static <T extends IPlugin> T getPluginForId(String id, List<T> plugins) throws PluginNotFoundException {
        for (T p : plugins) {
            if (p.getUniquePluginId().equals(id)) {
                return p;
            }
        }
        throw new PluginNotFoundException("Plugin not found: " + id);
    }

    /**
     * Sprawdza czy aktualnie rejestrowany plugin koliduje z jakimś już zarejestrowanym.
     *
     * @param pluginList Lista już zarejestrowanym pluginów z danej kategorii.
     * @param plugin     Aktualnie rejestrowany plugin.
     * @throws PluginCollisionException Jeżeli {@code plugin} koliduje z którymś z pluginów z listy {@code pluginList}
     */
    private static void checkForPluginIdCollision(List<? extends IPlugin> pluginList, IPlugin plugin) {
        for (IPlugin p : pluginList) {
            if (p.getUniquePluginId().equals(plugin.getUniquePluginId())) {
                throw new PluginCollisionException("Plugin ID " + plugin.getUniquePluginId() + " is already registered");
            }
        }
    }

    /**
     * Zgłasza użytkownikowi w trybie awaryjnym informację o nieprawidłowej pracy pluginu.
     *
     * @param phs Informacja o stanie danego niedziałającego pluginu.
     */
    private static void reportPluginOutOfOrder(PluginHealthStatus phs) {
        if (phs.messages.isEmpty())
            return;
        System.err.println("Plugin >>>" + phs.plugin.getUserVisibleName() + "<<< o identyfikatorze >>>" + phs.plugin.getUniquePluginId() + "<<< jest niesprawny:");
        for (String message : phs.getMessages()) {
            System.err.println("\t" + message);
        }
    }

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

    /**
     * Próbuje zarejestrować plugin obsługujący serializacje.
     *
     * @param p Plugin obsługujący serializacje.
     */
    void registerPlugin(ISerializationPlugin p) {
        PluginHealthStatus pluginHealthStatus = Plugins.getHealthStatus(p);
        if (pluginHealthStatus.isReady()) {
            checkForPluginIdCollision(serializationPlugins, p);
            serializationPlugins.add(p);
        } else {
            brokenPlugins.add(pluginHealthStatus);
        }
    }

    /**
     * Próbuje zarejestrować plugin obsługujący szyfrowanie.
     *
     * @param p Plugin obsługujący szyfrowanie.
     */
    void registerPlugin(IEncryptionPlugin p) {
        PluginHealthStatus pluginHealthStatus = Plugins.getHealthStatus(p);
        if (pluginHealthStatus.isReady()) {
            checkForPluginIdCollision(encryptionPlugins, p);
            encryptionPlugins.add(p);
        } else {
            brokenPlugins.add(pluginHealthStatus);
        }
    }

    /**
     * Próbuje zarejestrować plugin obsługujący import/eksport.
     *
     * @param p Plugin obsługujący import/eksport.
     */
    void registerPlugin(IImportExportPlugin p) {
        PluginHealthStatus pluginHealthStatus = Plugins.getHealthStatus(p);
        if (pluginHealthStatus.isReady()) {
            checkForPluginIdCollision(importExportPlugins, p);
            importExportPlugins.add(p);
        } else {
            brokenPlugins.add(pluginHealthStatus);
        }
    }

    /**
     * Próbuje zarejestrować plugin obsługujący nadpisywanie.
     *
     * @param p Plugin obsługujący nadpisywanie.
     */
    void registerPlugin(IOverridePlugin p) {
        PluginHealthStatus pluginHealthStatus = Plugins.getHealthStatus(p);
        if (pluginHealthStatus.isReady()) {
            checkForPluginIdCollision(overridePlugins, p);
            overridePlugins.add(p);
        } else {
            brokenPlugins.add(pluginHealthStatus);
        }
    }

    /**
     * Próbuje zarejestrować plugin dostarczający interfejs użytkownika dla aplikacji.
     *
     * @param p Plugin dostarczający interfejs użytkownika.
     * @throws PluginCollisionException Jeżeli już zarejestrowano jakiś plugin dostarczający funkcjonalność interfejsu
     *                                  użytkownika.
     */
    void registerPlugin(IGuiPlugin p) {
        if (guiPlugin != null) {
            throw new PluginCollisionException("GUI module already registered");
        }
        guiPlugin = p;
    }

    /**
     * Uruchamia zainicjowany moduł Core. Oznacza to ostateczne sprawdzenie dostępności pluginu GUI, wyświetlenie
     * informacji o niesprawnych pluginach i w końcu uruchomienie aplikacji (jeżeli jest to możliwe).
     *
     * @throws NoGuiPluginRegisteredException Jeżeli nie zarejestrowano żadnego pluginu GUI.
     */
    public void start() {
        // Application entry point.

        if (guiPlugin == null) {
            reportBrokenPlugins();
            throw new NoGuiPluginRegisteredException();
        }

        guiImplementation = guiPlugin.getImplementation(this);
        if (guiImplementation == null) {
            reportBrokenPlugins();
        } else {
            // guiImplementation.showBrokenPlugins(brokenPlugins);   // TODO ta linia nie powinna być komentarzem
            reportBrokenPlugins();                                   // TODO a tej nie powinno być
            guiImplementation.start();
        }
    }

    /**
     * Zgłasza użytkownikowi w trybie awaryjnym informację o wszystkich niesprawnych pluginach.
     */
    private void reportBrokenPlugins() {
        for (PluginHealthStatus phs : brokenPlugins)
            reportPluginOutOfOrder(phs);
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

    @Override
    public ISerializationPlugin getSerializationPluginForId(String id) throws PluginNotFoundException {
        return getPluginForId(id, serializationPlugins);
    }

    @Override
    public IEncryptionPlugin getEncryptionPluginForId(String id) throws PluginNotFoundException {
        return getPluginForId(id, encryptionPlugins);
    }
}
