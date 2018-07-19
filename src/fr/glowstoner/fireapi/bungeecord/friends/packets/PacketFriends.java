package fr.glowstoner.fireapi.bungeecord.friends.packets;

import java.util.List;

import fr.glowstoner.fireapi.bungeecord.friends.packets.enums.FriendsActionType;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class PacketFriends extends Packet implements Encryptable{

	private static final long serialVersionUID = 7080980497669673061L;
	
	private FriendsActionType actionType;
	private VersionType to;
	private List<String> friends, connected;
	private String destination, name;
	
	public PacketFriends(FriendsActionType actionType, VersionType to, List<String> friends,
			List<String> connected, String destination, String name) {
		
		this.setActionType(actionType);
		this.setConnected(connected);
		this.setDestination(destination);
		this.setFriends(friends);
		this.setName(name);
		this.setTo(to);
	}
	
	public PacketFriends() {
		
	}

	public FriendsActionType getActionType() {
		return actionType;
	}

	public void setActionType(FriendsActionType actionType) {
		this.actionType = actionType;
	}

	public VersionType getTo() {
		return to;
	}

	public void setTo(VersionType to) {
		this.to = to;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public List<String> getConnected() {
		return connected;
	}

	public void setConnected(List<String> connected) {
		this.connected = connected;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"actionType", "to", "friends", "connected", "destination", "name"};
	}

	@Override
	public boolean isCrypted() {
		return true;
	}
}
