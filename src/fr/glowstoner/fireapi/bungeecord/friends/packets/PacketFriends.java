package fr.glowstoner.fireapi.bungeecord.friends.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import lombok.Getter;
import lombok.Setter;

public class PacketFriends extends Packet implements Serializable{

	private static final long serialVersionUID = 7080980497669673061L;
	
	@Getter @Setter private FriendsAction action;
	
	public PacketFriends(FriendsAction action) {
		this.setAction(action);
	}
	
	public PacketFriends() {
		
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
