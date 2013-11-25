package meerkat.modules.import_export.standard;

import meerkat.modules.import_export.IExportImplementation;
import meerkat.modules.import_export.IImportExportPlugin;
import meerkat.modules.import_export.IImportImplementation;

/**
 *
 * @author Tomasz Nocek
 */
public class BasicPlugin implements IImportExportPlugin {

    private static final String name = "Standard I/E";
    
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
