package fr.glowstoner.connectionsapi.network.packets;

import java.io.Serializable;

public class PacketPing extends Packet implements Serializable{

	private static final long serialVersionUID = -4826374884627726143L;
	
	//test packet for check connection
	public PacketPing() {
		
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
