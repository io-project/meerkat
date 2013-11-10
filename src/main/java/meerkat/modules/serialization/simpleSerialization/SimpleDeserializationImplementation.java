package meerkat.modules.serialization.simpleSerialization;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.IDeserializationImplementation;

public class SimpleDeserializationImplementation implements
		IDeserializationImplementation {

	public String path;
	private ReadableByteChannel inputChannel;

	// Uzytkownik musi wybrac sciezke gdzie zdeserializuje sie plik.
	@Override
	public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() throws Exception {

		ByteBuffer buf = ByteBuffer.allocate(1024*1024*4);

		int bytesRead = inputChannel.read(buf);
		byte[] bytes = buf.array();

		byte[] subArray = Arrays.copyOfRange(bytes, 4, bytesToInt(bytes) + 4);
		ByteArrayInputStream bis = new ByteArrayInputStream(subArray);
		ObjectInput in = null;
		in = new ObjectInputStream(bis);
		DirectoryNode node = (DirectoryNode) in.readObject();

		if(!path.endsWith(File.separator)) {
			path += File.separator;
		}
		
		new DirectoryTreeCreater().createTree(node, path);
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
