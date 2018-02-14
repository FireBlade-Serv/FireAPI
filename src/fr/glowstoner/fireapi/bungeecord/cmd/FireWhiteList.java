package fr.glowstoner.fireapi.bungeecord.cmd;

import java.io.File;
import java.io.IOException;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class FireWhiteList extends Command {

	private final String pre = "§6[§eWhiteList§6]§r ";
	private FireAPI api;

	public FireWhiteList(String name, FireAPI api) {
		super(name);
		
		this.api = api;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!this.api.getRankSystem().hasRankAndSupOrConsole(sender, Rank.MANAGER_IG)) {
			sender.sendMessage(new TextComponent(this.pre+"§cVous n'avez pas la permission d'utiliser cette commande !"));
			
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firewl <add/remove/on/off> (<pseudo>)"));
			
			return;
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				File file = new File(this.api.getBungeePlugin().getDataFolder(), "whitelist.yml");
				
				try {
					Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
					
					config.set("enable", true);
					
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
					
					sender.sendMessage(new TextComponent(this.pre+"§aLe plugin est activé !"));
					
					this.api.getWhiteListSystem().setEnable(true);
				} catch (IOException e) {
					sender.sendMessage(new TextComponent(this.pre+"§cUne erreur interne au plugin est survenue !"));
				}
			}else if(args[0].equalsIgnoreCase("off")) {
				File file = new File(this.api.getBungeePlugin().getDataFolder(), "whitelist.yml");
				
				try {
					Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
					
					config.set("enable", false);
					
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
					
					sender.sendMessage(new TextComponent(this.pre+"§aLe plugin est désactivé !"));
					
					this.api.getWhiteListSystem().setEnable(false);
				} catch (IOException e) {
					sender.sendMessage(new TextComponent(this.pre+"§cUne erreur interne au plugin est survenue !"));
				}
			}else {
				sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firewl <add/remove/on/off> (<pseudo>)"));
				
				return;
			}
		}else if(args.length == 2) {
			if(this.api.getWhiteListSystem().isEnable() == false) {
				sender.sendMessage(new TextComponent(this.pre+"§cLe plugin est désactivé !"));
				
				return;
			}
			
			if(args[0].equalsIgnoreCase("add")) {
				try {
					File file = new File(this.api.getBungeePlugin().getDataFolder(), "whitelist.yml");
					
					Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
					
					if(config.getBoolean("players."+args[1]+".accepted") == true) {
						sender.sendMessage(new TextComponent(this.pre+"§cLe joueur "+args[1]+" est déjà dans la whitelist !"));
						
						return;
					}
					
					config.set("players."+args[1]+".accepted", true);
					
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
					
					sender.sendMessage(new TextComponent(this.pre+"§aVous avez ajouté le joueur "+args[1]+" à la whitelist !"));
					
					return;
				} catch (IOException e) {
					sender.sendMessage(new TextComponent(this.pre+"§cUne erreur interne au plugin est survenue !"));
				}
			}else if(args[0].equalsIgnoreCase("remove")) {
				//remove

				try {
					File file = new File(this.api.getBungeePlugin().getDataFolder(), "whitelist.yml");
					
					Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
					
					if(config.getBoolean("players."+args[1]+".accepted") == false) {
						sender.sendMessage(new TextComponent(this.pre+"§cLe joueur "+args[1]+" n'existe pas !"));
						
						return;
					}
					
					config.set("players."+args[1]+".accepted", false);
					
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
					
					sender.sendMessage(new TextComponent(this.pre+"§aVous avez retiré le joueur "+args[1]+" de la WhiteList !"));
				} catch (IOException e) {
					sender.sendMessage(new TextComponent(this.pre+"§cUne erreur interne au plugin est survenue !"));
				}
			}else {
				sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firewl <add/remove/on/off> (<pseudo>)"));
				
				return;
			}
		}else {
			sender.sendMessage(new TextComponent(this.pre+"§cUsage : /firewl <add/remove/on/off> (<pseudo>)"));
			
			return;
		}
	}

}