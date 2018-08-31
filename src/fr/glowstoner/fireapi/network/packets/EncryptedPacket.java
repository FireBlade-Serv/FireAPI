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

	public Class<?> getPacketClass() {
		return this.klass;
	}
	
	public void addValue(String name, EncryptedObject data) {
		this.data.put(name, data);
	}

	public EncryptedObject getElement(String el) {
		return this.data.get(el);
	}
	
	@Override
	public boolean encrypted() {
		return true;
	}
	
	@Override
	public String toString() {
		return "data: "+this.data.toString()+"/ klass: "+this.klass.getName();
	}
}
