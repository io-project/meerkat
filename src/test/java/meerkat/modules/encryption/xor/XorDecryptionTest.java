package meerkat.modules.encryption.xor;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class XorDecryptionTest {
	MockReadableChannel readChannel = null;
	MockWriteableChannel writeChannel = null;
	XorDecryption xorDecryption = null;
	
	@Before
    public void setUp() throws Exception {
		readChannel = new MockReadableChannel();
		writeChannel = new MockWriteableChannel();
		xorDecryption = new XorDecryption();
		xorDecryption.setInputChannel(readChannel);
		xorDecryption.setOutputChannel(writeChannel);
		char[] password = {'a','b'};
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
		xorDecryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		int bytesRead = writeChannel.getGotLength();
		assertEquals(size, bytesRead);
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
		xorDecryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.reset();
		xorDecryption.run();
		
		/*assertion*/
		int bytesRead = writeChannel.getGotLength();
		assertEquals(size, bytesRead);
		assertArrayEquals(in, writeChannel.getGotMessage());
	}

    @After
    public void tearDown() throws Exception {

    }
}
