package meerkat.modules.encryption.des;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;

public class DesPlugin implements IEncryptionPlugin{

	private static String name = "DES Encryption";
	
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
		return new DesEncryption();
	}

	@Override
	public IDecryptionImplementation getDecryptionImplementation() {
		return new DesDecryption();
	}

}
