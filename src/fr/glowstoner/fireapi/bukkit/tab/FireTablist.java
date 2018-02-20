package fr.glowstoner.fireapi.bukkit.tab;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;
import lombok.Getter;

public class FireTablist {
	
	private @Getter Scoreboard scoreboard;
	
	private FireAPI api;
	
	public FireTablist(FireAPI api) {
		this.api = api;
		
		this.scoreboard = this.api.getBukkitPlugin().getServer().getScoreboardManager().getNewScoreboard();
	}
	
	public void registerRanks() {
		for(Rank r : Rank.values()) {
			this.scoreboard.registerNewTeam(r.name());
			
			this.scoreboard.getTeam(r.name()).setPrefix(this.api.getChatUtils().getPrefixByRank(r));
		}
	}
	
	@SuppressWarnings("deprecation")
	public void add(Player p) {
		FirePlayer fp = new FirePlayer(p, this.api);
		
		p.setScoreboard(this.scoreboard);
		
		this.scoreboard.getTeam(fp.getRank().name()).addPlayer(p);
	}
	
	@SuppressWarnings("deprecation")
	public void remove(Player p) {
		FirePlayer fp = new FirePlayer(p, this.api);
		
		p.setScoreboard(this.scoreboard);
		
		this.scoreboard.getTeam(fp.getRank().name()).removePlayer(p);
	}
}
