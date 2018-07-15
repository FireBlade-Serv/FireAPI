package fr.glowstoner.fireapi.crypto;

import lombok.Data;

@Data
public class EncryptionKey {

	private String key;
	
	public EncryptionKey(String key) {
		this.setKey(key);
	}
}
