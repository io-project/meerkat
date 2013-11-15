package meerkat.modules.serialization.simpleSerialization;

import meerkat.modules.serialization.IDeserializationImplementation;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;
import meerkat.modules.serialization.ISerializationImplementation;
import meerkat.modules.serialization.ISerializationPlugin;

public class SimpleSerializationPlugin implements ISerializationPlugin{

	private static final String name = "Simple Serialization";
	
	@Override
	public String getUserVisibleName() {
		return name;
	}

	@Override
	public String getUniquePluginId() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public ISerializationImplementation getSerializationImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDeserializationImplementation getDeserializationImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDeserializationPreviewImplementation getDeserializationPreviewImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

}
