package meerkat.modules.encryption.xor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.WritableByteChannel;

public class MockWriteableChannel implements WritableByteChannel, InterruptibleChannel{

	private byte[] got = null;
	private int position = 0;
	private int size = 0;
	
	public byte[] getGotMessage(){
		return got;
	}
	
	public int getGotLength(){
		return got.length;
	}
	
	public void setSize(int s){
		size = s;
		got = new byte[size];
	}
	
	public void reset(){
		position = 0;
	}
	
	@Override
	public void close() throws IOException {
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		System.arraycopy(src.array(), 0, got, position, src.position());
		position += src.position();
		return src.position();
	}

}