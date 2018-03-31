package fr.glowstoner.fireapi.bigbrother.console.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.player.enums.VersionType;
import lombok.Getter;
import lombok.Setter;

public class PacketVersion extends Packet implements Serializable{

	private static final long serialVersionUID = 4715009865236006640L;

	@Getter @Setter private VersionType type;
	
	public PacketVersion(VersionType type) {
		this.setType(type);
	}
	
	public PacketVersion() {
		
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
	
}
