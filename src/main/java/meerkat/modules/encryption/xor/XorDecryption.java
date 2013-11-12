package meerkat.modules.encryption.xor;

import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import meerkat.modules.encryption.IDecryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;

//TODO testy
public class XorDecryption implements IDecryptionImplementation{

	ReadableByteChannel readChannel;
	WritableByteChannel writeChannel;
	
	@Override
	public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
		//TODO popracować nad tym wyświetlaniem
		return true;
	}

	@Override
	public void run() throws Exception {
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		int read = -1;
		while((read = readChannel.read(readBuffer)) != -1){
			for(int i=0; i<read; i++){
				int b = readBuffer.getInt();
				writeBuffer.putInt(b^i);
			}
			writeChannel.write(writeBuffer);
			readBuffer.flip();
			writeBuffer.flip();
		}
	}

	@Override
	public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
			T channel) {
		readChannel = channel;
	}

	@Override
	public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(
			T channel) {
		writeChannel = channel;
	}
}
