package fr.glowstoner.fireapi.crypto;

public class EncryptedString extends EncryptFunction<String, String>{

	public EncryptedString(String key) {
		super(key, EncryptValueType.ENCRYPT_STRING);
	}

	@Override
	public String encrypt(String e) throws Exception {
		return new FireEncrypt(this.key).encrypt(e);
	}

	@Override
	public String decrypt(String d) throws Exception {
		return new FireEncrypt(this.key).decrypt(d);
	}
}
