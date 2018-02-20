package fr.glowstoner.fireapi;

import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.friends.FireFriends;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;

public interface FireAPI {
	
	//bungeecord
	net.md_5.bungee.api.plugin.Plugin getBungeePlugin();
	FireAuth getAuthentification();
	FireWL getWhiteListSystem();
	FireFriends getFriends();
	
	//bukkit/spigot
	org.bukkit.plugin.Plugin getBukkitPlugin();
	String id();
	
	//share
	Client getClient();
	void setClient(Client c);
	FireSQL getSQL();
	FireRank getRankSystem();
	FireChat getChatUtils();
	VersionType type();
}