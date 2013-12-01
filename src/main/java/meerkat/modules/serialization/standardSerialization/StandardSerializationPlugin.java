package meerkat.modules.serialization.standardSerialization;

import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

import javax.swing.tree.TreeModel;

import meerkat.modules.core.IResultCallback;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;
import meerkat.modules.serialization.ISerializationImplementation;
import meerkat.modules.serialization.ISerializationPlugin;

public class StandardSerializationPlugin implements ISerializationPlugin{

	private static final String name = "Standard Serialization";
	
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
		return new StandardSerializationImplementation();
	}

	@Override
	public IDeserializationImplementation getDeserializationImplementation() {
		return new StandardDeserializationImplementation();
	}

	@Override
	public IDeserializationPreviewImplementation getDeserializationPreviewImplementation() {
		return new StandardDeserializationPreviewImplementation();
	}

}
