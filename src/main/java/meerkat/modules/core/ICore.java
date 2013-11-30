package meerkat.modules.core;

import meerkat.modules.encryption.IEncryptionPlugin;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.plausible_deniability.IOverridePlugin;
import meerkat.modules.serialization.ISerializationPlugin;

import javax.swing.tree.TreeModel;
import java.util.List;

/**
 * Funkcjonalność oferowana przez moduł Core dla klienta (np GUI). Jeden z dwóch najważniejszych interfejsów tego
 * modułu (drugim jest {@code IJob}).
 *
 * @author Maciej Poleski
 */
public interface ICore {
    /**
     * @return Lista (tylko do odczytu) dostępnych pluginów serializacji.
     */
    List<ISerializationPlugin> getSerializationPlugins();

    /**
     * @return Lista (tylko do odczytu) dostępnych pluginów szyfrowania.
     */
    List<IEncryptionPlugin> getEncryptionPlugins();

    /**
     * @return Lista (tylko do odczytu) dostępnych pluginów importu/eksportu.
     */
    List<IImportExportPlugin> getImportExportPlugins();

    /**
     * @return Lista (tylko do odczytu) dostępnych pluginów nadpisywania.
     */
    List<IOverridePlugin> getOverridePlugins();

    /**
     * Tworzy obiekt służący do komunikacji z zadaniem szyfrowania.
     *
     * @param pipeline      Zainicjalizowany pipeline wybranymi przez użytkownika pluginami.
     * @param observer      Opcjonalny obserwator tego zadania.
     * @param resultHandler handler który zostanie uruchomiony po zakończeniu zadania.
     * @return Obiekt pozwalający na komunikację z procesem szyfrowania.
     */
    IJob prepareEncryptionJob(EncryptionPipeline pipeline, IJobObserver observer, IResultHandler<Void> resultHandler);

    /**
     * Tworzy obiekt służący do komunikacji z zadaniem deszyfrowania.
     *
     * @param importPlugin  Plugin służący do importu (wybrany przez użytkownika)
     * @param observer      Opcjonalny obserwator tego zadania.
     * @param resultHandler handler który zostanie uruchomiony po zakończeniu zadania.
     * @return Obiekt pozwalający na komunikację z procesem deszyfrowania.
     */
    IJob prepareDecryptionJob(IImportExportPlugin importPlugin, IJobObserver observer, IResultHandler<Void> resultHandler);

    /**
     * Tworzy obiekt służący do komunikacji z zadaniem podglądu zaszyfrowanej struktury.
     *
     * @param importPlugin  Plugin służący do importu (wybrany przez użytkownika)
     * @param observer      Opcjonalny obserwator tego zadania.
     * @param resultHandler handler który zostanie uruchomiony po zakończeniu zadania.
     * @return Obiekt pozwalający na komunikację z procesem podglądu zaszyfrowanej struktury.
     */
    IJob prepareDecryptionPreviewJob(IImportExportPlugin importPlugin, IJobObserver observer, IResultHandler<TreeModel> resultHandler);


}
