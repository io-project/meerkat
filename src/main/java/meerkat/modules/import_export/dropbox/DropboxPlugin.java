package meerkat.modules.import_export.dropbox;

import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.import_export.IImportImplementation;

/**
 *
 * 
 */
public class DropboxPlugin implements IImportExportPlugin {

    private static String name = "Dropbox I/E";
    
    @Override
    public IImportImplementation getImportImplementation() {
        return new DropboxImport();
    }

    @Override
    public IExportImplementation getExportImplementation() {
        return new DropboxExport();
    }

    @Override
    public String getUserVisibleName() {
        return name;
    }

    @Override
    public String getUniquePluginId() {
        return this.getClass().getCanonicalName();
    }
    
}
