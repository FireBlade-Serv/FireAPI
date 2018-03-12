package fr.glowstoner.fireapi.bigbrother.console.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import lombok.Getter;
import lombok.Setter;

public class PacketExecute extends Packet implements Serializable{

	private static final long serialVersionUID = -5731791793346143581L;

	@Getter @Setter private String serverCommand;
	
	public PacketExecute(String servercommand) {
		this.serverCommand = servercommand;
	}
	
	public PacketExecute() {
		
	}
	
	@Override
	public boolean isCrypted() {
		return false;
	}
}
