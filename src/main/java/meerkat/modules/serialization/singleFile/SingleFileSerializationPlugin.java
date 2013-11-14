package meerkat.modules.serialization.singleFile;

import meerkat.modules.serialization.IDeserializationImplementation;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;
import meerkat.modules.serialization.ISerializationImplementation;
import meerkat.modules.serialization.ISerializationPlugin;

/**
 *
 * @author Tomasz Nocek
 */
public class SingleFileSerializationPlugin implements ISerializationPlugin {

    private static final String name = "Single File";
    
    @Override
    public ISerializationImplementation getSerializationImplementation() {
        return new SingleFileSerializationImplementation();
    }

    @Override
    public IDeserializationImplementation getDeserializationImplementation() {
        return new SingleFileDeserializationImplementation();
    }

    @Override
    public IDeserializationPreviewImplementation getDeserializationPreviewImplementation() {
        // TODO Auto-generated method stub
	return null;
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
