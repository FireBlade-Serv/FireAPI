package fr.glowstoner.fireapi.gediminas.console.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;

public class PacketExecute extends Packet implements Serializable{

	private static final long serialVersionUID = -5731791793346143581L;

	private String serverCommand;
	
	public PacketExecute(String servercommand) {
		this.serverCommand = servercommand;
	}
	
	public PacketExecute() {
		
	}

	public String getServerCommand() {
		return serverCommand;
	}
	
	@Override
	public boolean isCrypted() {
		return false;
	}
}
