package fr.glowstoner.fireapi.crypto;

import java.util.List;

public abstract class EncryptFunction<E, D> {

	protected String key;
	
	private EncryptValueType type;
	
	public EncryptFunction(String key, EncryptValueType type) {
		this.key = key;
		this.type = type;
	}
	
	public EncryptedObject getEncryptedObject(E e) throws Exception {
		return new EncryptedObject(this.encrypt(e), this.type);
	}
	
	public abstract D encrypt(E e) throws Exception;
	
	public abstract E decrypt(D d) throws Exception;
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public static EncryptedObject encrypt(String key, Object o) {
		try {
			if(o instanceof Integer) {
				return new EncryptedInt(key).getEncryptedObject((Integer) o);
			}else if(o instanceof String) {
				return new EncryptedString(key).getEncryptedObject((String) o);
			}else if(o instanceof List<?>) {
				return new EncryptedList(key).getEncryptedObject((List<?>) o);
			}else if(o instanceof Enum<?>) {
				return new EncryptedEnum(key).getEncryptedObject((Enum<?>) o);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static Object decrypt(String key, EncryptedObject eo) throws Exception {
		switch (eo.getType()) {
			case ENCRYPT_ENUM:
				return new EncryptedEnum(key).decrypt((String) eo.getData());
			case ENCRYPT_INT:
				return new EncryptedInt(key).decrypt((String) eo.getData());
			case ENCRYPT_LIST:
				return new EncryptedList(key).decrypt((String) eo.getData());
			case ENCRYPT_STRING:
				return new EncryptedString(key).decrypt((String) eo.getData());
			case OTHER:
				return null;
		}	
		return null;
	}
}
