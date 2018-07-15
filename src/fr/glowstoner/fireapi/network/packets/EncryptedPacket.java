package fr.glowstoner.fireapi.network.packets;

import java.util.HashMap;
import java.util.Map;

import fr.glowstoner.fireapi.crypto.EncryptedObject;

public class EncryptedPacket extends Packet{

	private static final long serialVersionUID = -1568286924486565709L;
	
	private Map<String, EncryptedObject> data = new HashMap<>();
	private Class<?> klass;
	
	public EncryptedPacket(Class<?> klass) {
		this.klass = klass;
	}

	public Map<String, EncryptedObject> getData() {
		return data;
	}

	public void setData(Map<String, EncryptedObject> data) {
		this.data = data;
	}
	
	public void addValue(String name, EncryptedObject data) {
		this.data.put(name, data);
	}
	
	public EncryptedObject getElement(String name) {
		return this.data.get(name);
	}

	public Class<?> getPacketClass() {
		return this.klass;
	}
	
	@Override
	public boolean isCrypted() {
		return true;
	}
	
	@Override
	public String toString() {
		return this.data.toString()+"/"+this.klass.getName();
	}
}
