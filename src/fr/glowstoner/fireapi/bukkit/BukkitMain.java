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
import fr.glowstoner.fireapi.bigbrother.ac.BigBrotherAC;
import fr.glowstoner.fireapi.bigbrother.console.check.BigBrotherConnectionCheck;
import fr.glowstoner.fireapi.bigbrother.console.check.enums.BigBrotherConnectionCheckType;
import fr.glowstoner.fireapi.bigbrother.console.login.BigBrotherConnectionInfos;
import fr.glowstoner.fireapi.bigbrother.console.login.BigBrotherLoginGetter;
import fr.glowstoner.fireapi.bigbrother.console.packets.PacketExecute;
import fr.glowstoner.fireapi.bigbrother.console.packets.PacketVersion;
import fr.glowstoner.fireapi.bukkit.commands.PingCommand;
import fr.glowstoner.fireapi.bukkit.friends.FriendsActionInventoryGUI;
import fr.glowstoner.fireapi.bukkit.id.FireBukkitID;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FireInjector;
import fr.glowstoner.fireapi.bukkit.tag.FireTag;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.friends.FireFriends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.PacketFriends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.action.FriendsActionTransmetterGUI;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;

public class BukkitMain extends JavaPlugin implements FireAPI{
	
	private static FireAPI api;
	
	private FireRank rank;
	private FireSQL sql;
	private FireChat chat;
	private BigBrotherLoginGetter log;
	private FireInjector injector;
	private Client c;
	private FireTag tag;
	private BigBrotherConnectionCheck check;
	private BigBrotherAC ac;
	
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
	
		this.tag = new FireTag(this);
		
		this.tag.registerRanks();
		
		FireBukkitID id = new FireBukkitID(this);
		
		try {
			id.loadConfiguration();
		} catch (IOException | InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
		
		this.id = id.getID();
		
		this.log = new BigBrotherLoginGetter();
		
		try {
			this.log.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		super.getLogger().info("ID serveur utilisé : "+this.id);
		
		this.injector = new FireInjector();
		
		this.ac = new BigBrotherAC(this);
		
		this.ac.init();
		this.ac.startKillAuraChecks((long) (30 * 20));
		
		try {
			ConnectionsAPI.init();
			
			Client c = new Client("62.4.16.89", 2566);
			
			c.start();
			
			this.c = c;
			
			ConnectionHandler ch = c;
			
			ch.eval();
			
			ch.sendPacket(new PacketLogin(this.log.getKey(), this.log.getPassword()));
			ch.sendPacket(new PacketVersion(VersionType.SPIGOT_VERSION));
			ch.sendPacket(new PacketCommand("name "+this.id));

			BigBrotherConnectionCheck check = new BigBrotherConnectionCheck
					(this, BigBrotherConnectionCheckType.GLOBAL_CHECK, BigBrotherConnectionInfos.builder()
							.id(this.id)
							.key(this.log.getKey())
							.password(this.log.getPassword())
							.versionType(VersionType.SPIGOT_VERSION)
							
							.build());
			
			check.startChecks();
			
			this.setChecker(check);
		}catch (Exception ex) {
			BigBrotherConnectionCheck check = new BigBrotherConnectionCheck
					(this, BigBrotherConnectionCheckType.ERROR_CHECK, BigBrotherConnectionInfos.builder()
							.id(this.id)
							.key(this.log.getKey())
							.password(this.log.getPassword())
							.versionType(VersionType.SPIGOT_VERSION)
							
							.build());
			
			check.startChecks();
		}
		
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
		
		Events events = new Events(this);
		
		super.getServer().getPluginManager().registerEvents(events, this);
		super.getServer().getPluginManager().registerEvents(new EventsAT(this), this);
		
		this.check.registerListener(events);
		
		super.getCommand("ping").setExecutor(new PingCommand(this));
		super.getCommand("at").setExecutor(new AdminToolsCmd(this));
		
		api = this;
	}
	
	@Override
	public void onDisable() {
		this.sql.disconnect();
		
		try {
			this.c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	public FireWL getWhitelistSystem() {
		return null;
	}
	
	@Override
	public Client getClient() {
		return this.c;
	}
	
	@Override
	public void setClient(Client c) {
		this.c = c;
	}

	@Override
	public String id() {
		return this.id;
	}
	
	@Override
	public VersionType type() {
		return VersionType.SPIGOT_VERSION;
	}
	
	public static FireAPI getAPI() {
		return api;
	}

	@Override
	public FireFriends getFriends() {
		return null;
	}

	@Override
	public BigBrotherConnectionCheck getChecker() {
		return this.check;
	}

	@Override
	public void setChecker(BigBrotherConnectionCheck checker) {
		this.check = checker;
	}

	@Override
	public FireInjector getPacketInjector() {
		return this.injector;
	}

	@Override
	public FireTag getTagSystem() {
		return this.tag;
	}
}