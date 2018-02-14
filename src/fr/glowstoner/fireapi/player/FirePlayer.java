package fr.glowstoner.fireapi.player;

import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FirePlayer {
	
	private ProxiedPlayer bungeep;
	private VersionType type;
	private Player bukkitp;
	private FireAPI api;
	private String pname;
	
	public FirePlayer(ProxiedPlayer pp, FireAPI api) {
		this.bungeep = pp;
		this.api = api;
		this.type = VersionType.BUNGEECORD_VERSION;
		this.pname = this.bungeep.getName();
	}
	
	public FirePlayer(Player p, FireAPI api) {
		this.bukkitp = p;
		this.api = api;
		this.type = VersionType.SPIGOT_VERSION;
		this.pname = this.bukkitp.getName();
	}
	
	public boolean hasRank(Rank rank) {
		return this.api.getRankSystem().getPlayerRank(this.pname).
				equals(rank);
	}
	
	public boolean hasRankAndSup(Rank rank) {
		return this.api.getRankSystem().getListRankAndSup(rank).
				contains(this.api.getRankSystem().getPlayerRank(this.pname));
	}
	
	public VersionType getPlayerVersionType() {
		return this.type;
	}
	
	public String getName() {
		return this.pname;
	}
	
	public void sendMessage(String msg) {
		switch (this.type) {
			case BUNGEECORD_VERSION:
				this.bungeep.sendMessage(new TextComponent(msg));
				
				break;

			case SPIGOT_VERSION:
				this.bukkitp.sendMessage(msg);
				
				break;
			default:
			try {
				throw new InvalidVersionTypeException
					("Le VersionType spécifié ne peut pas être appliqué sur ce FirePlayer !");
			} catch (InvalidVersionTypeException e) {
				e.printStackTrace();
			}
				
				break;
		}
	}
	
	public Player getBukkitPlayer() {
		return this.bukkitp;
	}
	
	public ProxiedPlayer getBungeePlayer() {
		return this.bungeep;
	}
	
	public boolean isInStaff() {
		return this.hasRankAndSup(Rank.GUIDE);
	}
}
