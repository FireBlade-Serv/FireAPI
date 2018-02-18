package fr.glowstoner.fireapi.wl;

import java.io.File;
import java.io.IOException;

import fr.glowstoner.fireapi.FireAPI;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class FireWL {
	
	private final String pre = "§6[§eWhiteList§6]§r ";
	
	private FireAPI api;
	private boolean enable;
	private Plugin plugin;
	
	public FireWL(FireAPI api) {
		this.api = api;
		this.plugin = this.api.getBungeePlugin();
	}

	public void loadConfiguration() {
		try {
	        if (!plugin.getDataFolder().exists()) {
	            plugin.getDataFolder().mkdirs();
	        }
	        
	        File file = new File(plugin.getDataFolder(), "whitelist.yml");
	        
	        if (!file.exists()) {
	        	file.createNewFile();
				
				Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				config.set("enable", true);
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
				
				if(config.getBoolean("enable") == true){
					enable = true;
				}else if(config.getBoolean("enable") == false){
					enable = false;
				}
	        } else {
	        	Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
				
				if(config.getBoolean("enable") == true){
					enable = true;
				}else if(config.getBoolean("enable") == false){
					enable = false;
				}
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void setDefault(String name, String ip) throws IOException {
		 File file = new File(plugin.getDataFolder(), "whitelist.yml");
		 
		 Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		 
		 if(config.getBoolean("players."+name+".accepted") == true && config.contains("players."+name+".ip")) {
			return;
		 }
		 
		 if(config.getBoolean("players."+name+".accepted") == true && !config.contains("players."+name+".ip")) {
			 config.set("players."+name+".ip", ip);
				 
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
			 
			return;
		 }
		 
		 config.set("players."+name+".accepted", false);
		 config.set("players."+name+".ip", ip);
		
		 ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
	}
	
	public boolean exists(String name) throws IOException {
		File file = new File(plugin.getDataFolder(), "whitelist.yml");
		 
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		 
		return config.contains("players."+name);
	}
	
	public boolean containsIP(String name) throws IOException {
		if(this.exists(name)) {
			File file = new File(plugin.getDataFolder(), "whitelist.yml");
			 
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			
			return config.contains("players."+name+".ip");
		}
		
		return false;
	}
	
	public String getIP(String name) throws IOException {
		if(this.containsIP(name)) {
			File file = new File(plugin.getDataFolder(), "whitelist.yml");
			 
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			
			return config.getString("players."+name+".ip");
		}
		
		return null;
	}
	
	public boolean isEnable() {
		return this.enable;
	}
	
	public void setEnable(boolean enable){
		this.enable = enable;
	}
	
	public String getPrefix() {
		return this.pre;
	}
}
