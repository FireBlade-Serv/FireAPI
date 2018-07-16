package fr.glowstoner.fireapi.crypto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EncryptionKey implements Serializable{

	private static final long serialVersionUID = -8185224456640740822L;
	
	private String key;
	
	public EncryptionKey(String key) {
		this.setKey(key);
	}
}
