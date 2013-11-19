package meerkat.modules.encryption.xor;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.codec.digest.DigestUtils;

public class XorAddition {
	
	private byte[] hashCode = null;
	
	public byte[] getHashCodeArray(){
		return hashCode;
	}
	
	/**
	 * This method make password coded by sha512. 
	 * @param byte[] that represent password
	 * @return byte[] that represent this password as hash sha512
	 */
	public static byte[] makeByteArrayFromPassword(byte[] password){
		return  DigestUtils.sha512( password );
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
	
	public boolean makeHashArrayFromPassword(char[] password){
		try{
			byte[] encodedPassword = makePasswordEncodeCharToByteArray(password);
			hashCode = makeByteArrayFromPassword(encodedPassword);
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
