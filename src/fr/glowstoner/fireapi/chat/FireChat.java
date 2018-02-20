package fr.glowstoner.fireapi.chat;

import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FireChat {
	
	private FireRank rank;
	private FireAPI api;
	
	public FireChat(FireAPI api) {
		this.api = api;
		this.rank = this.api.getRankSystem();
	}
	
	public String getStringRank(String name){
		if(this.rank.getPlayerRank(name).equals(Rank.BUILDER)){
			return "§6§lBuilder";
		}else if(this.rank.getPlayerRank(name).equals(Rank.FIRE)){
			return "§e§lFire";
		}else if(this.rank.getPlayerRank(name).equals(Rank.ULTRA)){
			return "§a§lUltra";
		}else if(this.rank.getPlayerRank(name).equals(Rank.ULTIMATE)){
			return "§5§lUltimate";
		}else if(this.rank.getPlayerRank(name).equals(Rank.YOUTUBER)){
			return "§2§lYoutuber";
		}else if(this.rank.getPlayerRank(name).equals(Rank.GUIDE)){
			return "§2§lGuide";
		}else if(this.rank.getPlayerRank(name).equals(Rank.ASSISTANT)){
			return "§6§lAssistant";
		}else if(this.rank.getPlayerRank(name).equals(Rank.MODÉRATEUR)){
			return "§3§lModérateur";
		}else if(this.rank.getPlayerRank(name).equals(Rank.STAFF_MANAGER)){
			return "§c§lStaff Manager";
		}else if(this.rank.getPlayerRank(name).equals(Rank.MANAGER_IG)){
			return "§c§lManager IG";
		}else if(this.rank.getPlayerRank(name).equals(Rank.DÉVELOPPEUR)){
			return "§9§lDev";
		}else if(this.rank.getPlayerRank(name).equals(Rank.ADMINISTRATEUR)){
			return "§4§lAdmin";
		}else if(this.rank.getPlayerRank(name).equals(Rank.RESPONSABLE_COM)){
			return "§6§lResp. Com.";
		}else{
			return "§7Membre";
		}
	}
	
	public ChatColor getRankColor(String name){
		return this.rank.getPlayerRank(name).getColor();
	}
	
	public String getPrefix(Player p) {
		Rank rank = this.rank.getPlayerRank(p.getName());
		
		switch (rank) {
			case ADMINISTRATEUR:
				return "§4[Admin] ";
			case DÉVELOPPEUR:
				return "§9[Dev] ";
			case ASSISTANT:
				return "§6[Assistant] ";
			case BUILDER:
				return "§6[Builder] ";
			case RESPONSABLE_COM:
				return "§6[Resp.Com] ";
			case MANAGER_IG:
				return "§c[Man.IG] ";
			case STAFF_MANAGER:
				return "§c[Staff.Man] ";
			case YOUTUBER:
				return "§2[Youtuber] ";
			case GUIDE:
				return "§2[Guide] ";
			case MODÉRATEUR:
				return "§3[Mod] ";
			case ULTIMATE:
				return "§5[Ultimate] ";
			case ULTRA:
				return "§2[Ultra] ";
			case FIRE:
				return "§e[Fire] ";
			default:
				return "§7[Membre] ";
		}
	}
	
	public String getPrefixByRank(Rank rank) {
		switch (rank) {
			case ADMINISTRATEUR:
				return "§4[Admin] ";
			case DÉVELOPPEUR:
				return "§9[Dev] ";
			case ASSISTANT:
				return "§6[Assistant] ";
			case BUILDER:
				return "§6[Builder] ";
			case RESPONSABLE_COM:
				return "§6[Resp.Com] ";
			case MANAGER_IG:
				return "§c[Man.IG] ";
			case STAFF_MANAGER:
				return "§c[Staff.Man] ";
			case YOUTUBER:
				return "§2[Youtuber] ";
			case GUIDE:
				return "§2[Guide] ";
			case MODÉRATEUR:
				return "§3[Mod] ";
			case ULTIMATE:
				return "§5[Ultimate] ";
			case ULTRA:
				return "§2[Ultra] ";
			case FIRE:
				return "§e[Fire] ";
			default:
				return "§7[Membre] ";
	}
	}

	public void sendMessageToGroup(String msg, VersionType type, Rank rank) {
		switch (type) {
			case BUNGEECORD_VERSION:
				for(ProxiedPlayer pps : this.api.getBungeePlugin().getProxy().getPlayers()) {
					FirePlayer fp = new FirePlayer(pps, this.api);
					
					if(fp.hasRankAndSup(rank)) {
						fp.sendMessage(msg);
					}
				}
			
				break;
			case SPIGOT_VERSION:
				for(Player ps : this.api.getBukkitPlugin().getServer().getOnlinePlayers()) {
					FirePlayer fp = new FirePlayer(ps, this.api);
					
					if(fp.hasRankAndSup(rank)) {
						fp.sendMessage(msg);
					}
				}
				
				break;
			default:
				break;
		}
	}
}
