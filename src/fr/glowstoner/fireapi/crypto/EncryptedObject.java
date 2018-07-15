package fr.glowstoner.fireapi.crypto;

import java.io.Serializable;

public class EncryptedObject implements Serializable{
	
	private static final long serialVersionUID = -209277207568679736L;
	
	private Object data;
	private EncryptValueType type;
	
	public EncryptedObject(Object data, EncryptValueType type) {
		this.setData(data);
		this.setType(type);
	}
	
	public EncryptedObject() {
		
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public EncryptValueType getType() {
		return type;
	}

	public void setType(EncryptValueType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "data: "+this.data.toString()+", type: "+this.type.name();
	}
}
