package fr.glowstoner.fireapi.bungeecord;

import java.io.IOException;

import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.connectionsapi.network.events.ClientListener;
import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;
import fr.glowstoner.connectionsapi.network.packets.login.PacketLogin;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FireInjector;
import fr.glowstoner.fireapi.bukkit.tag.FireTag;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.commands.CoinsCheckerCommand;
import fr.glowstoner.fireapi.bungeecord.commands.FireRankCommand;
import fr.glowstoner.fireapi.bungeecord.commands.FireWhiteListCommand;
import fr.glowstoner.fireapi.bungeecord.commands.FriendsCommand;
import fr.glowstoner.fireapi.bungeecord.commands.LoginCommand;
import fr.glowstoner.fireapi.bungeecord.commands.RegisterCommand;
import fr.glowstoner.fireapi.bungeecord.commands.StaffChatCommand;
import fr.glowstoner.fireapi.bungeecord.commands.misc.Discord;
import fr.glowstoner.fireapi.bungeecord.commands.misc.Website;
import fr.glowstoner.fireapi.bungeecord.events.Events;
import fr.glowstoner.fireapi.bungeecord.friends.FireFriends;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC.EnumPacketGediminasACTODO;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC.EnumPacketGediminasACType;
import fr.glowstoner.fireapi.gediminas.console.check.GediminasConnectionCheck;
import fr.glowstoner.fireapi.gediminas.console.check.enums.GediminasConnectionCheckType;
import fr.glowstoner.fireapi.gediminas.console.login.GediminasConnectionInfos;
import fr.glowstoner.fireapi.gediminas.console.login.GediminasLoginGetter;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketExecute;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketVersion;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.PacketPlayerPing;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.rank.Rank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeMain extends Plugin implements FireAPI{
	
	private static FireAPI api;
	
	private final FireSQL sql = new FireSQL();
	
	private FireFriends friends;
	private FireRank rank;
	private FireAuth auth;
	private FireChat chat;
	private FireWL wl;
	private GediminasConnectionCheck check;
	
	private GediminasLoginGetter log;
	private Client c;

	@Override
	public void onEnable() {
		super.getLogger().info("FireAPI actif !");
		
		this.sql.connection();
		this.sql.startSqlRefreshScheduler(1800L);
		this.sql.putConsole();
		
		this.rank = new FireRank(this.sql);
		
		super.getProxy().registerChannel("fireapi");
		
		this.log = new GediminasLoginGetter();
		
		try {
			this.log.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			ConnectionsAPI.init();
			
			Client c = new Client("62.4.31.183", 2566);
			
			c.start();
			
			this.c = c;
			
			ConnectionHandler ch = c;
			
			ch.eval();
			
			ch.sendPacket(new PacketLogin(this.log.getKey(), log.getPassword()));
			ch.sendPacket(new PacketVersion(VersionType.BUNGEECORD_VERSION));
			ch.sendPacket(new PacketCommand("name main-bungeecord"));
			
			GediminasConnectionCheck check = new GediminasConnectionCheck
					(this, GediminasConnectionCheckType.GLOBAL_CHECK, GediminasConnectionInfos.builder()
							.id("main-bungeecord")
							.key(this.log.getKey())
							.password(this.log.getPassword())
							.versionType(VersionType.BUNGEECORD_VERSION)
							
							.build());
			
			check.startChecks();
			
			this.setChecker(check);
		}catch(Exception ex) {
			GediminasConnectionCheck check = new GediminasConnectionCheck
					(this, GediminasConnectionCheckType.ERROR_CHECK, GediminasConnectionInfos.builder()
							.id("main-bungeecord")
							.key(this.log.getKey())
							.password(this.log.getPassword())
							.versionType(VersionType.BUNGEECORD_VERSION)
							
							.build());
			
			check.startChecks();
		}
		
		ConnectionsAPI.getListeners().registerClientListener(new ClientListener() {
			
			@Override
			public void onPacketReceive(Packet packet) {
				if(packet instanceof PacketExecute) {
					BungeeMain.super.getLogger().info("(Gediminas) Execution de la commande : "+ ((PacketExecute) packet).
								getServerCommand()+".");
					
					BungeeMain.super.getProxy().getPluginManager().dispatchCommand
						(BungeeMain.super.getProxy().getConsole(), ((PacketExecute) packet).
								getServerCommand());
				}else if(packet instanceof PacketPlayerPing) {
					PacketPlayerPing pp = (PacketPlayerPing) packet;
					
					BungeeMain.super.getProxy().getPlayer(pp.getName()).sendMessage(new TextComponent
							("§6[§ePing§6]§r Ton ping §eproxy§r est de §e"
					+BungeeMain.super.getProxy().getPlayer(pp.getName()).getPing()+" ms§r !"));
				}else if(packet instanceof PacketGediminasAC) {
					PacketGediminasAC gacp = (PacketGediminasAC) packet;
					
					if(gacp.getType().equals(EnumPacketGediminasACType.CHEAT_DETECTION)) {
						if(gacp.getTODO().equals(EnumPacketGediminasACTODO.INFORM_STAFF)) {
							for(ProxiedPlayer ps : getProxy().getPlayers()) {
								if(api.getRankSystem().hasRankAndSup(ps.getName(), Rank.ASSISTANT)) {
									ps.sendMessage(
									new TextComponent("§c[Gediminas] [Cheat] §4"+gacp.getPlayerName()+" §r~ "+gacp.getMessage()));
								}
							}
						}
					}
				}
			}
			
		});
		
		this.friends = new FireFriends(this);
		this.friends.initFolder();
		
		try {
			this.friends.loadConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.auth = new FireAuth(this);
		
		try {
			this.auth.loadConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.chat = new FireChat(this);
		
		this.wl = new FireWL(this);
		this.wl.loadConfiguration();
		
		super.getLogger().info("Clé de sécurité utilisée : "+this.auth.getSecurityKey());
		
		PluginManager man = super.getProxy().getPluginManager();
		
		man.registerCommand(this, new CoinsCheckerCommand(this, "coins"));
		man.registerCommand(this, new Website("site"));
		man.registerCommand(this, new Discord("discord"));
		man.registerCommand(this, new FriendsCommand(this, "amis"));
		man.registerCommand(this, new FireRankCommand(this, "firerank"));
		man.registerCommand(this, new RegisterCommand(this, "register"));
		man.registerCommand(this, new LoginCommand(this, "login"));
		man.registerCommand(this, new FireWhiteListCommand("firewl", this));
		
		StaffChatCommand scc = new StaffChatCommand(this, "firestaffchat");
		
		man.registerCommand(this, scc);
		
		Events events = new Events(this, scc);
		
		man.registerListener(this, events);
		
		this.check.registerListener(events);
		
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
	public org.bukkit.plugin.Plugin getBukkitPlugin() {
		return null;
	}

	@Override
	public Plugin getBungeePlugin() {
		return this;
	}

	@Override
	public FireRank getRankSystem() {
		return this.rank;
	}

	@Override
	public FireSQL getSQL() {
		return this.sql;
	}

	@Override
	public FireChat getChatUtils() {
		return this.chat;
	}

	@Override
	public FireAuth getAuthentification() {
		return this.auth;
	}

	@Override
	public FireWL getWhitelistSystem() {
		return this.wl;
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
		return "main-bungeecord";
	}
	
	@Override
	public VersionType type() {
		return VersionType.BUNGEECORD_VERSION;
	}
	
	public static FireAPI getAPI() {
		return api;
	}

	@Override
	public FireFriends getFriends() {
		return this.friends;
	}

	@Override
	public GediminasConnectionCheck getChecker() {
		return this.check;
	}

	@Override
	public void setChecker(GediminasConnectionCheck checker) {
		this.check = checker;
	}

	@Override
	public FireInjector getPacketInjector() {
		return null;
	}

	@Override
	public FireTag getTagSystem() {
		return null;
	}
}