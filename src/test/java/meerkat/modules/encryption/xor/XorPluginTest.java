package meerkat.modules.encryption.xor;

import static org.junit.Assert.*;

import java.util.Random;

import meerkat.modules.IncorrectPasswordException;
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
		int read_size = 64;
		int write_size = 128;
		byte[] in = new byte[read_size];
		new Random().nextBytes(in);
		writeChannel.setSize(write_size);
		writeChannel.reset();
		
		/*action*/
		readChannel.setSendMessage(in);
		xorEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(read_size);
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
	
	@Test
	public void checkMultiBufferXorEncoding() throws Exception{
		/*preprare*/
		int read_size = 1500;
		int write_size = 1564;
		byte[] in = new byte[read_size];
		new Random().nextBytes(in);
		writeChannel.setSize(write_size);
		writeChannel.reset();
		
		/*action*/
		readChannel.setSendMessage(in);
		xorEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(read_size);
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
	
	@Test(expected = IncorrectPasswordException.class)
	public void incorrectPasswordCheck() throws Exception{
		/*preprare*/
		int read_size = 1500;
		int write_size = 1564;
		byte[] in = new byte[read_size];
		new Random().nextBytes(in);
		writeChannel.setSize(write_size);
		writeChannel.reset();
		char[] inPassword = {'a', 'a'};
		xorDecryption.getXorAddition().makeHashArrayFromPassword(inPassword);
		
		
		/*action*/
		readChannel.setSendMessage(in);
		xorEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(read_size);
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}

    @After
    public void tearDown() throws Exception {

    }
}
