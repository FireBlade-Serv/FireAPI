package be.goldocelot.admintools;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.title.FireTitle;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;

public class EventsAT implements Listener {

	public static ArrayList<Player> pVanish = new ArrayList<Player>();
	private static ArrayList<Player> pCo = new ArrayList<Player>();
	public static ArrayList<Player> freezes = new ArrayList<Player>();
	private static HashMap<Player, Integer> currentPage = new HashMap<Player, Integer>();
	private static HashMap<Player, String> playerMenu = new HashMap<Player, String>();
	
	private FireAPI api;
	
	public EventsAT(FireAPI api) {
		this.api = api;
	}

	@EventHandler
	public void onMove (PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		
		if(e.getFrom().getY() < e.getTo().getY()) {
			if(!((CraftPlayer) p).getHandle().onGround){
				if(freezes.contains(p)) {
					e.setCancelled(true);			
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		
		if(pVanish.contains(p)) {
			pVanish.remove(p);
			
			for(Player online : Bukkit.getOnlinePlayers()) {
				online.showPlayer(p); 
			}
		}
	}
	
	@EventHandler
	public void onRightClick (PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		final Action a = e.getAction();
		
		FirePlayer fp = new FirePlayer(p, this.api);
		
		ItemStack adminStick = new ItemStack(Material.STICK);
		ItemMeta adminStickMeta = adminStick.getItemMeta();
		adminStickMeta.setDisplayName(ChatColor.DARK_BLUE+"AdminStick");
		adminStick.setItemMeta(adminStickMeta);
		
		if(fp.hasRankAndSup(Rank.MODÉRATEUR)) {
			if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
				if (p.getInventory().getItemInHand().equals(adminStick)) {
					Inventory i1 = Bukkit.createInventory(null, 27, ChatColor.GOLD+"AdminTools");
					i1.setItem(1, generateItem(Material.STONE_AXE, ChatColor.DARK_RED+"Gamemode Survie"));
					i1.setItem(3, generateItem(Material.GRASS, ChatColor.DARK_GREEN+"Gamemode Créatif"));
					i1.setItem(5, generateItem(Material.IRON_SWORD, ChatColor.GOLD+"Gamemode Aventure"));
					i1.setItem(7, generateItem(Material.GLASS, ChatColor.GRAY+"Gamemode Spectateur"));
					i1.setItem(9, adminToolsLogo("_goldocelot_"));
					i1.setItem(13, generateItem(Material.PAPER, ChatColor.WHITE+"Ouvrir l'AdminList"));
					i1.setItem(19, generatePotItem(PotionType.INVISIBILITY, ChatColor.WHITE+"Activer/Desactiver le vanish"));
					i1.setItem(21, generatePotItem(PotionType.INSTANT_HEAL, ChatColor.LIGHT_PURPLE+"Se heal"));
					i1.setItem(23, generateItem(Material.COOKED_BEEF, ChatColor.GREEN+"Se feed"));
					i1.setItem(25, generatePotItem(PotionType.SPEED, ChatColor.BLUE+"Activer/Desactiver le fly speed"));
					p.openInventory(i1);
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		 final Player p = (Player) e.getWhoClicked();
		 final Inventory i = e.getInventory();
		 ItemStack item = e.getCurrentItem(); 
		 
		if(i.getName().equals(ChatColor.GOLD+"AdminTools")) {
			if(item.equals(generateItem(Material.STONE_AXE, ChatColor.DARK_RED+"Gamemode Survie"))) {
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur survie.");
				p.closeInventory();
			} else if(item.equals(generateItem(Material.GRASS, ChatColor.DARK_GREEN+"Gamemode Créatif"))){
				p.setGameMode(GameMode.CREATIVE);
				p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur créatif.");
				p.closeInventory();
			} else if(item.equals(generateItem(Material.IRON_SWORD, ChatColor.GOLD+"Gamemode Aventure"))) {
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur aventure.");
				p.closeInventory();
			} else if(item.equals(generateItem(Material.GLASS, ChatColor.GRAY+"Gamemode Spectateur"))) {
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.BLUE+"Gamemode réglé sur spéctateur.");
				p.closeInventory();
			} else if(item.equals(adminToolsLogo("_goldocelot_"))){
				p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Merci d'utiliser le plugin \"AdminTools\" développé par Goldocelot et Glowstoner.");
				p.closeInventory();
			} else if (item.equals(generatePotItem(PotionType.INVISIBILITY, ChatColor.WHITE+"Activer/Desactiver le vanish"))) {
				if(pVanish.contains(p)) {
					for(Player online : Bukkit.getOnlinePlayers()) {
						online.showPlayer(p);
					}
					
					p.sendMessage(ChatColor.BLUE+"Vous n'êtes plus vanish.");
					
					pVanish.remove(p);
				} else if (!pVanish.contains(p)) {
					for(Player online : Bukkit.getOnlinePlayers()) {
						online.hidePlayer(p);
					}
					
					p.sendMessage(ChatColor.BLUE+"Vous êtes vanish.");
					
					pVanish.add(p);
				}
				p.closeInventory();
			} else if (item.equals(generatePotItem(PotionType.INSTANT_HEAL, ChatColor.LIGHT_PURPLE+"Se heal"))) {
				p.setHealth(p.getMaxHealth());
				p.sendMessage(ChatColor.BLUE+"Vie réstaurée.");
				p.closeInventory();
			} else if (item.equals(generateItem(Material.COOKED_BEEF, ChatColor.GREEN+"Se feed"))) {
				p.setFoodLevel(20);
				p.sendMessage(ChatColor.BLUE+"Nourriture réstaurée.");
				p.closeInventory();
			} else if (item.equals(generatePotItem(PotionType.SPEED, ChatColor.BLUE+"Activer/Desactiver le fly speed"))) {
				if (p.getFlySpeed() == 0.3f) {
					p.setFlySpeed(0.1f);
					p.sendMessage(ChatColor.BLUE+"Speed désactivé.");
				} else if (p.getFlySpeed() == 0.1f) {
					p.setFlySpeed(0.3f);
					p.sendMessage(ChatColor.BLUE+"Speed activé.");
				}
				p.closeInventory();
			} else if (item.equals(generateItem(Material.PAPER, ChatColor.WHITE+"Ouvrir l'AdminList"))) {
				p.closeInventory();				
					initPlayers();
				
				if(currentPage.containsKey(p)) {
					currentPage.remove(p);	
				}
				
				currentPage.put(p, 1);
				
				createInventory(p, currentPage.get(p));
			}
			
			 e.setCancelled(true);

		}else if(i.getName().equals(ChatColor.WHITE+"AdminList")) {
			if(item.getType().equals(Material.SKULL_ITEM)) {
				Player target = Bukkit.getPlayer(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
				if(target == null) {
					p.sendMessage("§9Le joueur "+ChatColor.WHITE+item.getItemMeta().getDisplayName()+ "§9 s'est déconnecté !");
				}else {
					if(playerMenu.containsKey(p)) {
						playerMenu.remove(p, playerMenu.get(p));
					}
					
					playerMenu.put(p, target.getName());
					
					Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN+"PlayerMenu");
					EventsAT.createPlayerMenu(target, inv);
					p.openInventory(inv);
				}
			} else if(item.equals(adminToolsLogo("Glowstoner"))){
				p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Merci d'utiliser le plugin \"AdminTools\" développé par Goldocelot et Glowstoner.");
				p.closeInventory();
			} else if (item.equals(generateItem(Material.PAPER, ChatColor.AQUA+"Suivant"))) {
				p.closeInventory();
				currentPage.replace(p, currentPage.get(p), currentPage.get(p) + 1);
				
				p.sendMessage(""+currentPage.get(p));
				
				createInventory(p, currentPage.get(p)); 
				
			} else if (item.equals(generateItem(Material.PAPER, ChatColor.AQUA+"Précédent"))) {
				p.closeInventory();
				currentPage.replace(p, currentPage.get(p), currentPage.get(p) - 1);
				
				if(currentPage.get(p) == 1) {
					initPlayers();
					p.sendMessage(""+currentPage.get(p));
				}
				
				createInventory(p, currentPage.get(p));
			} else if (item.equals(generateItem(Material.HOPPER, ChatColor.DARK_GREEN+"Rechercher"))) {
				p.sendMessage(ChatColor.BLUE+"Utiliser la commande: /at r [Joueur]");
				
				p.closeInventory();
			} 
			e.setCancelled(true);
		}else if (i.getName().equals(ChatColor.GREEN+"PlayerMenu")) {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
			ItemMeta skullM = skull.getItemMeta();
			skullM.setDisplayName(ChatColor.RED+"Kick (Anti-Jeu)");
			skull.setItemMeta(skullM);
			String targetName = playerMenu.get(p);
			Player target = Bukkit.getPlayer(targetName);
			
			if (item.equals(adminToolsLogo("_goldocelot_ and Glowstoner"))) {
				p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Merci d'utiliser le plugin \"AdminTools\" développé par Goldocelot et Glowstoner.");
			} else if (item.equals(generateItem(Material.PAPER, ChatColor.AQUA+"Retour"))) {
				p.closeInventory();				
				initPlayers();
				
				if(currentPage.containsKey(p)) {
					currentPage.remove(p);	
				}
				
				currentPage.put(p, 1);
				
				createInventory(p, currentPage.get(p));
			} else if (item.equals(generateItem(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE+"Go to"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.teleport(target.getLocation());
					p.sendMessage(ChatColor.BLUE+"Vous avez bien été téléporté sur le joueur \""+ChatColor.WHITE+targetName+ChatColor.BLUE+"\" .");
					p.closeInventory();
				}
			} else if (item.equals(generateItem(Material.PACKED_ICE, ChatColor.GRAY+"Freeze"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					if(freezes.contains(target)) {
						p.sendMessage("§9Le joueur \"§f"+targetName+"§9 \" n'est plus freeze.");
						
						FireTitle gt = new FireTitle(target, "", "§cVous n'êtes plus freeze !", 20, 50, 20);
						gt.send();
						
						target.setWalkSpeed(0.2f);
						
						freezes.remove(target);
						p.closeInventory();
					} else {
						p.sendMessage("§9Le joueur \"§f"+targetName+"§9 \" est freeze.");
						
						FireTitle gt = new FireTitle(target, "", "§9Vous êtes freeze !", 20, 50, 20);
						gt.send();
						
						target.setWalkSpeed(0.0f);
						freezes.add(target);
						p.closeInventory();
					}
				}
			} else if (item.equals(generateItem(Material.REDSTONE, ChatColor.DARK_RED+"Kill"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.sendMessage("§9Le joueur \"§f"+targetName+"§9 \" a été kill.");
					
					target.damage(target.getMaxHealth());
					p.closeInventory();
				}
			} else if (item.equals(generateItem(Material.BUCKET, ChatColor.GRAY+"Clear"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.sendMessage("§9Le joueur \"§f"+targetName+"§9 \" a été clear.");
					
					target.getInventory().clear();
					target.getInventory().setHelmet(null);
					target.getInventory().setChestplate(null);
					target.getInventory().setLeggings(null);
					target.getInventory().setBoots(null);
					p.closeInventory();
				}
			} else if (item.equals(generateItem(Material.JUKEBOX, ChatColor.RED+"Mute"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.closeInventory();
				// ouvrir le menu MUTE
				
				// Mute le target.
				}
			} else if (item.equals(skull)) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.sendMessage("§9Le joueur \"§f"+targetName+"§9 \" a été kick.");
					p.closeInventory();
				// Kick le target.
				}
			} else if (item.equals(generateItem(Material.BARRIER, ChatColor.RED+""+ChatColor.BOLD+"Ban"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.closeInventory();
					p.openInventory(createBanMenu(target));
					
				}
			} else if (item.equals(generateItem(Material.ENDER_CHEST, ChatColor.GRAY+"Inventaire de l'ender chest"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.closeInventory();
					p.sendMessage("§9Ender Chest de \"§f"+targetName+"§9 \" ouvert.");
					
					p.openInventory(createEnderChest(target));					
				}
			} else if (item.equals(generateItem(Material.CHEST, ChatColor.GOLD+"Inventaire"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.closeInventory();
					p.sendMessage("§9Inventaire de \"§f"+targetName+"§9 \" ouvert.");
					
					p.openInventory(createPlayerInventory(target));					
				}
			} else if (item.equals(generatePotItem(PotionType.INSTANT_HEAL, ChatColor.LIGHT_PURPLE+"Heal"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.sendMessage(ChatColor.BLUE+"Vie de "+ChatColor.WHITE+targetName+ChatColor.BLUE+" réstaurée.");
					
					target.setHealth(target.getMaxHealth());
					p.closeInventory();
				}
			} else if (item.equals(generateItem(Material.COOKED_BEEF, ChatColor.GREEN+"Feed"))) {
				if (target == null) {
					p.sendMessage(ChatColor.BLUE+"Impossible de localiser le joueur: \""+ChatColor.WHITE+targetName+
							ChatColor.BLUE+"\" .");
				} else {
					p.sendMessage(ChatColor.BLUE+"Nourriture de "+ChatColor.WHITE+targetName+ChatColor.BLUE+" réstaurée.");
					
					target.setFoodLevel(20);
					p.closeInventory();
				}
			} else if (item.equals(generateItem(Material.IRON_CHESTPLATE, ChatColor.GRAY+"Demander une vérif"))) {
				p.sendMessage(ChatColor.BLUE+"Les vérifs sont pour le moment interdite.");
				p.closeInventory();
			}
			e.setCancelled(true);
		} else if (i.getName().equals(ChatColor.GRAY+"EnderChest")) {
			e.setCancelled(true);
		} else if (i.getName().equals(ChatColor.GOLD+"Inventaire")) {
			e.setCancelled(true);
		} else if (i.getName().equals(ChatColor.RED+"Menu de ban")) {
			String targetName = playerMenu.get(p);
			Player target = Bukkit.getPlayer(targetName);
			if (item.equals(adminToolsLogo("_goldocelot_ and Glowstoner"))) {
				p.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"Merci d'utiliser le plugin \"AdminTools\" développé par Goldocelot et Glowstoner.");
			} else if (item.equals(generateItem(Material.PAPER, ChatColor.AQUA+"Retour"))) {
				p.closeInventory();
				Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN+"PlayerMenu");
				EventsAT.createPlayerMenu(target, inv);
				p.openInventory(inv);
			} else if (item.equals(generateItem(Material.REDSTONE_ORE, ChatColor.RED+"Xray"))) {
				p.closeInventory();
				//Ban pour Xray
			} else if (item.equals(generateItem(Material.ANVIL, ChatColor.DARK_GRAY+"Anti-kb"))) {
				p.closeInventory();
				//Ban pour Anti-kb
			} else if (item.equals(generateEgg((short) 93, ChatColor.WHITE+"Glide"))) {
				p.closeInventory();
				//Ban pour Glide
			} else if (item.equals(generateEgg((short) 52, ChatColor.DARK_RED+"Spider"))) {
				p.closeInventory();
				//Ban pour Spider
			} else if (item.equals(generateItem(Material.GLASS, ChatColor.WHITE+"HitBox"))) {
				p.closeInventory();
				//Ban pour HitBox
			} else if (item.equals(generateItem(Material.ARROW, ChatColor.YELLOW+"AimBot/AimAssist"))) {
				p.closeInventory();
				//Ban pour AimBot/AimAssist
			} else if (item.equals(generateItem(Material.BREAD, ChatColor.GOLD+"FastEat"))) {
				p.closeInventory();
				//Ban pour FastEat
			} else if (item.equals(generateItem(Material.SLIME_BALL, ChatColor.GREEN+"Autre"))) {
				p.closeInventory();
				//Ban pour Autre
			} else if (item.equals(generateItem(Material.FEATHER, ChatColor.WHITE+"Fly"))) {
				p.closeInventory();
				//Ban pour Fly
			} else if (item.equals(generateItem(Material.DIAMOND_PICKAXE, ChatColor.DARK_BLUE+"FastMining"))) {
				p.closeInventory();
				//Ban pour FastMining
			} else if (item.equals(generateItem(Material.WOOD, ChatColor.GREEN+"FastPlace"))) {
				p.closeInventory();
				//Ban pour FastPlace
			} else if (item.equals(generateItem(Material.DIAMOND_BOOTS, ChatColor.LIGHT_PURPLE+"NoFall"))) {
				p.closeInventory();
				//Ban pour NoFall
			} else if (item.equals(generateItem(Material.LEATHER_BOOTS, ChatColor.AQUA+"Strafe"))) {
				p.closeInventory();
				//Ban pour Strafe
			} else if (item.equals(generateItem(Material.WATER_BUCKET, ChatColor.BLUE+"Jesus"))) {
				p.closeInventory();
				//Ban pour Jesus
			} else if (item.equals(generateItem(Material.DIAMOND_SWORD, ChatColor.AQUA+"ForceField"))) {
				p.closeInventory();
				//Ban pour ForceField
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHit (EntityDamageByEntityEvent e)	{
		final Entity targetE = e.getEntity();
		final Entity pE = e.getDamager();
	
		if (targetE.getType().equals(EntityType.PLAYER) && pE.getType().equals(EntityType.PLAYER)) {
			Player target = (Player) targetE;
			Player p = (Player) pE;
			ItemStack adminStick = new ItemStack(Material.STICK);
			ItemMeta adminStickMeta = adminStick.getItemMeta();
			adminStickMeta.setDisplayName(ChatColor.DARK_BLUE+"AdminStick");
			adminStick.setItemMeta(adminStickMeta);
		
			if(!p.getItemInHand().equals(adminStick)) return;
		
			if(playerMenu.containsKey(p)) {
				playerMenu.remove(p, playerMenu.get(p));
			}
			
			playerMenu.put(p, target.getName());
			
			Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN+"PlayerMenu");
			EventsAT.createPlayerMenu(target, inv);
			p.openInventory(inv);
			
			e.setCancelled(true);
		}
	}
	
	public static ItemStack generateItem(Material mat, String name) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static ItemStack generatePotItem(PotionType pt, String name) {
		Potion pot = new Potion(pt, 1);
		ItemStack potion = pot.toItemStack(1);
		ItemMeta meta = potion.getItemMeta();
		meta.setDisplayName(name);
		potion.setItemMeta(meta);
		
		return potion;
	}

	public static ItemStack generateEgg(short dataid, String name) {
		ItemStack item = new ItemStack(Material.MONSTER_EGG, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setDurability(dataid);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack adminToolsLogo(String crea) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§8");
		lore.add(ChatColor.GOLD+""+ChatColor.ITALIC+"By "+crea);
		
		ItemStack logo = new ItemStack(Material.GOLD_SWORD, 1);
		ItemMeta metaLogo = logo.getItemMeta();
		metaLogo.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"Admin Tools");
		metaLogo.setLore(lore);
		logo.setItemMeta(metaLogo);
		
		return logo;
	}
	
	
	public static ItemStack generateSkull(String owner)  {
	    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
	    SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
	    skullMeta.setOwner(owner);
	    skullMeta.setDisplayName("§d" + owner);
	    skull.setItemMeta(skullMeta);
	    
	    return skull;
	  }
	
	public static void createInventory(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.WHITE+"AdminList");
		
		for(int i = 0 ; i < 45; i++) {
			Player target;
			
			try {
				target = pCo.get(0);
				
				inv.setItem(i, generateSkull(target.getName()));
				
				pCo.remove(target);
			}catch(IndexOutOfBoundsException ex) {
				break;
			}
		}
		
		int pMod = pCo.size() % 45;
		int pa = pCo.size() / 45;
		if (pMod != 0) {
			pa++;
		}
		if(pa == 0 && page == 1) {
			inv.setItem(46, generateItem(Material.HOPPER, ChatColor.DARK_GREEN+"Rechercher"));
			inv.setItem(49, adminToolsLogo("Glowstoner"));
			
			p.openInventory(inv);
		}else if(page == 1) {
			inv.setItem(53, generateItem(Material.PAPER, ChatColor.AQUA+"Suivant"));
			inv.setItem(46, generateItem(Material.HOPPER, ChatColor.DARK_GREEN+"Rechercher"));
			inv.setItem(49, adminToolsLogo("Glowstoner"));
			
			p.openInventory(inv);
		}else if(page == pa) {
			inv.setItem(45, generateItem(Material.PAPER, ChatColor.AQUA+"Précédent"));
			inv.setItem(46, generateItem(Material.HOPPER, ChatColor.DARK_GREEN+"Rechercher"));
			inv.setItem(49, adminToolsLogo("Glowstoner"));
			p.openInventory(inv);
		}else {
			inv.setItem(53, generateItem(Material.PAPER, ChatColor.AQUA+"Suivant"));
			inv.setItem(46, generateItem(Material.HOPPER, ChatColor.DARK_GREEN+"Rechercher"));
			inv.setItem(49, adminToolsLogo("Glowstoner"));
			inv.setItem(45, generateItem(Material.PAPER, ChatColor.AQUA+"Précédent"));
			
			p.openInventory(inv);
		}
	}
	
	public static void initPlayers() {
		for(Player online : Bukkit.getOnlinePlayers()) {
			pCo.add(online);
		}
	}
	
	public static Inventory createEnderChest (Player p) {
		
		Inventory enderChest = Bukkit.createInventory(null, 27, ChatColor.GRAY+"EnderChest");
		
		for (int i = 0 ; i < 27 ; i++) {
			enderChest.setItem(i, p.getEnderChest().getItem(i));
		}
		
		return enderChest;
		
	}
	
	public static Inventory createPlayerInventory (Player p) {
		
		Inventory chest = Bukkit.createInventory(null, 45, ChatColor.GOLD+"Inventaire");
		
		for (int i = 0 ; i < 40 ; i++) {
			chest.setItem(i, p.getInventory().getItem(i));
		}
		
		return chest;
	}
	
	public static  Inventory createPlayerMenu(Player p, Inventory inv) {
		inv.setItem(0, generateSkull(p.getName()));
		inv.setItem(4, adminToolsLogo("_goldocelot_ and Glowstoner"));
		inv.setItem(8, generateItem(Material.PAPER, ChatColor.AQUA+"Retour"));
		inv.setItem(10, generateItem(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE+"Go to"));
		inv.setItem(12, generateItem(Material.PACKED_ICE, ChatColor.GRAY+"Freeze"));
		inv.setItem(14, generateItem(Material.REDSTONE, ChatColor.DARK_RED+"Kill"));
		inv.setItem(16, generateItem(Material.BUCKET, ChatColor.GRAY+"Clear"));
		inv.setItem(27, generateItem(Material.JUKEBOX, ChatColor.RED+"Mute"));
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
		ItemMeta skullM = skull.getItemMeta();
		skullM.setDisplayName(ChatColor.RED+"Kick (Anti-Jeu)");
		skull.setItemMeta(skullM);
		inv.setItem(29, skull);
		inv.setItem(31, generateItem(Material.BARRIER, ChatColor.RED+""+ChatColor.BOLD+"Ban"));
		inv.setItem(33, generateItem(Material.ENDER_CHEST, ChatColor.GRAY+"Inventaire de l'ender chest"));
		inv.setItem(35, generateItem(Material.CHEST, ChatColor.GOLD+"Inventaire"));
		inv.setItem(46, generatePotItem(PotionType.INSTANT_HEAL, ChatColor.LIGHT_PURPLE+"Heal"));
		inv.setItem(48, generateItem(Material.COOKED_BEEF, ChatColor.GREEN+"Feed"));
		inv.setItem(50, generateItem(Material.IRON_CHESTPLATE, ChatColor.GRAY+"Demander une vérif"));	
		inv.setItem(52, generateItem(Material.GRASS, ChatColor.GREEN+"Monde: "+p.getWorld().getName()));
		return inv;
	}	
	
	public static  Inventory createBanMenu(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED+"Menu de ban");
		
		inv.setItem(0, generateSkull(p.getName()));
		inv.setItem(4, adminToolsLogo("_goldocelot_ and Glowstoner"));
		inv.setItem(8, generateItem(Material.PAPER, ChatColor.AQUA+"Retour"));
		inv.setItem(11, generateItem(Material.REDSTONE_ORE, ChatColor.RED+"Xray"));
		inv.setItem(15, generateItem(Material.ANVIL, ChatColor.DARK_GRAY+"Anti-kb"));
		inv.setItem(19, generateEgg((short) 93, ChatColor.WHITE+"Glide"));
		inv.setItem(47, generateItem(Material.WATER_BUCKET, ChatColor.BLUE+"Jesus"));
		inv.setItem(21, generateEgg((short) 52, ChatColor.DARK_RED+"Spider"));
		inv.setItem(23, generateItem(Material.GLASS, ChatColor.WHITE+"HitBox"));
		inv.setItem(51, generateItem(Material.DIAMOND_SWORD, ChatColor.AQUA+"ForceField"));
		inv.setItem(25, generateItem(Material.ARROW, ChatColor.YELLOW+"AimBot/AimAssist"));
		inv.setItem(27, generateItem(Material.BREAD, ChatColor.GOLD+"FastEat"));
		inv.setItem(37, generateItem(Material.DIAMOND_PICKAXE, ChatColor.DARK_BLUE+"FastMining"));
		inv.setItem(39, generateItem(Material.WOOD, ChatColor.GREEN+"FastPlace"));
		inv.setItem(41, generateItem(Material.DIAMOND_BOOTS, ChatColor.LIGHT_PURPLE+"NoFall"));
		inv.setItem(35, generateItem(Material.FEATHER, ChatColor.WHITE+"Fly"));
		inv.setItem(43, generateItem(Material.LEATHER_BOOTS, ChatColor.AQUA+"Strafe"));
		inv.setItem(31, generateItem(Material.SLIME_BALL, ChatColor.GREEN+"Autre"));
		return inv;
	}	
}