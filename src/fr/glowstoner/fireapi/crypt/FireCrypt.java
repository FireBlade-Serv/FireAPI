package fr.glowstoner.fireapi.crypt;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class FireCrypt implements Serializable{

	private static final long serialVersionUID = 6818253746128838534L;
	
	private SecretKeySpec key;
	private byte[] bkey;
	
	public FireCrypt() {
		
	}
	
	public void setKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] sha = MessageDigest.getInstance("SHA-1").digest(key.getBytes("UTF-8"));
		byte[] sha16 = Arrays.copyOf(sha, 16);
		
		this.bkey = sha16;
		this.key = new SecretKeySpec(sha16, "AES");
	}
	
	public String encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException,
		IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {
		
		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		
		c.init(Cipher.ENCRYPT_MODE, this.key);
		
		return Base64.getEncoder().encodeToString(c.doFinal(text.getBytes("UTF-8")));
	}
	
	public String decrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		
		c.init(Cipher.DECRYPT_MODE, this.key);
		
		String en = null;
		
		try {
			en = new String(c.doFinal(Base64.getDecoder().decode(text)));
		}catch (Exception ex) {
			en = new String(c.update(Base64.getDecoder().decode(text)));
		}
		
		return en;
	}
	
	public byte[] getByteKey() {
		return this.bkey;
	}
}