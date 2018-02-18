package fr.glowstoner.fireapi.bungeecord.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bungeecord.friends.Friends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.PacketFriends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.action.FriendsActionTransmetterGUI;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class FriendsCmd extends Command {
	
	private final String pre = "§6[§eAmis§6]§r ";
	
	private Plugin instance;
	private Friends friends;
	private FireAPI api;
	private Client c;

	public FriendsCmd(FireAPI api, Client c, String name, Friends friends) {
		super(name);
		
		this.api = api;
		this.instance = this.api.getBungeePlugin();
		this.friends = friends;
		this.c = c;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			if(sender instanceof ProxiedPlayer) {
				sender.sendMessage(new TextComponent(this.pre+"/amis add <pseudo>"));
				sender.sendMessage(new TextComponent(this.pre+"/amis remove <pseudo>"));
				sender.sendMessage(new TextComponent(this.pre+"/amis list"));
				sender.sendMessage(new TextComponent(this.pre+"/amis join <pseudo>"));
				sender.sendMessage(new TextComponent(this.pre+"/amis accept <pseudo>"));
				sender.sendMessage(new TextComponent(this.pre+"/amis gui"));
			}
			
			if(this.api.getRankSystem().hasRankAndSupOrConsole(sender, Rank.MANAGER_IG)) {
				sender.sendMessage(new TextComponent(this.pre+"/amis show <pseudo>"));
			}
		}else if(args.length == 1) {
			if(sender instanceof ProxiedPlayer) {
				ProxiedPlayer pp = (ProxiedPlayer) sender;
				
				if(args[0].equalsIgnoreCase("add")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /amis add <pseuso>"));
				}else if(args[0].equalsIgnoreCase("remove")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /amis remove <pseudo>"));
				}else if(args[0].equalsIgnoreCase("list")) {
					//list friends
					
					try {
						this.friends.sendGeneratedList(pp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(args[0].equalsIgnoreCase("join")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /amis join <pseudo>"));
				}else if(args[0].equalsIgnoreCase("accept")) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /amis accept <pseudo>"));
				}else if(args[0].equalsIgnoreCase("gui")) {
					List<String> names = new ArrayList<>();
					
					for(ProxiedPlayer pps : this.api.getBungeePlugin().getProxy().getPlayers()) {
						names.add(pps.getName());
					}
					
					try {
						c.sendPacket(new PacketFriends(new FriendsActionTransmetterGUI(pp.getName(),
								pp.getServer().getInfo().getName().concat("-spigot"),
								this.friends.getAllFriends(pp.getName()), names)));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					if(sender instanceof ProxiedPlayer) {
						sender.sendMessage(new TextComponent(this.pre+"/amis add <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis remove <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis list"));
						sender.sendMessage(new TextComponent(this.pre+"/amis join <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis accept <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis gui"));
					}
					
					if(this.api.getRankSystem().hasRankAndSupOrConsole(sender, Rank.MANAGER_IG)) {
						sender.sendMessage(new TextComponent(this.pre+"/amis show <pseudo>"));
					}
				}
			}else {
				if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("list") || 
						args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("accept")) {
					
					sender.sendMessage(new TextComponent(this.pre+
							"§cErreur, la console ne peut avoir d'amis :'("
							+ " (sauf Glowstoner mdr, sauf que c'est moi qui écrit ces lignes sur Eclipse,"
							+ " serais-je skyzophrène ?) C'est probable dit quelqu'un à l'horizon ... §oC'est le début d'une"
							+ " longue histoire"));
				}
			}
			
			if(args[0].equalsIgnoreCase("show")) {
				if(this.api.getRankSystem().hasRankAndSupOrConsole(sender, Rank.MANAGER_IG)) {
					sender.sendMessage(new TextComponent(this.pre+"§cUsage : /amis show <pseudo>"));
				}else {
					sender.sendMessage(new TextComponent(this.pre+
							"§cErreur, vous n'avez pas la permission d'utiliser cette fonctionnalité !"));
				}
			}
		}else if(args.length == 2){
			if(sender instanceof ProxiedPlayer) {
				ProxiedPlayer pp = (ProxiedPlayer) sender;
				ProxiedPlayer target = getPlayerByName(args[1]);
				FirePlayer fp = new FirePlayer(pp, this.api);
				
				if(args[0].equalsIgnoreCase("add")) {
					//add
					
					if(target == null) {
						pp.sendMessage(new TextComponent(this.pre+"§cErreur, aucun joueur trouvé pour \""+args[1]+"\" !"));
						return;
					}
					
					if(target.equals(pp)) {
						pp.sendMessage(new TextComponent(this.pre+"§cVous devriez pensez à soigner votre skyzophrènie"));
						return;
					}
					
					try {
						if(!this.friends.isAlreadyFriends(pp, target)) {
							if(this.friends.canHaveMoreFriends(fp)) {
								pp.sendMessage(new TextComponent(this.pre+"§aVous avez bien "
										+ "envoyé votre demande à §2"+target.getName()));
								this.friends.sendRequest(pp, target);
							}else {
								pp.sendMessage(new TextComponent(this.pre+
										"§cErreur vous avez ateint le nombre maximum d'amis pour votre grade !"));
							}
						}else {
							pp.sendMessage(new TextComponent(this.pre+"§cErreur, vous êtes déjà "
									+ "ami avec "+target.getName()+" !"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(args[0].equalsIgnoreCase("remove")) {
					try {
						this.friends.removeFriend(pp, args[1]);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}else if(args[0].equalsIgnoreCase("join")) {
					sender.sendMessage(new TextComponent(this.pre+"§5Indisponible pour le moment !"));
				}else if(args[0].equalsIgnoreCase("accept")) {
					//accept
					
					if(target == null) {
						pp.sendMessage(new TextComponent(this.pre+"§cErreur, aucun joueur trouvé pour \""+args[1]+"\" !"));
						return;
					}
					
					try {
						this.friends.acceptRequest(pp, target);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(args[0].equalsIgnoreCase("show")) {
					if(this.api.getRankSystem().hasRankAndSupOrConsole(sender, Rank.MANAGER_IG)) {
						try {
							this.friends.sendGeneratedListOther(sender, args[1]);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}else {
						sender.sendMessage(new TextComponent(this.pre+
								"§cErreur, vous n'avez pas la permission d'utiliser cette fonctionnalité !"));
					}
				}else {
					if(sender instanceof ProxiedPlayer) {
						sender.sendMessage(new TextComponent(this.pre+"/amis add <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis remove <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis list"));
						sender.sendMessage(new TextComponent(this.pre+"/amis join <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis accept <pseudo>"));
						sender.sendMessage(new TextComponent(this.pre+"/amis gui"));
					}
					
					if(sender.hasPermission("fireapi.friends.showother")) {
						sender.sendMessage(new TextComponent(this.pre+"/amis show <pseudo>"));	
					}
				}
			}else {
				if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("list") || 
						args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("accept")) {
					
					sender.sendMessage(new TextComponent(this.pre+
							"§cErreur, la console ne peut avoir d'amis :'("
							+ " (sauf Glowstoner mdr, sauf que c'est moi qui écrit ses lignes sur Eclipse,"
							+ " serais-je skyzophrène ?) C'est probable dit quelqu'un à l'horizon ... §oC'est le début d'une"
							+ " longue histoire"));
				}else{
					try {
						this.friends.sendGeneratedListOther(sender, args[1]);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	private ProxiedPlayer getPlayerByName(String name) {
		ProxiedPlayer pp = null;
		
		try {
			pp = this.instance.getProxy().getPlayer(name);
		}catch(Exception ex) {
			pp = null;
		}
		
		return pp;
	}
}
