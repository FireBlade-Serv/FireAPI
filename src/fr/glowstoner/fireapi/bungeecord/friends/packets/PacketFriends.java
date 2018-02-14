package fr.glowstoner.fireapi.bungeecord.friends.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;

public class PacketFriends extends Packet implements Serializable{

	private static final long serialVersionUID = 7080980497669673061L;
	
	private FriendsAction action;
	
	public PacketFriends(FriendsAction action) {
		this.setAction(action);
	}
	
	public PacketFriends() {
		
	}

	public FriendsAction getAction() {
		return this.action;
	}

	public void setAction(FriendsAction action) {
		this.action = action;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
