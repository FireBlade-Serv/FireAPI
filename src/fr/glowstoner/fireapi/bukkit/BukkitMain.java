package fr.glowstoner.fireapi.bukkit;

import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import be.goldocelot.admintools.AdminToolsCmd;
import be.goldocelot.admintools.EventsAT;
import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.connectionsapi.network.events.ClientListener;
import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;
import fr.glowstoner.connectionsapi.network.packets.login.PacketLogin;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.cmd.Ping;
import fr.glowstoner.fireapi.bukkit.friends.FriendsActionInventoryGUI;
import fr.glowstoner.fireapi.bukkit.id.FireBukkitID;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.PacketInjector;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.friends.packets.PacketFriends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.action.FriendsActionTransmetterGUI;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.gediminas.console.GediminasLoginGetter;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketExecute;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketVersion;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;

public class BukkitMain extends JavaPlugin implements FireAPI{
	
	private static FireAPI api;
	
	private FireRank rank;
	private FireSQL sql;
	private FireChat chat;
	private GediminasLoginGetter log;
	private PacketInjector injector;
	private Client c;
	
	private String id;

	@Override
	public void onEnable() {
		super.getLogger().info("FireAPI actif !");
		
		sql = new FireSQL();
		
		this.sql.connection();
		this.sql.startSqlRefreshScheduler(1800L);
		this.sql.putConsole();
		
		rank = new FireRank(this.sql);
		
		this.chat = new FireChat(this);
		
		FireBukkitID id = new FireBukkitID(this);
		
		try {
			id.loadConfiguration();
		} catch (IOException | InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
		
		this.id = id.getID();
		
		this.log = new GediminasLoginGetter();
		
		try {
			this.log.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		super.getLogger().info("ID serveur utilisé : "+this.id);
		
		this.injector = new PacketInjector();
		
		try {
			ConnectionsAPI.init();
			
			Client c = new Client("62.4.31.183", 2566);
			
			c.start();
			
			ConnectionsAPI.getListeners().registerClientListener(new ClientListener() {
				
				@Override
				public void onPacketReceive(Packet packet) {
					if(packet instanceof PacketExecute) {
						BukkitMain.super.getServer().dispatchCommand
							(BukkitMain.super.getServer().getConsoleSender(),
									((PacketExecute) packet).getServerCommand());
					}else if(packet instanceof PacketFriends) {
						PacketFriends pf = (PacketFriends) packet;
						
						if(pf.getAction() instanceof FriendsActionTransmetterGUI) {
							FriendsActionTransmetterGUI fa = (FriendsActionTransmetterGUI) pf.getAction();
							
							if(fa.to().equals(VersionType.SPIGOT_VERSION)) {
								FriendsActionInventoryGUI faig = new FriendsActionInventoryGUI
										(fa.getName(), fa.getFriends(), fa.getConnected());
								
								faig.initPlayer(BukkitMain.this);
								faig.generateInventory();
								faig.openInventory();
							}
						}
					}
				}
				
			});
			
			this.c = c;
			
			ConnectionHandler ch = c;
			
			ch.eval();
			
			ch.sendPacket(new PacketLogin(this.log.getKey(), this.log.getPassword()));
			ch.sendPacket(new PacketVersion(VersionType.SPIGOT_VERSION));
			ch.sendPacket(new PacketCommand("name "+this.id));
			
			super.getServer().getPluginManager().registerEvents(new Events(this, ch, this.id, this.injector), this);
			super.getServer().getPluginManager().registerEvents(new EventsAT(this), this);
			
			super.getCommand("ping").setExecutor(new Ping(ch));
			super.getCommand("at").setExecutor(new AdminToolsCmd(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		api = this;
	}
	
	@Override
	public void onDisable() {
		super.getLogger().info("FireAPI inactif !");
	}

	@Override
	public Plugin getBukkitPlugin() {
		return this;
	}

	@Override
	public net.md_5.bungee.api.plugin.Plugin getBungeePlugin() {
		return null;
	}

	@Override
	public FireSQL getSQL() {
		return this.sql;
	}

	@Override
	public FireRank getRankSystem() {
		return this.rank;
	}

	@Override
	public FireChat getChatUtils() {
		return this.chat;
	}

	@Override
	public FireAuth getAuthentification() {
		return null;
	}

	@Override
	public FireWL getWhiteListSystem() {
		return null;
	}
	
	@Override
	public ConnectionHandler getClient() {
		return this.c;
	}

	@Override
	public String name() {
		return this.id;
	}
	
	@Override
	public VersionType type() {
		return VersionType.SPIGOT_VERSION;
	}
	
	public static FireAPI getAPI() {
		return api;
	}
}