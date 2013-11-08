package meerkat.modules.serialization.simpleSerialization;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

import meerkat.modules.gui.IDialogBuilderFactory;
import meerkat.modules.serialization.ISerializationImplementation;

public class SimpleSerializationImplementation implements
		ISerializationImplementation {
	
	private WritableByteChannel outputChannel;
	private String path;
	private DirectoryTreeBuilder directoryTreeBuilder = new DirectoryTreeBuilder();

	//Uzytwkonik musi wybrac sciezke serializowanego pliku.
	@Override
	public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() throws Exception {
		DirectoryNode directoryNode = directoryTreeBuilder.buildTree(path);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		out = new ObjectOutputStream(bos);
		out.writeObject(directoryNode);
		byte[] directoryTreeBytes = bos.toByteArray();
		
		ByteBuffer buf = ByteBuffer.allocate(directoryTreeBytes.length+100);
		buf.clear();
		buf.putInt(directoryTreeBytes.length);

		buf.put(directoryTreeBytes);

		buf.flip();
		
		while (buf.hasRemaining()) {
			outputChannel.write(buf);
		}
		
		//teraz nalezy wyslac zawartosc plikow
	}

	@Override
	public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(
			T channel) {
		this.outputChannel = channel; 

	}

}
