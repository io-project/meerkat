package meerkat.modules.encryption.aes;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.crypto.spec.SecretKeySpec;

public class AesAddition {
	
	private SecretKeySpec secretKey = null;
	
	public SecretKeySpec getSecretKey(){
		return secretKey;
	}
	
	/**
	 * This method make secretKry from password 
	 * @param byte[] that represent password
	 * @return SecretKeySpec for this password
	 */
	public static SecretKeySpec makeSecretKeyFromPassword(byte[] password){
		return new SecretKeySpec(password, "AES");
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
	
	public boolean makeSecretKeyFromPassword(char[] password){
		byte[] encodedPassword;
		try {
			encodedPassword = makePasswordEncodeCharToByteArray(password);
			secretKey = makeSecretKeyFromPassword(encodedPassword);
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
