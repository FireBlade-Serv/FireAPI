package fr.glowstoner.fireapi.bigbrother.console.server.login;

import java.io.Serializable;

import fr.glowstoner.fireapi.crypto.EncryptionKey;

public class BigBrotherLogin implements Serializable{
	
	private static final long serialVersionUID = -2331272106486048631L;
	
	private EncryptionKey key;
	private String password;
	
	public BigBrotherLogin(EncryptionKey key, String password) {
		this.setKey(key);
		this.setPassword(password);
	}
	
	public BigBrotherLogin() {
		
	}

	public EncryptionKey getKey() {
		return key;
	}

	public void setKey(EncryptionKey key) {
		this.key = key;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
