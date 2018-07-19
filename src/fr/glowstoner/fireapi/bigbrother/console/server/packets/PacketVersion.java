package fr.glowstoner.fireapi.bigbrother.console.server.packets;

import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class PacketVersion extends Packet implements Encryptable{

	private static final long serialVersionUID = 4715009865236006640L;

	private VersionType version;
	
	public PacketVersion(VersionType version) {
		this.setVersion(version);
	}
	
	public PacketVersion() {
		
	}

	public VersionType getVersion() {
		return version;
	}

	public void setVersion(VersionType version) {
		this.version = version;
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"version"};
	}
	
	@Override
	public boolean isCrypted() {
		return true;
	}
	
}
