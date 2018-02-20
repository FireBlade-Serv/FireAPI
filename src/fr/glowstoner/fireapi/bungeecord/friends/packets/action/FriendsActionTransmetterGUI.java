package fr.glowstoner.fireapi.bungeecord.friends.packets.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.fireapi.bungeecord.friends.packets.FriendsAction;
import fr.glowstoner.fireapi.bungeecord.friends.packets.enums.FriendsActionType;
import fr.glowstoner.fireapi.player.enums.VersionType;
import lombok.Getter;
import lombok.Setter;

public class FriendsActionTransmetterGUI implements FriendsAction, Serializable{

	private static final long serialVersionUID = 2941273327484387467L;

	@Getter @Setter private List<String> friends = new ArrayList<>();
	@Getter @Setter private List<String> connected = new ArrayList<>();
	
	@Getter @Setter private String serverDestination, name;
	
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

	@Override
	public String ifToSpigotServerName() {
		return this.getServerDestination();
	}
}