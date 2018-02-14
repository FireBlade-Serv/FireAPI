package fr.glowstoner.connectionsapi.network.packets.login;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.crypt.FireCrypt;

public class PacketLogin extends Packet implements Serializable{

	private static final long serialVersionUID = -7078807062323289889L;
	
	private FireCrypt crypt = new FireCrypt();
	private String cryptpass;
	
	public PacketLogin(String key, String clearpass) throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		
		this.crypt.setKey(key);
		this.cryptpass = this.crypt.encrypt(clearpass);
	}
	
	public PacketLogin(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		this.crypt.setKey(key);
	}
	
	public void setCryptPassword(String clearpass) throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		
		this.cryptpass = this.crypt.encrypt(clearpass);
	}

	public String decryptPass(String key) throws InvalidKeyException, NoSuchAlgorithmException,
		NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		return this.crypt.decrypt(this.cryptpass);
	}
	
	@Override
	public boolean isCrypted() {
		return true;
	}

}
