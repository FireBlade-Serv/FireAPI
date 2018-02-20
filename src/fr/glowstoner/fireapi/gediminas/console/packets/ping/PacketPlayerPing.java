package fr.glowstoner.fireapi.gediminas.console.packets.ping;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.enums.PingState;
import lombok.Getter;
import lombok.Setter;

public class PacketPlayerPing extends Packet implements Serializable{
	
	private static final long serialVersionUID = 5070770223906428929L;

	@Getter @Setter private PingState state;
	@Getter @Setter private String name;
	
	public PacketPlayerPing(String name, PingState type) {
		this.setState(type);
		this.setName(name);
	}
	
	public PacketPlayerPing() {
		
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
