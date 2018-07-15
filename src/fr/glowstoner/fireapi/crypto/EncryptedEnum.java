package fr.glowstoner.fireapi.crypto;

import java.lang.reflect.Type;

public class EncryptedEnum extends EncryptFunction<Enum<?>, String>{

	public EncryptedEnum(String key) {
		super(key, EncryptValueType.ENCRYPT_ENUM);
	}

	@Override
	public String encrypt(Enum<?> e) throws Exception {
		return "*"+e.getClass().getName()+"@"+new FireEncrypt(super.key).encrypt(e.name());
	}

	@Override
	public Enum<?> decrypt(String d) throws Exception {
		Class<?> clazz = null;
		String value = null;
		
		for(String part : d.split("@")) {
			if(part.startsWith("*")) {
				clazz = (Class<?>) Class.forName(part.substring(1));
			}else {
				value = new FireEncrypt(super.key).decrypt(part);
			}
		}
		
		return this.createInstance(clazz, value);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Enum<T>> T createInstance(Type type, String value){
		return Enum.valueOf((Class<T>) type, value);
	}
}
