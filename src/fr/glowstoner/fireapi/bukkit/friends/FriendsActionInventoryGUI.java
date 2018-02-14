package fr.glowstoner.fireapi.bukkit.friends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Lists;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;

public class FriendsActionInventoryGUI {
	
	private List<String> friends = new ArrayList<>();
	private List<String> connected = new ArrayList<>();
	
	private Inventory inv;
	private FirePlayer fp;
	private String name;
	
	public FriendsActionInventoryGUI(String name, List<String> friends, List<String> connected) {
		this.setConnected(connected);
		this.setFriends(friends);
		
		this.setName(name);
	}
	
	public void initPlayer(FireAPI api) {
		this.fp = new FirePlayer(api.getBukkitPlugin().getServer().getPlayer(this.name), api);
	}
	
	//membre - 8 amis max
	//fire - 16
	//ultra - 24
	//ultimate - 32
	// youtube - 40
	// staff - max (une page entière)
	public Inventory generateInventory() {
		if(fp.hasRankAndSup(Rank.GUIDE)) {
			this.inv = Bukkit.createInventory(null, 54, "§6Amis");
			
			if(this.friends.size() == 0) {
				for(int i = 0 ; i < this.inv.getSize() ; i++) {
					this.inv.setItem(i, this.getBorderItem());
				}
			}else {
				for(int i = 0 ; i <= 5 ; i++) {
					this.inv.setItem(i * 9, this.getBorderItem());
				}
				
				int l = 0;
				
				for(List<String> sub : this.getFriendsSplitListed()) {
					int i = 1;
					
					for(String substr : sub) {
						this.inv.setItem((l * 9) + i, this.formHead(substr));
						
						i++;
					}
					
					l++;
				}
			}
		}else if(fp.hasRankAndSup(Rank.YOUTUBER)) {
			this.inv = Bukkit.createInventory(null, 45, "§6Amis");
			
			if(this.friends.size() == 0) {
				for(int i = 0 ; i < this.inv.getSize() ; i++) {
					this.inv.setItem(i, this.getBorderItem());
				}
			}else {
				for(int i = 0 ; i <= 4 ; i++) {
					this.inv.setItem(i * 9, this.getBorderItem());
				}
				
				int l = 0;
				
				for(List<String> sub : this.getFriendsSplitListed()) {
					int i = 1;
					
					for(String substr : sub) {
						this.inv.setItem((l * 9) + i, this.formHead(substr));
						
						i++;
					}
					
					l++;
				}
			}
		}else if(fp.hasRankAndSup(Rank.ULTIMATE)) {
			this.inv = Bukkit.createInventory(null, 36, "§6Amis");
			

			if(this.friends.size() == 0) {
				for(int i = 0 ; i < this.inv.getSize() ; i++) {
					this.inv.setItem(i, this.getBorderItem());
				}
			}else {
				for(int i = 0 ; i <= 3 ; i++) {
					this.inv.setItem(i * 9, this.getBorderItem());
				}
				
				int l = 0;
				
				for(List<String> sub : this.getFriendsSplitListed()) {
					int i = 1;
					
					for(String substr : sub) {
						this.inv.setItem((l * 9) + i, this.formHead(substr));
						
						i++;
					}
					
					l++;
				}
			}
		}else if(fp.hasRankAndSup(Rank.ULTRA)) {
			this.inv = Bukkit.createInventory(null, 27, "§6Amis");
			
			if(this.friends.size() == 0) {
				for(int i = 0 ; i < this.inv.getSize() ; i++) {
					this.inv.setItem(i, this.getBorderItem());
				}
			}else {
				for(int i = 0 ; i <= 2 ; i++) {
					this.inv.setItem(i * 9, this.getBorderItem());
				}
				
				int l = 0;
				
				for(List<String> sub : this.getFriendsSplitListed()) {
					int i = 1;
					
					for(String substr : sub) {
						this.inv.setItem((l * 9) + i, this.formHead(substr));
						
						i++;
					}
					
					l++;
				}
			}
		}else if(fp.hasRankAndSup(Rank.FIRE)) {
			this.inv = Bukkit.createInventory(null, 18, "§6Amis");
			
			if(this.friends.size() == 0) {
				for(int i = 0 ; i < this.inv.getSize() ; i++) {
					this.inv.setItem(i, this.getBorderItem());
				}
			}else {
				for(int i = 0 ; i <= 1 ; i++) {
					this.inv.setItem(i * 9, this.getBorderItem());
				}
				
				int l = 0;
				
				for(List<String> sub : this.getFriendsSplitListed()) {
					int i = 1;
					
					for(String substr : sub) {
						this.inv.setItem((l * 9) + i, this.formHead(substr));
						
						i++;
					}
					
					l++;
				}
			}
		}else {
			this.inv = Bukkit.createInventory(null, 9, "§6Amis");
			
			if(this.friends.size() == 0) {
				for(int i = 0 ; i < this.inv.getSize() ; i++) {
					this.inv.setItem(i, this.getBorderItem());
				}
			}else {
				this.inv.setItem(0, this.getBorderItem());
				
				int l = 0;
				
				for(List<String> sub : this.getFriendsSplitListed()) {
					int i = 1;
					
					for(String substr : sub) {
						this.inv.setItem((l * 9) + i, this.formHead(substr));
						
						i++;
					}
					
					l++;
				}
			}
		}
		
		return this.inv;
	}
	
	public void openInventory() {
		this.fp.getBukkitPlayer().openInventory(this.inv);
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public List<String> getConnected() {
		return connected;
	}

	public void setConnected(List<String> connected) {
		this.connected = connected;
	}
	
	public FirePlayer getFirePlayer() {
		return this.fp;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//amis.size ne peut dépasser 48;
	public List<List<String>> getFriendsSplitListed() {
		List<String> friendsconn = new ArrayList<>();
		List<String> friendsnotconn = this.friends;
		List<String> tried = new ArrayList<>();
		
		for(String f : this.friends) {
			if(this.connected.contains(f)) {
				friendsconn.add(f);
			}
		}
		
		friendsnotconn.removeAll(friendsconn);
		
		tried.addAll(friendsconn);
		tried.addAll(friendsnotconn);
		
		return Lists.partition(tried, 8);
	}
	
	public ItemStack formHead(String owner) {
		ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta m = (SkullMeta) i.getItemMeta();
		m.setDisplayName((this.connected.contains(owner)) ? "§2"+owner : "§8"+owner);
		
		m.setLore((this.connected.contains(owner) ? Arrays.asList("§a§oConnecté") :
			Arrays.asList("§c§oDéconnecté")));
		
		m.setOwner(owner);
		i.setItemMeta(m);
		
		return i;
	}
	
	public ItemStack getBorderItem() {
		ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) ((this.friends.size() != 0) ? 4 : 14));
		ItemMeta m = i.getItemMeta();
		m.setDisplayName((this.friends.size() != 0) ? "§eVous avez §6"+this.getFriends().size()
				+" §eamis" : "§cVous n'avez aucun ami :'(");
		i.setItemMeta(m);
		
		return i;
	}
}
