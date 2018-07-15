package fr.glowstoner.fireapi.crypto;

public class EncryptedInt extends EncryptFunction<Integer, String>{

	public EncryptedInt(String key) {
		super(key, EncryptValueType.ENCRYPT_INT);
	}

	@Override
	public String encrypt(Integer e) throws Exception {
		return new FireEncrypt(super.key).encrypt(""+e);
	}

	@Override
	public Integer decrypt(String d) throws Exception {
		return Integer.valueOf(new FireEncrypt(super.key).decrypt(d));
	}
}
