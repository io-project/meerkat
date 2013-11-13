package meerkat.modules.plausible_deniability.none;

import meerkat.modules.plausible_deniability.IOverrideImplementation;
import meerkat.modules.plausible_deniability.IOverridePlugin;

/**
 *
 * @author Tomasz Nocek
 */
public class NoOverridePlugin implements IOverridePlugin {

    private final String name = "None";
    
    @Override
    public IOverrideImplementation getOverrideImplementation() {
        return new NoOverrideImplementation();
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
