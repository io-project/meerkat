package meerkat.modules.encryption.basic_encryption;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;

public class BasicEncryptionPlugin implements IEncryptionPlugin{

	private static String name = "Basic Encryption (XOR)";
	
	@Override
	public String getUserVisibleName() {
		return name;
	}

	@Override
	public String getUniquePluginId() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public IEncryptionImplementation getEncryptionImplementation() {
		return new BasicEncryptionImplementation();
	}

	@Override
	public IDecryptionImplementation getDecryptionImplementation() {
		return new BasicDecryptionImplentation();
	}

}
