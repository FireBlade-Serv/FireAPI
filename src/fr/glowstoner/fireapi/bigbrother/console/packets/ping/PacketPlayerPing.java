package fr.glowstoner.fireapi.bigbrother.console.packets.ping;

import fr.glowstoner.fireapi.bigbrother.console.packets.ping.enums.PingState;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;

public class PacketPlayerPing extends Packet implements Encryptable{
	
	private static final long serialVersionUID = 5070770223906428929L;

	private PingState state;
	private String name;
	
	public PacketPlayerPing(String name, PingState type) {
		this.setState(type);
		this.setName(name);
	}
	
	public PacketPlayerPing() {
		
	}

	public PingState getState() {
		return state;
	}

	public void setState(PingState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String[] encryptedFields() {
		return new String[] {"state", "name"};
	}

	@Override
	public boolean isCrypted() {
		return true;
	}
}
