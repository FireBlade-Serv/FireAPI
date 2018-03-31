package fr.glowstoner.connectionsapi.network.packets;

import java.io.Serializable;

public class PacketText extends Packet implements Serializable{

	private static final long serialVersionUID = 2207269263350999959L;
	
	private String text;
	
	public PacketText(String text) {
		this.text = text;
	}
	
	public String getMessage() {
		return this.text;
	}
	
	public void writeMessage(String text) {
		this.text = text;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
	
}
