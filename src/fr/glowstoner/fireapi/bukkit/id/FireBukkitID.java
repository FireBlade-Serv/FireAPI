package fr.glowstoner.fireapi.bukkit.id;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.glowstoner.fireapi.FireAPI;

public class FireBukkitID {
	
	private FireAPI api;
	private FileConfiguration config;
	private String id;
	
	public FireBukkitID(FireAPI api) {
		this.api = api;
	}

	public void loadConfiguration() throws IOException, InvalidConfigurationException {
		File file = new File(this.api.getBukkitPlugin().getDataFolder(), "fireID.yml");
		
		if(!this.api.getBukkitPlugin().getDataFolder().exists()) {
			this.api.getBukkitPlugin().getDataFolder().mkdir();
		}
		
		if(!file.exists()) {
			file.createNewFile();
			
			this.config = new YamlConfiguration();
			
			this.config.load(file);
			
			this.config.set("fireID-gediminas", "default-spigot");
			
			this.id = this.config.getString("fireID-gediminas");
			
			this.config.save(file);
		}else {
			this.config = new YamlConfiguration();
			
			this.config.load(file);
			
			this.id = this.config.getString("fireID-gediminas");
		}
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public String getID() {
		return this.id;
	}
}
