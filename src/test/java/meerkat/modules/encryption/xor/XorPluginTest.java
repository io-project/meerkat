package meerkat.modules.encryption.xor;

import static org.junit.Assert.*;

import java.util.Random;

import meerkat.modules.encryption.xor.MockReadableChannel;
import meerkat.modules.encryption.xor.MockWriteableChannel;
import meerkat.modules.encryption.xor.XorEncryption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XorPluginTest {
    
	MockReadableChannel readChannel = null;
	MockWriteableChannel writeChannel = null;
	XorEncryption xorEncryption = null;
	XorDecryption xorDecryption = null;
	
	@Before
    public void setUp() throws Exception {
		readChannel = new MockReadableChannel();
		writeChannel = new MockWriteableChannel();
		xorEncryption = new XorEncryption();
		xorEncryption.setInputChannel(readChannel);
		xorEncryption.setOutputChannel(writeChannel);
		char[] password = {'a','b'};
		xorEncryption.getXorAddition().makeHashArrayFromPassword(password);
		xorDecryption = new XorDecryption();
		xorDecryption.setInputChannel(readChannel);
		xorDecryption.setOutputChannel(writeChannel);
		xorDecryption.getXorAddition().makeHashArrayFromPassword(password);
    }
    
	@Test
	public void checkSimpleXorEncoding() throws Exception{
		/*preprare*/
		int size = 64;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(size);
		writeChannel.reset();
		
		/*action*/
		readChannel.setSendMessage(in);
		xorEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
	
	@Test
	public void checkMultiBufferXorEncoding() throws Exception{
		/*preprare*/
		int size = 1500;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(size);
		writeChannel.reset();
		
		/*action*/
		readChannel.setSendMessage(in);
		xorEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}

    @After
    public void tearDown() throws Exception {

    }
}
