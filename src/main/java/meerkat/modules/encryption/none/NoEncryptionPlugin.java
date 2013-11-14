package meerkat.modules.encryption.none;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.encryption.IEncryptionPlugin;

/**
 *
 * @author Tomasz Nocek
 */
public class NoEncryptionPlugin implements IEncryptionPlugin {

    private final String name = "None";
    
    @Override
    public IEncryptionImplementation getEncryptionImplementation() {
        return new NoEncryptionImplementation();
    }

    @Override
    public IDecryptionImplementation getDecryptionImplementation() {
        return new NoDecryptionImplementation();
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
