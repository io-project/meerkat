package meerkat.modules.encryption.basic_encryption;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import meerkat.modules.encryption.IEncryptionImplementation;
import meerkat.modules.gui.IDialogBuilderFactory;


public class BasicEncryptionImplementation implements IEncryptionImplementation {
	
	

	@Override
	public boolean prepare(IDialogBuilderFactory dialogBuilderFactory) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends ReadableByteChannel & InterruptibleChannel> void setInputChannel(
			T channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends WritableByteChannel & InterruptibleChannel> void setOutputChannel(
			T channel) {
		// TODO Auto-generated method stub
		
	}

}
