package fr.glowstoner.fireapi;

import fr.glowstoner.fireapi.bigbrother.console.check.BigBrotherConnectionCheck;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FireInjector;
import fr.glowstoner.fireapi.bukkit.tag.FireTag;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.friends.FireFriends;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;

public interface FireAPI {
	
	public static final String VERSION = "4.0.1 (Branche Alpha)";
	
	//bungeecord
	net.md_5.bungee.api.plugin.Plugin getBungeePlugin();
	
	FireAuth getAuthentification();
	
	FireWL getWhitelistSystem();
	
	FireFriends getFriends();
	
	
	//bukkit/spigot
	org.bukkit.plugin.Plugin getBukkitPlugin();
	
	FireInjector getPacketInjector();
	
	FireTag getTagSystem();
	
	
	//share
	Client getClient();
	
	void setClient(Client c);
	
	BigBrotherConnectionCheck getChecker();
	
	void setChecker(BigBrotherConnectionCheck checker);
	
	FireSQL getSQL();
	
	FireRank getRankSystem();
	
	FireChat getChatUtils();
	
	String id();
	
	VersionType type();
	
	EncryptionKey encryptionKey();
}