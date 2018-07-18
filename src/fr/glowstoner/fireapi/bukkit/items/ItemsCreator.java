package fr.glowstoner.fireapi.bukkit.items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemsCreator {

	private ItemStack baseItem;
	private Material mat;
	private String name;
	private ArrayList<String> lore;
	private int amount;
	
	public ItemsCreator(Material mat, int amount, String name, ArrayList<String> lore) {
		this.mat = mat;
		this.amount = amount;
		this.name = name;
		this.lore = lore;
	}
	
	public ItemStack create() {
		ItemStack item = new ItemStack(baseItem);
		setAmount(item);
		setMaterial(item);
		setName(item);
		setLore(item);
		return item;
	}
	
	public void setAmount(ItemStack item) {
		item.setAmount(this.amount);
	}
	
	public void setMaterial(ItemStack item) {
		if(this.mat.equals(null)) item.setType(Material.STONE);
		else item.setType(this.mat);
		
	}
	
	public void setName(ItemStack item) {
		if(!this.name.equals(null)) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			item.setItemMeta(meta);
		}
	}
	
	public void setLore(ItemStack item) {
		if(this.lore.size() != 0) {
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}
}