package fr.glowstoner.fireapi.gediminas.console.packets.ping;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.enums.PingState;

public class PacketPlayerPing extends Packet implements Serializable{
	
	private static final long serialVersionUID = 5070770223906428929L;

	private PingState state;
	private String name;
	
	public PacketPlayerPing(String name, PingState type) {
		this.setState(type);
		this.setName(name);
	}
	
	public PacketPlayerPing() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PingState getState() {
		return state;
	}

	public void setState(PingState state) {
		this.state = state;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
