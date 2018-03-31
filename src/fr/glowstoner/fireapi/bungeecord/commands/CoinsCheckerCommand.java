package fr.glowstoner.fireapi.bungeecord.commands;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.rank.Rank;
import fr.glowstoner.fireapi.sql.FireSQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CoinsCheckerCommand extends Command {
	
	private final String pre = "§6[§eFireAPI§6]§r ";
	
	private FireAPI api;
	private FireSQL sql;

	public CoinsCheckerCommand(FireAPI api, String name) {
		super(name);
		
		this.api = api;
		this.sql = this.api.getSQL();
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(this.api.getRankSystem().hasRankAndSupOrConsole(sender, Rank.MANAGER_IG)){
			if(args.length == 0) {
				sender.sendMessage(new TextComponent(this.pre+"§cUsage : /coins [ultragems/firecoins] pseudo"));
			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("ultragems")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /coins ultragems pseudo"));
				}else if(args[0].equalsIgnoreCase("firecoins")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /coins firecoins pseudo"));
				}else {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /coins [ultragems/firecoins] pseudo"));
				}
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("ultragems")) {
					try {
						int gems = this.sql.getUltraMoney(args[1]);
						
						if(gems == 0) {
							sender.sendMessage(new TextComponent(this.pre+"Le joueur §e"+args[1]+"§r ne possède §aaucune UltraGem"));
						}else {
							sender.sendMessage(new TextComponent
									(this.pre+"Le joueur §e"+args[1]+"§r possède §a"+gems+" UltraGems"));
						}
					}catch(Exception ex) {
						sender.sendMessage(new TextComponent(this.pre+"§cErreur, le joueur \""+args[1]+"\" est introuvable !"
								+ " Il est soit inexistant soit non-inscrit sur le site internet !"));
					}
				}else if(args[0].equalsIgnoreCase("firecoins")) {
					try {
						int coins = this.sql.getFireMoney(args[1]);
						
						if(coins == 0) {
							sender.sendMessage(new TextComponent(this.pre+"Le joueur "+args[1]+" ne possède §eaucun FireCoin"));
						}else {
							sender.sendMessage(new TextComponent
									(this.pre+"Le joueur §e"+args[1]+"§r possède §e"+coins+" FireCoins"));
						}
					}catch(Exception ex) {
						sender.sendMessage(new TextComponent(this.pre+"§cErreur, le joueur \""+args[1]+"\" est introuvable !"));
					}
				}else {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /coins [ultragems/firecoins] pseudo"));
				}
			}else {
				sender.sendMessage(new TextComponent(this.pre+"§cUsage : /coins [ultragems/firecoins] pseudo"));
			}
		}else {
			sender.sendMessage(new TextComponent
					(this.pre+"§cErreur, vous n'avez pas la permission d'utiliser cette commande !"));
		}
	}
}
