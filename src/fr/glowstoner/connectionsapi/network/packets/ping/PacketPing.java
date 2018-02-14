package fr.glowstoner.connectionsapi.network.packets.ping;

import java.io.Serializable;
import java.util.UUID;

import fr.glowstoner.connectionsapi.network.packets.Packet;

public class PacketPing extends Packet implements Serializable{

	private static final long serialVersionUID = -4826374884627726143L;
	
	private String data;
	
	public PacketPing() {
		this.data = UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	public boolean isValid(String data) {
		if(this.data == null) {
			return false;
		}
		
		if(this.data.equals(data)) {
			return true;
		}else {
			return false;
		}
	}
	
	public String getData() {
		return this.data;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
