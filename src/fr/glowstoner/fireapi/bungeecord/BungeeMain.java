package fr.glowstoner.fireapi.bungeecord;

import java.io.IOException;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bigbrother.ac.packet.PacketBigBrotherAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherActionAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherTypeAC;
import fr.glowstoner.fireapi.bigbrother.console.server.check.BigBrotherConnectionCheck;
import fr.glowstoner.fireapi.bigbrother.console.server.check.enums.BigBrotherConnectionCheckType;
import fr.glowstoner.fireapi.bigbrother.console.server.login.BigBrotherConnectionInfos;
import fr.glowstoner.fireapi.bigbrother.console.server.login.BigBrotherLoginGetter;
import fr.glowstoner.fireapi.bigbrother.console.server.packets.PacketExecute;
import fr.glowstoner.fireapi.bigbrother.console.server.packets.PacketVersion;
import fr.glowstoner.fireapi.bigbrother.console.server.packets.ping.PacketPlayerPing;
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
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.events.ClientListener;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;
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
	private BigBrotherConnectionCheck check;
	
	private BigBrotherLoginGetter log;
	private Client c;

	private FireNetwork fn;

	@Override
	public void onEnable() {
		super.getLogger().info("FireAPI actif !");
		
		this.sql.connection();
		this.sql.startSqlRefreshScheduler(1800L);
		this.sql.putConsole();
		
		this.rank = new FireRank(this.sql);
		
		super.getProxy().registerChannel("fireapi");
		
		this.log = new BigBrotherLoginGetter();
		
		try {
			this.log.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		System.out.println("(BigBrother) Utilisation de la clé de chiffrement suivante : "+this.log.getKey()
			.getKey());
		
		try {
			this.fn = new FireNetwork(this.log.getKey());
			this.fn.start(ConnectionType.CLIENT_CONNECTION, false);

			Client ch = (Client) this.fn.getBaseConnector();
			
			this.c = ch;
			
			ch.sendPacket(new PacketLogin(this.log.getPassword()));
			ch.sendPacket(new PacketVersion(VersionType.BUNGEECORD_VERSION));
			ch.sendPacket(new PacketCommand("name main-bungeecord"));
			
			BigBrotherConnectionCheck check = new BigBrotherConnectionCheck
					(this, BigBrotherConnectionCheckType.GLOBAL_CHECK, BigBrotherConnectionInfos.builder()
							.id("main-bungeecord")
							.key(this.log.getKey())
							.password(this.log.getPassword())
							.versionType(VersionType.BUNGEECORD_VERSION)
							
							.build());
			
			check.startChecks();
			
			this.setChecker(check);
		}catch(Exception ex) {
			BigBrotherConnectionCheck check = new BigBrotherConnectionCheck
					(this, BigBrotherConnectionCheckType.ERROR_CHECK, BigBrotherConnectionInfos.builder()
							.id("main-bungeecord")
							.key(this.log.getKey())
							.password(this.log.getPassword())
							.versionType(VersionType.BUNGEECORD_VERSION)
							
							.build());
			
			check.startChecks();
		}
		
		this.fn.getListeners().registerClientListener(new ClientListener() {
			
			@Override
			public void onPacketReceive(Packet packet) {
				if(packet instanceof PacketExecute) {
					BungeeMain.super.getLogger().info("(BigBrother) Execution de la commande : "+ ((PacketExecute) packet).
								getServerCommand()+".");
					
					BungeeMain.super.getProxy().getPluginManager().dispatchCommand
						(BungeeMain.super.getProxy().getConsole(), ((PacketExecute) packet).
								getServerCommand());
				}else if(packet instanceof PacketPlayerPing) {
					PacketPlayerPing pp = (PacketPlayerPing) packet;
					
					BungeeMain.super.getProxy().getPlayer(pp.getName()).sendMessage(new TextComponent
							("§6[§ePing§6]§r Ton ping §eproxy§r est de §e"
					+BungeeMain.super.getProxy().getPlayer(pp.getName()).getPing()+" ms§r !"));
				}else if(packet instanceof PacketBigBrotherAC) {
					PacketBigBrotherAC gacp = (PacketBigBrotherAC) packet;
					
					if(gacp.getType().equals(BigBrotherTypeAC.CHEAT_DETECTION)) {
						if(gacp.getTODO().equals(BigBrotherActionAC.INFORM_STAFF)) {
							for(ProxiedPlayer ps : getProxy().getPlayers()) {
								if(api.getRankSystem().hasRankAndSup(ps.getName(), Rank.ASSISTANT)) {
									ps.sendMessage(
									new TextComponent("§c[BigBrother] [Cheat] §4"+gacp.getPlayerName()+" §r~ "+gacp.getMessage()
									+" §c"+gacp.getPing()+"ms"));
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
		
		super.getLogger().info("(FireAuth) Clé de sécurité utilisée : "+this.auth.getSecurityKey());
		
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
	public BigBrotherConnectionCheck getChecker() {
		return this.check;
	}

	@Override
	public void setChecker(BigBrotherConnectionCheck checker) {
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