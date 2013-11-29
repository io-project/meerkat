package meerkat.modules.encryption.aes;

import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import meerkat.modules.IncorrectPasswordException;
import meerkat.modules.encryption.xor.MockReadableChannel;
import meerkat.modules.encryption.xor.MockWriteableChannel;

import org.junit.Before;
import org.junit.Test;

public class AesPluginTest {
	MockReadableChannel readChannel = null;
	MockWriteableChannel writeChannel = null;
	AesEncryption aesEncryption = null;
	AesDecryption aesDecryption = null;
	
	@Before
    public void setUp() throws Exception {
		readChannel = new MockReadableChannel();
		writeChannel = new MockWriteableChannel();
		aesEncryption = new AesEncryption();
		aesEncryption.setInputChannel(readChannel);
		aesEncryption.setOutputChannel(writeChannel);
		aesDecryption = new AesDecryption();
		aesDecryption.setInputChannel(readChannel);
		aesDecryption.setOutputChannel(writeChannel);
		char[] password = {'a','b','a'};
		aesEncryption.getAesAddition().makeSecretKeyFromPassword(password);
		aesDecryption.getAesAddition().makeSecretKeyFromPassword(password);
    }
	
	@Test
	public void checkSimpleAesEncoding() throws Exception{
		/*preprare*/
		int size = 64;
		int out_size = 80;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(out_size);
		writeChannel.reset();
		
		/*action*/
		readChannel.setSendMessage(in);
		aesEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(size);
		writeChannel.reset();
		aesDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
	
	@Test
	public void checkLongerAesEncoding() throws Exception{
		/*preprare*/
		int size = 1500;
		int out_size = 1520;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(out_size);
		writeChannel.reset();
		
		/*action*/
		readChannel.setSendMessage(in);
		aesEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(size);
		writeChannel.reset();
		aesDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
	
	@Test(expected = IncorrectPasswordException.class)
	public void checkIncorrectPassword() throws Exception{
		/*preprare*/
		int size = 1500;
		int out_size = 1520;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(out_size);
		writeChannel.reset();
		char[] inPassword = {'a','a','a'};
		aesDecryption.getAesAddition().makeSecretKeyFromPassword(inPassword);
		
		/*action*/
		readChannel.setSendMessage(in);
		aesEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(size);
		writeChannel.reset();
		aesDecryption.run();
		
		/*assertion*/
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
}
