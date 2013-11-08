package meerkat.modules.core;

import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.import_export.IImportExportPlugin;

/**
 * Ta klasa obsługuje zadanie deszyfrowania.
 * <p/>
 * To nie jest część API.
 *
 * @author Maciej Poleski
 */
class DecryptionJob implements IJob {
    public DecryptionJob(IImportExportPlugin importPlugin, IJobObserver handler, IDialogBuilderFactory dialogBuilderFactory) {
    }

    @Override
    public void start() {

    }

    @Override
    public void abort() {

    }

    @Override
    public State getState() {
        return null;
    }
}
