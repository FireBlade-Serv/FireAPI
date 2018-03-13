package fr.glowstoner.connectionsapi.network.packets;

import java.io.Serializable;

public class PacketInfo extends Packet implements Serializable{

	private static final long serialVersionUID = 7606453362116235792L;

	public PacketInfo() {
		
	}
	
	@Override
	public boolean isCrypted() {
		return false;
	}

}
