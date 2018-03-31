package fr.glowstoner.fireapi.bukkit.id;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.glowstoner.fireapi.FireAPI;
import lombok.Getter;

public class FireBukkitID {
	
	@Getter private FileConfiguration config;
	@Getter private String ID;
	
	private FireAPI api;
	
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
			
			this.config.set("fireID-bigbrother", "default-spigot");
			
			this.ID = this.config.getString("fireID-bigbrother");
			
			this.config.save(file);
		}else {
			this.config = new YamlConfiguration();
			
			this.config.load(file);
			
			this.ID = this.config.getString("fireID-bigbrother");
		}
	}
}
