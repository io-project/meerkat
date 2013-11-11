package meerkat.modules.core;

import meerkat.modules.import_export.IImportExportPlugin;

/**
 * Funkcjonalność oferowana przez Core dla pozostałych modułów.
 *
 * @author Maciej Poleski
 */
public interface ICore {
    /**
     * Tworzy obiekt służący do komunikacji z zadaniem szyfrowania
     *
     * @param pipeline      Zainicjalizowany pipeline wybranymi przez użytkownika pluginami.
     * @param observer      Obserwator tego zadania.
     * @param resultHandler handler który zostanie uruchomiony po zakończeniu zadania.
     * @return Obiekt pozwalający na komunikację z procesem szyfrowania.
     */
    IJob prepareEncryptionJob(EncryptionPipeline pipeline, IJobObserver observer, IResultHandler<Void> resultHandler);

    /**
     * Tworzy obiekt służący do komunikacji z zadaniem deszyfrowania.
     *
     * @param importPlugin  Plugin służący do importu (wybrany przez użytkownika)
     * @param observer      Obserwator tego zadania.
     * @param resultHandler handler który zostanie uruchomiony po zakończeniu zadania.
     * @return Obiekt pozwalający na komunikację z procesem deszyfrowania.
     */
    IJob prepareDecryptionJob(IImportExportPlugin importPlugin, IJobObserver observer, IResultHandler<Void> resultHandler);

//    /**
//     * Tworzy obiekt służący do komunikacji z zadaniem podglądu zaszyfrowanej struktury.
//     *
//     * @param importPlugin  Plugin służący do importu (wybrany przez użytkownika)
//     * @param observer      Obserwator tego zadania.
//     * @param resultHandler handler który zostanie uruchomiony po zakończeniu zadania.
//     * @return Obiekt pozwalający na komunikację z procesem deszyfrowania.
//     */
//    IJob prepareDecryptionPreviewJob(IImportExportPlugin importPlugin, IJobObserver observer, IResultHandler<TreeModel> resultHandler);


}
