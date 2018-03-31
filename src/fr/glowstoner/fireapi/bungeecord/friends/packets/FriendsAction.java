package fr.glowstoner.fireapi.bungeecord.friends.packets;

import java.io.Serializable;

import fr.glowstoner.fireapi.bungeecord.friends.packets.enums.FriendsActionType;
import fr.glowstoner.fireapi.player.enums.VersionType;

public interface FriendsAction extends Serializable{
	
	public abstract VersionType to();
	
	public abstract FriendsActionType type();
	
	public abstract String ifToSpigotServerName();
}
