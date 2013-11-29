package meerkat.modules.encryption.des;

import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import meerkat.modules.IncorrectPasswordException;
import meerkat.modules.encryption.xor.MockReadableChannel;
import meerkat.modules.encryption.xor.MockWriteableChannel;

import org.junit.Before;
import org.junit.Test;

public class DesPluginTest {
	MockReadableChannel readChannel = null;
	MockWriteableChannel writeChannel = null;
	DesEncryption desEncryption = null;
	DesDecryption desDecryption = null;

	@Before
	public void setUp() throws Exception {
		readChannel = new MockReadableChannel();
		writeChannel = new MockWriteableChannel();
		desEncryption = new DesEncryption();
		desEncryption.setInputChannel(readChannel);
		desEncryption.setOutputChannel(writeChannel);
		desDecryption = new DesDecryption();
		desDecryption.setInputChannel(readChannel);
		desDecryption.setOutputChannel(writeChannel);
		char[] password = { 'a', 'a', 'b' };
		desEncryption.getDesAddition().makeSecretKeyFromPassword(password);
		desDecryption.getDesAddition().makeSecretKeyFromPassword(password);
	}

	@Test
	public void checkSimpleDesEncoding() throws Exception {
		/* preprare */
		int size = 64;
		int out_size = 72;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(out_size);
		writeChannel.reset();

		/* action */
		readChannel.setSendMessage(in);
		desEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(size);
		writeChannel.reset();
		desDecryption.run();

		/* assertion */
		assertArrayEquals(in, writeChannel.getGotMessage());
	}

	@Test
	public void checkLongerDesEncoding() throws Exception {
		/* preprare */
		int size = 1500;
		int out_size = 1512;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(out_size);
		writeChannel.reset();

		/* action */
		readChannel.setSendMessage(in);
		desEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(size);
		writeChannel.reset();
		desDecryption.run();

		/* assertion */
		assertArrayEquals(in, writeChannel.getGotMessage());
	}

	@Test(expected = IncorrectPasswordException.class)
	public void checkIncorrectPassword() throws Exception {
		/* preprare */
		int size = 1500;
		int out_size = 1512;
		byte[] in = new byte[size];
		new Random().nextBytes(in);
		writeChannel.setSize(out_size);
		writeChannel.reset();
		char[] inPassword = { 'a', 'a', 'a' };
		desDecryption.getDesAddition().makeSecretKeyFromPassword(inPassword);

		/* action */
		readChannel.setSendMessage(in);
		desEncryption.run();
		readChannel.setSendMessage(writeChannel.getGotMessage());
		writeChannel.setSize(size);
		writeChannel.reset();
		desDecryption.run();

		/* assertion */
		assertArrayEquals(in, writeChannel.getGotMessage());
	}
}
