package fr.glowstoner.fireapi.rank;

import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.fireapi.sql.FireSQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.command.ConsoleCommandSender;

public class FireRank {
	
	private FireSQL sql;

	public FireRank(FireSQL sql) {
		this.sql = sql;
	}

	public Rank getRankByName(String name) {
		return Rank.valueOf(name);
	}
	
	public boolean containsRank(String rank) {
		for(Rank all : Rank.values()) {
			if(all.name().equalsIgnoreCase(rank)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Rank getApproximative(String rank) {
		if(!containsRank(rank)) {
			return null;
		}
		
		for(Rank all : Rank.values()) {
			if(all.name().equalsIgnoreCase(rank)) {
				return all;
			}
		}
		
		return null;
	}
	
	public void setPlayerRank(String player, Rank rank) {
		this.sql.setRank(player, rank);
	}

	public Rank getPlayerRank(String name) {
		return getApproximative(this.sql.getRank(name));
	}
	
	public Rank[] getRankAndSup(Rank rank) {
		Rank[] ranks = new Rank[15];
		int index = 0;
		
		for(Rank val : Rank.values()) {
			if(val.getPower() >= rank.getPower()) {
				ranks[index] = val;
				
				index++;
			}
		}
		
		return ranks;
	}
	
	public List<Rank> getListRankAndSup(Rank rank) {
		List<Rank> list = new ArrayList<>();
		
		for(Rank val : Rank.values()) {
			if(val.getPower() >= rank.getPower()) {
				list.add(val);
			}
		}
		
		return list;
	}
	
	public boolean hasRankAndSup(String name, Rank rank) {
		return this.getListRankAndSup(this.getPlayerRank(name)).contains(rank);
	}
	
	public boolean hasRankAndSupOrConsole(CommandSender sender, Rank rank) {
		if(sender instanceof ConsoleCommandSender) {
			return true;
		}
		
		return getListRankAndSup(rank).contains(getPlayerRank(sender.getName()));
	}
}
