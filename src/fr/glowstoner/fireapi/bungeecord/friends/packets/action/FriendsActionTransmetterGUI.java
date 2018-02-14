package fr.glowstoner.fireapi.bungeecord.friends.packets.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.fireapi.bungeecord.friends.packets.FriendsAction;
import fr.glowstoner.fireapi.bungeecord.friends.packets.enums.FriendsActionType;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class FriendsActionTransmetterGUI implements FriendsAction, Serializable{

	private static final long serialVersionUID = 2941273327484387467L;

	private List<String> friends = new ArrayList<>();
	private List<String> connected = new ArrayList<>();
	
	private String serverDestination, name;
	
	public FriendsActionTransmetterGUI(String name, String dest, List<String> friends, List<String> connected) {
		this.setConnected(connected);
		this.setFriends(friends);
		this.setServerDestination(dest);
		this.setName(name);
	}
	
	public FriendsActionTransmetterGUI() {
		
	}
	
	@Override
	public VersionType to() {
		return VersionType.SPIGOT_VERSION;
	}

	@Override
	public FriendsActionType type() {
		return FriendsActionType.OPEN_FRIENDSLIST_GUI;
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

	@Override
	public String ifToSpigotServerName() {
		return this.getServerDestination();
	}

	public String getServerDestination() {
		return serverDestination;
	}

	public void setServerDestination(String serverDestination) {
		this.serverDestination = serverDestination;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}