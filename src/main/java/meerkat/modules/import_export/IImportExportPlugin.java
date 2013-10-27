package meerkat.modules.import_export;

import meerkat.modules.IPlugin;

/**
 * Pluginy obsługujące proces importu i exportu muszą implementować ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface IImportExportPlugin extends IPlugin {

    /**
     * Zwraca implementacje funkcjonalności importu.
     */
    IImportImplementation getImportImplementation();

    /**
     * Zwraca implementacje funkcjonalności eksportu.
     */
    IExportImplementation getExportImplementation();
}
