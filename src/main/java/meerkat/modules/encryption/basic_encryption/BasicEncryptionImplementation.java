package meerkat.modules.encryption.basic_encryption;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;


public class BasicEncryptionImplementation implements IEncryptionImplementation {
	
	ReadableByteChannel readChannel;
	WritableByteChannel writeChannel;
	byte[] code = new byte[1024];
	
	@Override
	public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
		for(int i=0; i<1024; i++)
			code[i] = (byte)255;
		return true;
	}

	@Override
	public void run() throws Exception {
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		byte[] readByte;
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		byte[] writeByte = new byte[1024];
		int read = -1;
		while((read = readChannel.read(readBuffer)) != -1){
			readByte = readBuffer.array();
			for(int i=0; i<read; i++){
				writeByte[i] = (byte) (readByte[i] ^ code[i]);
			}
			writeBuffer.get(writeByte);
			writeChannel.write(writeBuffer);
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
