package meerkat.modules.import_export.basic;

import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.import_export.IImportImplementation;

/**
 *
 * @author Tomasz Nocek
 */
public class BasicPlugin implements IImportExportPlugin {

    private static String name = "Basic I/E";
    
    @Override
    public IImportImplementation getImportImplementation() {
        return new BasicImport();
    }

    @Override
    public IExportImplementation getExportImplementation() {
        return new BasicExport();
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
