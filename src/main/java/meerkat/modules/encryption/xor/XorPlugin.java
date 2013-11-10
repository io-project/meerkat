package meerkat.modules.encryption.xor;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;

public class XorPlugin implements IEncryptionPlugin{

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
		return new XorEncryption();
	}

	@Override
	public IDecryptionImplementation getDecryptionImplementation() {
		return new XorDecryption();
	}

}
