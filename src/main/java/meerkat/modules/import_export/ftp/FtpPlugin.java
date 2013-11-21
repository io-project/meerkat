package meerkat.modules.import_export.ftp;

import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.import_export.IImportImplementation;

/**
 *
 * 
 */
public class FtpPlugin implements IImportExportPlugin {

    private static String name = "FTP I/E";
    
    @Override
    public IImportImplementation getImportImplementation() {
        return new FtpImport();
    }

    @Override
    public IExportImplementation getExportImplementation() {
        return new FtpExport();
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
