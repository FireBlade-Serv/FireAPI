package be.goldocelot.admintools;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.title.FireTitle;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.ChatColor;

public class AdminToolsCmd implements CommandExecutor {
	
	private FireAPI api;
	
	public AdminToolsCmd(FireAPI api) {
		this.api = api;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"Vous ne pouvez pas utiliser les commandes de ce Plugin via la console.");
		} else {
			Player p = (Player) sender;
			FirePlayer fp = new FirePlayer(p, this.api);
			
			if (fp.hasRankAndSup(Rank.MODÉRATEUR)) {
				if(args.length == 0) {
					p.sendMessage(ChatColor.RED+"Utiliser \"/at help\" pour avoir la liste des commandes et leurs effets.");
				} else if (args.length > 2) {
					p.sendMessage(ChatColor.RED+"Utiliser \"/at help\" pour avoir la liste des commandes et leurs effets.");
				} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
					p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Liste des commandes du Plugin "+ChatColor.WHITE+"[AdminTools] ");
					p.sendMessage(ChatColor.WHITE+"/at as :"+ChatColor.GREEN+" Enlever/Obtenir l'AdminStick dans son inventaire.");
					p.sendMessage(ChatColor.WHITE+"/at s [Joueur] :"+ChatColor.GREEN+" Passer en gamemode survie le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at c [Joueur] :"+ChatColor.GREEN+" Passer en gamemode créatif le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at a [Joueur]:"+ChatColor.GREEN+" Passer en gamemode aventure le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at sp [Joueur] :"+ChatColor.GREEN+" Passer en gamemode spéctateur le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at heal [Joueur] :"+ChatColor.GREEN+" Heal le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at feed [Joueur] :"+ChatColor.GREEN+" Feed le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at r [Joueur] :"+ChatColor.GREEN+" Ouvre le menu du joueur.");
					p.sendMessage(ChatColor.WHITE+"/at kill [Joueur] :"+ChatColor.GREEN+" Tue le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at clear [Joueur] :"+ChatColor.GREEN+" Clear le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at ec [Joueur] :"+ChatColor.GREEN+" Ouvre l'enderchest du joueur.");
					p.sendMessage(ChatColor.WHITE+"/at inv [Joueur] :"+ChatColor.GREEN+" Ouvre l'inventaire du joueur.");
					p.sendMessage(ChatColor.WHITE+"/at tp [Joueur] :"+ChatColor.GREEN+" Vous téléporte au joueur.");
					p.sendMessage(ChatColor.WHITE+"/at freeze [Joueur] (Joueur obligatoire) :"+ChatColor.GREEN+" Freeze le joueur.");
					p.sendMessage(ChatColor.WHITE+"/at vanish :"+ChatColor.GREEN+" Activer/désactiver le vanish.");
					p.sendMessage(ChatColor.WHITE+"/at flyspeed :"+ChatColor.GREEN+" Activer/désactiver le fly speed.");
					p.sendMessage(ChatColor.WHITE+"/at as gui :"+ChatColor.GREEN+" Ouvre le menu de l'AdminStick");
					p.sendMessage(ChatColor.WHITE+"Utiliser les commandes contenant \"[Joueur]\" sans définir celui-ci vous vise par défaut.");
					p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Développé par _goldocelot_.");
				} else if (args.length == 1 && args[0].equalsIgnoreCase("as")) {
					ItemStack adminStick = new ItemStack(Material.STICK);
					ItemMeta adminStickMeta = adminStick.getItemMeta();
					adminStickMeta.setDisplayName(ChatColor.DARK_BLUE+"AdminStick");
					adminStick.setItemMeta(adminStickMeta);
					
					if (!p.getInventory().containsAtLeast(adminStick, 1)) {
						p.getInventory().addItem(adminStick);
						p.sendMessage(ChatColor.BLUE+"Ajout d'un AdminStick dans votre inventaire.");
					} else {
						p.getInventory().remove(adminStick);
						p.sendMessage(ChatColor.BLUE+"Supression d'un AdminStick dans votre inventaire.");
					}
				} else if (args.length == 2 && args[0].equals("as") && args[1].equals("gui")) {
						Inventory i1 = Bukkit.createInventory(null, 27, ChatColor.GOLD+"AdminTools");
						i1.setItem(1, EventsAT.generateItem(Material.STONE_AXE, ChatColor.DARK_RED+"Gamemode Survie"));
						i1.setItem(3, EventsAT.generateItem(Material.GRASS, ChatColor.DARK_GREEN+"Gamemode Créatif"));
						i1.setItem(5, EventsAT.generateItem(Material.IRON_SWORD, ChatColor.GOLD+"Gamemode Aventure"));
						i1.setItem(7, EventsAT.generateItem(Material.GLASS, ChatColor.GRAY+"Gamemode Spectateur"));
						i1.setItem(9, EventsAT.adminToolsLogo("_goldocelot"));
						i1.setItem(19, EventsAT.generatePotItem(PotionType.INVISIBILITY, ChatColor.WHITE+"Activer/Desactiver le vanish"));
						i1.setItem(21, EventsAT.generatePotItem(PotionType.INSTANT_HEAL, ChatColor.LIGHT_PURPLE+"Se heal"));
						i1.setItem(23, EventsAT.generateItem(Material.COOKED_BEEF, ChatColor.GREEN+"Se feed"));
						i1.setItem(25, EventsAT.generatePotItem(PotionType.SPEED, ChatColor.BLUE+"Activer/Desactiver le fly speed"));
						p.openInventory(i1);
				} else if (args[0].equalsIgnoreCase("s")) {
					 if (args.length == 1) {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur survie.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.setGameMode(GameMode.SURVIVAL);
							p.sendMessage(ChatColor.BLUE+"Gamemode de "+ChatColor.WHITE+target.getName()+ChatColor.BLUE+" réglé sur survie.");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}						
				} else if (args[0].equalsIgnoreCase("c")) {
					if (args.length == 1) {
						p.setGameMode(GameMode.CREATIVE);
						p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur créatif.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.setGameMode(GameMode.CREATIVE);
							p.sendMessage(ChatColor.BLUE+"Gamemode de "+ChatColor.WHITE+target.getName()+ChatColor.BLUE+" réglé sur créatif.");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("a")) {
					if (args.length == 1) {
						p.setGameMode(GameMode.ADVENTURE);
						p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur aventure.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.setGameMode(GameMode.ADVENTURE);
							p.sendMessage(ChatColor.BLUE+"Gamemode de "+ChatColor.WHITE+target.getName()+ChatColor.BLUE+" réglé sur aventure.");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("sp")) {
					if (args.length == 1) {
						p.setGameMode(GameMode.SPECTATOR);
						p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur spéctateur.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.setGameMode(GameMode.SPECTATOR);
							p.sendMessage(ChatColor.BLUE+"Gamemode de "+ChatColor.WHITE+target.getName()+ChatColor.BLUE+" réglé sur spéctateur.");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("heal")) {
					if(args.length == 1) {
						p.setHealth(p.getMaxHealth());
						p.sendMessage(ChatColor.BLUE+"Vie réstaurée.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.setHealth(p.getMaxHealth());
							p.sendMessage(ChatColor.BLUE+"Vie de "+ChatColor.WHITE+target.getName()+ChatColor.BLUE+" réstaurée.");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("feed")) {
					if(args.length == 1) {
						p.setFoodLevel(20);
						p.sendMessage(ChatColor.BLUE+"Nourriture réstaurée.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.setFoodLevel(20);
							p.sendMessage(ChatColor.BLUE+"Nourriture de "+ChatColor.WHITE+target.getName()+ChatColor.BLUE+" réstaurée.");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("vanish") && args.length == 1) {
					if (EventsAT.pVanish.contains(p)) {
						for(Player online : Bukkit.getOnlinePlayers()) {
							online.showPlayer(p);
						}
						
						p.sendMessage(ChatColor.BLUE+"Vous n'êtes plus vanish.");
						
						EventsAT.pVanish.remove(p);
					} else if (!EventsAT.pVanish.contains(p)) {
						for(Player online : Bukkit.getOnlinePlayers()) {
							online.hidePlayer(p);
						}
						
						p.sendMessage(ChatColor.BLUE+"Vous êtes vanish.");
						
						EventsAT.pVanish.add(p);
					}
				} else if (args[0].equalsIgnoreCase("FlySpeed") && args.length == 1) {
					if(p.getFlySpeed() == 0.3f) {
						p.setFlySpeed(0.1f);
						p.sendMessage(ChatColor.BLUE+"Speed désactivé.");
					} else if (p.getFlySpeed() == 0.1f) {
						p.setFlySpeed(0.3f);
						p.sendMessage(ChatColor.BLUE+"Speed activé.");
					}
				} else if (args[0].equalsIgnoreCase("r")) {
					if (args.length == 1) {
						Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN+"PlayerMenu");
						EventsAT.createPlayerMenu(p, inv);
						p.openInventory(inv);
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN+"PlayerMenu");
						EventsAT.createPlayerMenu(target, inv);
						p.openInventory(inv);
					}
				} else if (args[0].equalsIgnoreCase("tp")) {
					if (args.length == 1) {
						p.teleport(p);
						p.sendMessage(ChatColor.BLUE+"Vous avez été téléporté sur vous-même");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							p.teleport(target);
							p.sendMessage(ChatColor.BLUE+"Vous avez été téléporté à \""+ChatColor.WHITE+target.getName()+ChatColor.BLUE+"\" .");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}						
				} else if (args[0].equalsIgnoreCase("freeze") && args.length == 2) {
					Player target = Bukkit.getPlayer(args[1]);
					
					if(target != null) {						
						if(EventsAT.freezes.contains(target)) {
							p.sendMessage("§9Le joueur \"§f"+target.getName()+"§9 \" n'est plus freeze.");
							
							FireTitle gt = new FireTitle(target, "", "§cVous n'êtes plus freeze !", 20, 50, 20);
							gt.send();
							
							target.setWalkSpeed(0.2f);
							
							EventsAT.freezes.remove(target);
							p.closeInventory();
						} else {
							p.sendMessage("§9Le joueur \"§f"+target.getName()+"§9 \" est freeze.");
							
							FireTitle gt = new FireTitle(target, "", "§9Vous êtes freeze !", 20, 50, 20);
							gt.send();
							
							target.setWalkSpeed(0.0f);
							EventsAT.freezes.add(target);
							p.closeInventory();
						}
					}else {
						p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
					}
				} else if (args[0].equalsIgnoreCase("kill")) {
					if(args.length == 1) {
						p.damage(p.getMaxHealth());
						p.sendMessage(ChatColor.BLUE+"Vous vous êtes tué.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.damage(target.getMaxHealth());
							p.sendMessage(ChatColor.BLUE+"Vous avez tué \""+ChatColor.WHITE+target.getName()+ChatColor.BLUE+"\" .");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				}  else if (args[0].equalsIgnoreCase("clear")) {
					if(args.length == 1) {
						p.getInventory().clear();
						p.getInventory().setHelmet(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setLeggings(null);
						p.getInventory().setBoots(null);
						p.sendMessage(ChatColor.BLUE+"Vous vous êtes clear.");
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							target.getInventory().clear();
							target.getInventory().setHelmet(null);
							target.getInventory().setChestplate(null);
							target.getInventory().setLeggings(null);
							target.getInventory().setBoots(null);
							p.sendMessage(ChatColor.BLUE+"Vous avez clear \""+ChatColor.WHITE+target.getName()+ChatColor.BLUE+"\" .");
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("ec")) {
					if(args.length == 1) {
						p.sendMessage("§9Vous avez ouvert votre enderchest.");
						
						p.openInventory(EventsAT.createEnderChest(p));
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							p.sendMessage("§9Ender Chest de \"§f"+target.getName()+"§9 \" ouvert.");
							
							p.openInventory(EventsAT.createEnderChest(target));
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else if (args[0].equalsIgnoreCase("inv")) {
					if(args.length == 1) {
						p.sendMessage("§9Vous avez ouvert votre inventaire.");
						
						p.openInventory(EventsAT.createPlayerInventory(p));
					} else if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target != null) {
							p.sendMessage("§9Iventaire de \"§f"+target.getName()+"§9 \" ouvert.");
							
							p.openInventory(EventsAT.createPlayerInventory(target));
						}else {
							p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+args[1]+ChatColor.BLUE+"\" .");
						}
					}
				} else {
						p.sendMessage(ChatColor.RED+"Utiliser \"/at help\" pour avoir la liste des commandes et leurs effets.");
				}
			} else {
				p.sendMessage(ChatColor.DARK_RED+"Vous n'avez pas les permissions pour utiliser cette commande!");
			}
		}
		return false;
	}
				
}