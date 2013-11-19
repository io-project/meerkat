package meerkat.modules.encryption.des;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class DesAddition {

	private byte[] hashCode = null;

	public byte[] getHashCodeArray() {
		return hashCode;
	}

	/**
	 * This method make password coded by sha512.
	 * 
	 * @param byte[] that represent password
	 * @return SecretKey that represent this password as Key
	 * @throws InvalidKeyException 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException 
	 */
	
	public static SecretKey makeKeyFromPassword(byte[] password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		DESKeySpec dks = new DESKeySpec(password);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		return skf.generateSecret(dks);
	}

}
