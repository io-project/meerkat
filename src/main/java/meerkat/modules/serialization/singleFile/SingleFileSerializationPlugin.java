package meerkat.modules.serialization.singleFile;

import meerkat.modules.core.IResultCallback;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;
import meerkat.modules.serialization.ISerializationImplementation;
import meerkat.modules.serialization.ISerializationPlugin;

import javax.swing.tree.TreeModel;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

/**
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
        return new IDeserializationPreviewImplementation() {

            @Override
            public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(T channel) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public void setResultCallback(IResultCallback<TreeModel> resultCallback) {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

            @Override
            public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
                throw new UnsupportedOperationException("Not supported yet.");  
            }

            @Override
            public void run() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); 
            }

        };
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
