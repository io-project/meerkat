package meerkat.modules.encryption.aes;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;

public class AesPlugin implements IEncryptionPlugin{

	private static String name = "AES Encryption";
	
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
		return new AesEncryption();
	}

	@Override
	public IDecryptionImplementation getDecryptionImplementation() {
		return new AesDecryption();
	}

}
