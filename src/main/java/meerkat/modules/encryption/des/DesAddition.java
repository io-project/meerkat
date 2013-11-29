package meerkat.modules.encryption.des;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class DesAddition {

private SecretKey secretKey = null;
	
	public SecretKey getSecretKey(){
		return secretKey;
	}
	
	/**
	 * This method make secretKry from password 
	 * @param byte[] that represent password
	 * @return SecretKeySpec for this password
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws CharacterCodingException 
	 */
	public boolean makeSecretKeyFromPassword(char[] password) throws InvalidKeySpecException, NoSuchAlgorithmException, CharacterCodingException{
		
		byte[] key = DigestUtils.sha512(makePasswordEncodeCharToByteArray(password));
		key = Arrays.copyOf(key, 24);
		secretKey = new SecretKeySpec(key, "DESede");
		return true;
	}
	
	/**
	 * This method encode char[] password to byte[] password
	 * @param char[] that represent password obtain from IDialog
	 * @return byte[] that represent this password encoded to byte array
	 */
	public static byte[] makePasswordEncodeCharToByteArray(char[] password) throws CharacterCodingException{
		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();
		CharBuffer charBuffer = CharBuffer.wrap(password);
		return encoder.encode(charBuffer).array();
	}

}
