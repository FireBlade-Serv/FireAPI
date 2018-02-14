package fr.glowstoner.fireapi;

import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;

public interface FireAPI {
	
	org.bukkit.plugin.Plugin getBukkitPlugin();
	
	net.md_5.bungee.api.plugin.Plugin getBungeePlugin();
	
	FireSQL getSQL();
	
	FireRank getRankSystem();
	
	FireChat getChatUtils();
	
	FireAuth getAuthentification();
	
	FireWL getWhiteListSystem();
}