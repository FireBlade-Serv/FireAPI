package fr.glowstoner.fireapi.bigbrother.console.login;

import java.io.Serializable;

public class BigBrotherLogin implements Serializable{
	
	private static final long serialVersionUID = -2331272106486048631L;
	
	private String key, password;
	
	public BigBrotherLogin(String key, String pass) {
		this.setKey(key);
		this.setPassword(pass);
	}
	
	public BigBrotherLogin() {
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
