package fr.glowstoner.fireapi.gediminas.console.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class PacketVersion extends Packet implements Serializable{

	private static final long serialVersionUID = 4715009865236006640L;

	private VersionType type;
	
	public PacketVersion(VersionType type) {
		this.setType(type);
	}
	
	public PacketVersion() {
		
	}

	public VersionType getType() {
		return type;
	}

	public void setType(VersionType type) {
		this.type = type;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
	
}
