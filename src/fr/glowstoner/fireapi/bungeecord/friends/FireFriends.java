package fr.glowstoner.fireapi.bungeecord.friends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.glowstoner.fireapi.bungeecord.BungeeMain;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class FireFriends {
	
	private final String pre = "§6[§eAmis§6]§r ";
	
	//sender - receiver
	private Map<ProxiedPlayer, ProxiedPlayer> cant = new HashMap<>();
	
	//receiver - sender
	private Map<ProxiedPlayer, ProxiedPlayer> outdated = new HashMap<>();
	
	private BungeeMain instance;
	private File file;
	private boolean enable;
	
	public FireFriends(BungeeMain plugin) {
		this.instance = plugin;
	}
	
	public void initFolder() {
		if(!this.instance.getDataFolder().exists()) {
			this.instance.getDataFolder().mkdirs();
		}
	}

	public void loadConfiguration() throws IOException {
		File file = new File(this.instance.getDataFolder(), "friends.yml");
		
		this.file = file;
		
		if(!file.exists()) {
			file.createNewFile();
			
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
			config.set("enable", true);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, this.file);
			
			this.enable = true;
		}else {
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
			this.enable = config.getBoolean("enable");
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, this.file);
		}
	}
	
	public void addFriend(ProxiedPlayer p1, ProxiedPlayer p2) {
		if(this.enable) {
			try {
				Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
				
				if(!config.contains("data."+p1.getName())) {
					List<String> list = new ArrayList<>();
					list.add(p2.getName());
					
					config.set("data."+p1.getName(), list);
				}else {
					List<String> list = config.getStringList("data."+p1.getName());
					list.add(p2.getName());
					
					config.set("data."+p1.getName(), list);
				}
				
				if(!config.contains("data."+p2.getName())) {
					List<String> list = new ArrayList<>();
					list.add(p1.getName());
					
					config.set("data."+p2.getName(), list);
				}else {
					List<String> list = config.getStringList("data."+p2.getName());
					list.add(p1.getName());
					
					config.set("data."+p2.getName(), list);
				}
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, this.file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeFriend(ProxiedPlayer p1, String p2) throws IOException {
		if(this.enable) {
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
			
			if(config.contains("data."+p1.getName()) && config.contains("data."+p2)) {
				List<String> list = config.getStringList("data."+p1.getName());
				
				if(list.contains(p2)) {
					list.remove(p2);
				}
				
				if(list.size() == 0) {
					config.set("data."+p1.getName(), null);
				}else {
					config.set("data."+p1.getName(), list);
				}
				
				List<String> list2 = config.getStringList("data."+p2);
				
				if(list2.contains(p1.getName())) {
					list2.remove(p1.getName());
				}
				
				if(list2.size() == 0) {
					config.set("data."+p2, null);
				}else {
					config.set("data."+p2, list2);
				}
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, this.file);
				
				p1.sendMessage(new TextComponent(this.pre+"§aVous n'êtes plus ami avec "+p2));
			}else {
				p1.sendMessage(new TextComponent(this.pre+"§cErreur, vous n'êtes pas ami avec "+p2));
			}
		}
	}
	
	public void sendFriendsMessageResquest(ProxiedPlayer pp, String name) {
		TextComponent accept = new TextComponent("§a§l✔ Accepter§r ");
		accept.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/amis accept "+name));
		accept.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("§aCliquez ici pour accepter sa demande").create()));
		
		TextComponent base = new TextComponent(this.pre+"§e"+name+"§r vous demande en ami ! ");
		base.addExtra(accept);
		
		pp.sendMessage(base);
	}
	
	public void sendRequest(ProxiedPlayer sender, ProxiedPlayer receiver) {
		if(this.cant.containsKey(sender)) {
			if(this.cant.get(sender).equals(receiver)) {
				sender.sendMessage(new TextComponent(this.pre+
						"§cAttendez un peu avant d'envoyer une nouvelle §cdemande d'amis à §c"+receiver.getName()+"!"));
				
				return;
			}
		}
		
		this.outdated.put(receiver, sender);
		this.cant.put(sender, receiver);
		
		sendFriendsMessageResquest(receiver, sender.getName());
		
		this.instance.getProxy().getScheduler().schedule(this.instance, new Runnable() {

			@Override
			public void run() {
				cant.remove(sender);
				outdated.remove(receiver);
			}
			
		}, 5L, TimeUnit.MINUTES);
	}
	
	public void acceptRequest(ProxiedPlayer receiver, ProxiedPlayer sender) throws IOException {
		if(this.outdated.containsKey(receiver)) {
			if(this.outdated.get(receiver).equals(sender)) {
				if(!isAlreadyFriends(receiver, sender)) {
					//accepted
					
					receiver.sendMessage(new TextComponent
							(this.pre+"§aVous êtes maintenant ami avec §2"+sender.getName()+"§a !"));
					sender.sendMessage(new TextComponent
							(this.pre+"§aVous êtes maintenant ami avec §2"+receiver.getName()+"§a !"));
					
					addFriend(sender, receiver);
					
					return;
				}else {
					receiver.sendMessage(new TextComponent(this.pre+"§cErreur, vous êtes déjà "
							+ "ami avec "+sender.getName()+" !"));
					return;
				}
			}
		}
		
		receiver.sendMessage(new TextComponent(
				this.pre+"§cErreur, vous n'avez reçu aucune demande récente de ce §cjoueur !"));
	}
	
	public boolean isAlreadyFriends(ProxiedPlayer p1, ProxiedPlayer p2) throws IOException {
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		if(!config.contains("data."+p1.getName())) {
			return false;
		}
		
		if(config.getStringList("data."+p1.getName()).contains(p2.getName())) {
			return true;
		}else {
			return false;
		}
	}
	
	public void sendGeneratedList(ProxiedPlayer pp) throws IOException {
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		if(hasFriends(pp)) {
			if(getOnlineFriends(pp).size() == 0) {
				pp.sendMessage(new TextComponent(this.pre+"Vous n'avez §caucun§r ami connecté sur le serveur"));
			}else if(getOnlineFriends(pp).size() == 1){
				pp.sendMessage(new TextComponent(this.pre+"Vous avez §eun§r ami connecté sur le serveur"));
			}else {
				pp.sendMessage(new TextComponent(this.pre+"Vous avez §e"+getOnlineFriends(pp).size()+
						"§r amis connectés sur le serveur"));
			}
		}else {
			pp.sendMessage(new TextComponent(this.pre+"§cVous n'avez pas d'amis :("));
			
			return;
		}
		
		List<String> list = config.getStringList("data."+pp.getName());
		
		for(ProxiedPlayer connected : getOnlineFriends(pp)){
			pp.sendMessage(new TextComponent(this.pre+
					"§a"+connected.getName()+"§r est actuellement sur le §e"+connected.getServer().getInfo().getName()));
			
			list.remove(connected.getName());
		}
		
		for(String names : list) {
			pp.sendMessage(new TextComponent(this.pre+"§8"+names+"§r est actuellement §chors-ligne"));
		}
	}
	
	public void sendGeneratedListOther(CommandSender sender, String name) throws IOException {
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		if(!this.instance.getSQL().hasFireAccount(name)) {
			sender.sendMessage(new TextComponent(this.pre+"§cErreur ce joueur n'existe pas !"));
			return;
		}
		
		if(hasFriends(name)) {
			if(getOnlineFriends(name).size() == 0) {
				sender.sendMessage(new TextComponent(this.pre+name+" n'a §caucun§r ami connecté sur le serveur"));
			}else if(getOnlineFriends(name).size() == 1){
				sender.sendMessage(new TextComponent(this.pre+name+" a §eun§r ami connecté sur le serveur"));
			}else {
				sender.sendMessage(new TextComponent(this.pre+name+" a §e"+getOnlineFriends(name).size()+
						"§r amis connectés sur le serveur"));
			}
		}else {
			sender.sendMessage(new TextComponent(this.pre+"§c"+name+" n'a pas d'amis :("));
			
			return;
		}
		
		List<String> list = config.getStringList("data."+name);
		
		for(ProxiedPlayer connected : getOnlineFriends(name)){
			sender.sendMessage(new TextComponent(this.pre+
					"§a"+connected.getName()+"§r est actuellement sur le §e"+connected.getServer().getInfo().getName()));
			
			list.remove(connected.getName());
		}
		
		for(String names : list) {
			sender.sendMessage(new TextComponent(this.pre+"§8"+names+"§r est actuellement §chors-ligne"));
		}
	}
	
	public List<ProxiedPlayer> getOnlineFriends(ProxiedPlayer pp) throws IOException{
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		List<ProxiedPlayer> connected = new ArrayList<>();
		List<String> list = config.getStringList("data."+pp.getName());
		
		for(String names : list) {
			try {
				ProxiedPlayer player = this.instance.getProxy().getPlayer(names);
				
				if(player != null) {
					connected.add(player);
				}
			}catch(Exception e) {
				continue;
			}
		}
		
		return connected;
	}
	
	public List<String> getAllFriends(String name) throws IOException {
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		return config.getStringList("data."+name);
	}
	
	public List<ProxiedPlayer> getOnlineFriends(String pp) throws IOException{
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		List<ProxiedPlayer> connected = new ArrayList<>();
		List<String> list = config.getStringList("data."+pp);
		
		for(String names : list) {
			try {
				ProxiedPlayer player = this.instance.getProxy().getPlayer(names);
				
				if(player != null) {
					connected.add(player);
				}
			}catch(Exception e) {
				continue;
			}
		}
		
		return connected;
	}
	
	public void sendFriendsGUI(ConnectionHandler c, String name) {
		
	}
	
	public boolean hasFriends(ProxiedPlayer pp) throws IOException {
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		return config.contains("data."+pp.getName());
	}
	
	public boolean hasFriends(String pp) throws IOException {
		Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
		
		return config.contains("data."+pp);
	}
	
	public void sendFriendsAlert(ProxiedPlayer pp) {
		try {
			if(this.hasFriends(pp)) {
				if(this.getOnlineFriends(pp).size() == 0) {
					pp.sendMessage(new TextComponent(this.pre+"Vous n'avez §caucun§r ami connecté sur le serveur"));
				}else if(this.getOnlineFriends(pp).size() == 1){
					pp.sendMessage(new TextComponent(this.pre+"Vous avez §eun§r ami connecté sur le serveur"));
				}else {
					pp.sendMessage(new TextComponent(this.pre+"Vous avez §e"+
						this.getOnlineFriends(pp).size()+"§r amis connectés sur le serveur"));
				}
				
				for(ProxiedPlayer players : this.getOnlineFriends(pp)) {
					players.sendMessage(new TextComponent(this.pre+"§a+ §r"+pp.getName()+" s'est §aconnecté"));
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean canHaveMoreFriends(FirePlayer fp) throws IOException {
		if(fp.hasRankAndSup(Rank.GUIDE)) {
			if(this.getAllFriends(fp.getName()).size() == 48) {
				return false;
			}
		}else if(fp.hasRankAndSup(Rank.YOUTUBER)) {
			if(this.getAllFriends(fp.getName()).size() == 40) {
				return false;
			}
		}else if(fp.hasRankAndSup(Rank.ULTIMATE)) {
			if(this.getAllFriends(fp.getName()).size() == 32) {
				return false;
			}
		}else if(fp.hasRankAndSup(Rank.ULTRA)) {
			if(this.getAllFriends(fp.getName()).size() == 24) {
				return false;
			}
		}else if(fp.hasRankAndSup(Rank.FIRE)) {
			if(this.getAllFriends(fp.getName()).size() == 16) {
				return false;
			}
		}else {
			if(this.getAllFriends(fp.getName()).size() == 8) {
				return false;
			}
		}
		
		return true;
	}
}