package meerkat.modules.encryption.xor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;

public class MockReadableChannel implements ReadableByteChannel, InterruptibleChannel{

	private byte[] send = null;
	int position = 0;
	int end = 0;
	
	public void setSendMessage(byte[] message){
		position = 0;
		send = message;
		end = send.length;
	}
	
	@Override
	public void close() throws IOException {
	}

	@Override
	public boolean isOpen() {
		if(send != null) return true;
		return false;
	}

	@Override
	public int read(ByteBuffer arg0) throws IOException {
		if(position == end) return -1;
		int capacity = arg0.remaining();
		int put = 0;
		for(;position<end && capacity>0;position++, capacity--){
			arg0.put(send[position]);
			put++;
		}
		return put;
	}
	
}
