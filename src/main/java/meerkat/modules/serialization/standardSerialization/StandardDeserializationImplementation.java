package meerkat.modules.serialization.standardSerialization;

import meerkat.modules.gui.IDialog;
import meerkat.modules.gui.IDialogBuilder;
import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

import javax.swing.tree.TreeModel;

public class StandardDeserializationImplementation implements
        IDeserializationImplementation {

    public String path;
    private ReadableByteChannel inputChannel;
    private final FileCreater fileCreater = new FileCreater();
    private final DirectoryTreeCreater directoryTreeCreater = new DirectoryTreeCreater();

    // Uzytkownik musi wybrac sciezke gdzie zdeserializuje sie katalog.
    @Override
    public boolean prepare(IDialogBuilderFactory<?> dialogBuilderFactory) {
    	IDialogBuilder builder = dialogBuilderFactory.newDialogBuilder();
        builder.addLabel("Standard deserialization:")
               .addSeparator()
               .addLabel("Podaj ścieżkę katalogu:")
               .addDirectoryChooser("dc");
        IDialog d = builder.build();
        
        if(d.exec()) {
            path = d.getDirectoryValue("dc").getAbsolutePath();
            return true;
        }
        return false;
    }

    @Override
    public void run() throws Exception {
    	ByteBuffer buf = ByteBuffer.allocate(4);

		int bytesRead = inputChannel.read(buf);
		byte[] bytes = buf.array();
		int treeSize = bytesToInt(bytes);

		InputStream ind = null;
		ind = Channels.newInputStream(inputChannel);

		bytes = new byte[(int) treeSize];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = ind.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		in = new ObjectInputStream(bis);

		DirectoryNode node = (DirectoryNode) in.readObject();

        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }

        directoryTreeCreater.createTree(node, path);

        node.DFSCreateFiles(fileCreater, inputChannel, path);

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
        this.inputChannel = channel;

    }

}
