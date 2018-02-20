package fr.glowstoner.fireapi.bungeecord.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bungeecord.friends.FireFriends;
import fr.glowstoner.fireapi.gediminas.console.check.GediminasConnectionCheckListener;
import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;
import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Events implements Listener, GediminasConnectionCheckListener {
	
	private final String pre = "§6[§eAmis§6]§r ";
	
	private FireFriends friends;
	private FireAPI instance;
	private List<ProxiedPlayer> stillconnected;
	private Client c;
	
	public Events(FireAPI api) {
		this.instance = api;
		this.friends = this.instance.getFriends();
		this.stillconnected = new ArrayList<>();
		this.c = this.instance.getClient();
	}

	@EventHandler
	public void onLogin(PostLoginEvent e) {
		final ProxiedPlayer pp = e.getPlayer();
		
		pp.setTabHeader(new TextComponent("§6Fireblade-serv"), new TextComponent("§eplay.fireblade-serv.eu"));
		
		if(!this.instance.getSQL().hasFireAccount(pp.getName())) {
			this.instance.getSQL().createFireAccount(pp);
		}
		
		PacketSpyAction ps = new PacketSpyAction(pp.getName(), pp.getAddress().getAddress().getHostAddress(),
				"Connection sur le proxy principal.", SpyAction.PLAYER_JOIN);
		
		ps.setDateToNow();
		
		try {
			this.c.sendPacket(ps);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		this.stillconnected.add(pp);
		
		this.instance.getBungeePlugin().getProxy().getScheduler().schedule
			(this.instance.getBungeePlugin(), new Runnable() {
				
				@Override
				public void run() {
					if(pp != null) {
						if(pp.isConnected() && stillconnected.contains(pp)) {
							pp.disconnect(new TextComponent
									("§cVous mettez trop de temps pour vous connecter !"));
						}
					}
				}
				
			}, 15L, TimeUnit.SECONDS);
		
		if(this.instance.getSQL().getCryptPassword(pp.getName()).equals("default-not-set")) {
			//not registered
			
			pp.sendMessage(new TextComponent(this.instance.getAuthentification().getRegisterMessage()));
			this.instance.getAuthentification().getRegisterTitle().send(pp);
		}else {
			//registered
			
			pp.sendMessage(new TextComponent(this.instance.getAuthentification().getLoginMessage()));
			this.instance.getAuthentification().getLoginTitle().send(pp);
		}
	}
	
	@EventHandler
	public void onLoginSuccefull(LoginSuccessEvent e) {
		ProxiedPlayer pp = e.getPlayer();
		
		this.stillconnected.remove(pp);
		
		pp.connect(this.instance.getBungeePlugin().getProxy().getServerInfo("hub"));
		
		this.friends.sendFriendsAlert(pp);
		
		try {
			this.c.sendPacket(new PacketSpyAction(pp.getName(), pp.getAddress().getAddress()
					.getHostAddress(),
					"Le joueur vient de passer les sécurités du login.", SpyAction.PLAYER_LOGGED));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onConnection(PreLoginEvent e){
		String name = e.getConnection().getName();
		String ip = e.getConnection().getAddress().getAddress().getHostAddress();
		
		try {			
			this.instance.getWhitelistSystem().setDefault(name, ip);
			
			this.instance.getWhitelistSystem().loadConfiguration();
		} catch (IOException e2) {
			this.instance.getChatUtils().sendMessageToGroup(this.instance.getWhitelistSystem().getPrefix()+
					"§c Une erreur critique est survenue ! {name = "+name+", "+e2.getMessage()+"}",
					VersionType.BUNGEECORD_VERSION, Rank.MODÉRATEUR);
			
			e.setCancelReason(new TextComponent
					("§cUne erreur critique est survenue ! Veuillez contacter Glowstoner ! (SpamBot ?)"));
			e.setCancelled(true);
			
			return;
		}
		
		if(this.instance.getWhitelistSystem().isEnable() == false) {
			return;
		}
		
		File file = new File(this.instance.getBungeePlugin().getDataFolder(), "whitelist.yml");
		
		Configuration config;
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			
			boolean accepted = config.getBoolean("players."+name+".accepted");
			
			if(config.getString("players."+name+".ip").equals(ip) && accepted == true) {
				return;
			}
			
			e.setCancelReason(new TextComponent("§cLe serveur est en maintenance !\n§9Discord : https://discordapp.com/invite/H7acUcX"));
			e.setCancelled(true);

		} catch (IOException e1) {
			this.instance.getChatUtils().sendMessageToGroup(this.instance.getWhitelistSystem().getPrefix()+
					"§c Une erreur critique est survenue ! {name = "+name+", "+e1.getMessage()+"}",
					VersionType.BUNGEECORD_VERSION, Rank.MODÉRATEUR);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(ChatEvent e) {
		ProxiedPlayer pp = (ProxiedPlayer) e.getSender();
		
		if(pp.getServer().getInfo().equals(this.instance.getBungeePlugin().getProxy().getServerInfo("login"))) {
			List<String> success = Arrays.asList("/login", "/register");
			
			if(!success.contains(e.getMessage().split(" ")[0])) {
				e.setCancelled(true);
			}
			
			if(e.isCommand()) {
				if(this.instance.getSQL().getCryptPassword(pp.getName()).equals("default-not-set")) {
					//not registered
					
					pp.sendMessage(new TextComponent(this.instance.getAuthentification().getRegisterMessage()));
					this.instance.getAuthentification().getRegisterTitle().send(pp);
				}else {
					//registered
					
					pp.sendMessage(new TextComponent(this.instance.getAuthentification().getLoginMessage()));
					this.instance.getAuthentification().getLoginTitle().send(pp);
				}
			}else {
				if(this.instance.getSQL().getCryptPassword(pp.getName()).equals("default-not-set")) {
					//not registered
					
					pp.sendMessage(new TextComponent(this.instance.getAuthentification().getRegisterMessage()));
					this.instance.getAuthentification().getRegisterTitle().send(pp);
				}else {
					//registered
					
					pp.sendMessage(new TextComponent(this.instance.getAuthentification().getLoginMessage()));
					this.instance.getAuthentification().getLoginTitle().send(pp);
				}
			}
		}else {
			try {
				PacketSpyAction ps = new PacketSpyAction(pp.getName(), pp.getAddress().getAddress().getHostAddress(),
						e.getMessage(), SpyAction.PLAYER_CHAT);
				ps.setDateToNow();
				
				this.c.sendPacket(ps);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerDisconnectEvent e) {
		try {
			for(ProxiedPlayer players : this.friends.getOnlineFriends(e.getPlayer())) {
				players.sendMessage(new TextComponent(this.pre+"§c- §r"+e.getPlayer().getName()+" s'est §cdéconnecté"));
			}
			
			this.c.sendPacket(new PacketSpyAction(e.getPlayer().getName(), 
					e.getPlayer().getAddress().getAddress().getHostAddress(), "Déconnection du proxy principal.",
					SpyAction.PLAYER_LEAVE));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void onMessageReceive(PluginMessageEvent e) {
		if(e.getTag().equals("fireapi")) {
			this.instance.getBungeePlugin().getProxy().getPluginManager().callEvent(
					new FireChannelReceiveEvent(e.getData(), e.getSender(), e.getReceiver()));
		}
	}
	
	@EventHandler
	public void onPingReceive(ProxyPingEvent e) {
		this.instance.getBungeePlugin().getLogger().info("Ping reçu de "+e.getConnection().getName());
	}

	@Override
	public void onSocketChange(ConnectionHandler c) {
		this.c = (Client) c;
	}
}