package fr.glowstoner.fireapi.bungeecord.cmd;

import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.rank.Rank;
import fr.glowstoner.fireapi.sql.FireSQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class FireRankCmd extends Command {

	private final String pre = "§6[§eRank§6]§r ";
	
	private FireRank rank;
	private FireSQL sql;

	public FireRankCmd(String name, FireRank rank, FireSQL sql) {
		super(name);
		
		this.rank = rank;
		this.sql = sql;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(this.rank.hasRankAndSupOrConsole(sender, Rank.STAFF_MANAGER)) {
			if(args.length == 0) {
				sender.sendMessage(new TextComponent(this.pre+"/firerank show <pseudo>"));
				sender.sendMessage(new TextComponent(this.pre+"/firerank set <pseudo> <rang>"));
			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("show")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firerank show <pseudo>"));
				}else if(args[0].equalsIgnoreCase("set")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firerank set <pseudo> <rang>"));
				}else {
					sender.sendMessage(new TextComponent(this.pre+"/firerank show <pseudo>"));
					sender.sendMessage(new TextComponent(this.pre+"/firerank set <pseudo> <rang>"));
				}
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("show")) {
					if(this.sql.hasFireAccount(args[1])) {
						sender.sendMessage(new TextComponent(
								this.pre+"Le joueur §a"+args[1]+"§r est §e"+this.rank.getPlayerRank(args[1]).name()+"§r !"));
					}else {
						sender.sendMessage(new TextComponent(
								this.pre+"§cImpossible de trouver le joueur \""+args[1]+"\" !"));
					}
				}else if(args[0].equalsIgnoreCase("set")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firerank set <pseudo> <rang>"));
				}else {
					sender.sendMessage(new TextComponent(this.pre+"/firerank show <pseudo>"));
					sender.sendMessage(new TextComponent(this.pre+"/firerank set <pseudo> <rang>"));
				}
			}else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("show")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firerank show <pseudo>"));
				}else if(args[0].equalsIgnoreCase("set")) {
					if(this.sql.hasFireAccount(args[1])) {
						if(containsRank(args[2])) {
							this.rank.setPlayerRank(args[1], getApproximative(args[2]));
							
							sender.sendMessage(new TextComponent(this.pre+"§e"+args[1]+
									" §rpossède maintenant le grade de §e"+getApproximative(args[2]).name()+"§r !"));
						}else {
							sender.sendMessage(new TextComponent(this.pre+"§cErreur, ce grade n'existe pas !"));
							sender.sendMessage(new TextComponent(
									this.pre+"§cVoici la liste des grades disponibles : "+getRankList()));
						}
					}else {
						sender.sendMessage(new TextComponent(
								this.pre+"§cImpossible de trouver le joueur \""+args[1]+"\" !"));
					}
				}
			}
		}else {
			sender.sendMessage(new TextComponent(this.pre+"§cVous n'avez pas la permission d'utiliser cette commande !"));
		}
	}
	
	private boolean containsRank(String rank) {
		for(Rank all : Rank.values()) {
			if(all.name().equalsIgnoreCase(rank)) {
				return true;
			}
		}
		
		return false;
	}
	
	private Rank getApproximative(String rank) {
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
	
	private String getRankList() {
		StringBuilder builder = new StringBuilder();
		
		for(Rank rang : Rank.values()) {
			builder.append("§c"+rang.name()+", ");
		}
		
		String base = builder.toString();
		
		return base.substring(0, base.length() - 2).concat(".");
	}
}
