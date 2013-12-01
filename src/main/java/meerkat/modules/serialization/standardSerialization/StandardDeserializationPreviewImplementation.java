package meerkat.modules.serialization.standardSerialization;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

import javax.swing.tree.TreeModel;

import meerkat.modules.core.IResultCallback;
import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationPreviewImplementation;

public class StandardDeserializationPreviewImplementation implements
		IDeserializationPreviewImplementation {
	
	private String path;
	private ReadableByteChannel inputChannel;
	private final TreeModelBuilder treeModelBuilder = new TreeModelBuilder();
	private IResultCallback<TreeModel> resultCallback;

    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
    	return true;
    }

	@Override
	public void run() throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(4);

        int bytesRead = inputChannel.read(buf);
        byte[] bytes = buf.array();
        int treeSize = bytesToInt(bytes);

        buf = ByteBuffer.allocate(treeSize);
        bytesRead = inputChannel.read(buf);

        bytes = buf.array();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        in = new ObjectInputStream(bis);
        DirectoryNode node = (DirectoryNode) in.readObject();
        
        TreeModel treeModel;
        treeModel = treeModelBuilder.buildTreeModel(node);
        
        resultCallback.setResult(treeModel);

	}
	
	public static int bytesToInt(byte[] int_bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(int_bytes);
        DataInputStream ois = new DataInputStream(bis);
        int my_int = ois.readInt();
        ois.close();
        return my_int;
    }

	@Override
	public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
			T channel) {
		
		inputChannel = channel;
		
	}

	@Override
	public void setResultCallback(IResultCallback<TreeModel> resultCallback) {
		
		this.resultCallback = resultCallback;

	}

}
