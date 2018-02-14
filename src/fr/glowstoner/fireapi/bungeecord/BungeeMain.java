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
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.cmd.CoinsChecker;
import fr.glowstoner.fireapi.bungeecord.cmd.FireRankCmd;
import fr.glowstoner.fireapi.bungeecord.cmd.FireWhiteList;
import fr.glowstoner.fireapi.bungeecord.cmd.FriendsCmd;
import fr.glowstoner.fireapi.bungeecord.cmd.LoginCmd;
import fr.glowstoner.fireapi.bungeecord.cmd.RegisterCmd;
import fr.glowstoner.fireapi.bungeecord.cmd.misc.Discord;
import fr.glowstoner.fireapi.bungeecord.cmd.misc.Website;
import fr.glowstoner.fireapi.bungeecord.events.Events;
import fr.glowstoner.fireapi.bungeecord.friends.Friends;
import fr.glowstoner.fireapi.chat.FireChat;
import fr.glowstoner.fireapi.gediminas.console.GediminasLoginGetter;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketExecute;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketVersion;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.PacketPlayerPing;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.rank.FireRank;
import fr.glowstoner.fireapi.sql.FireSQL;
import fr.glowstoner.fireapi.wl.FireWL;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeMain extends Plugin implements FireAPI{
	
	private final FireSQL sql = new FireSQL();
	
	private Friends friends;
	private FireRank rank;
	private FireAuth auth;
	private FireChat chat;
	private FireWL wl;
	
	private GediminasLoginGetter log;
	private Client c;

	@Override
	public void onEnable() {
		super.getLogger().info("FireAPI actif !");
		
		sql.connection();
		sql.startSqlRefreshScheduler(1800L);
		sql.putConsole();
		
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
			
			ConnectionsAPI.getListeners().registerClientListener(new ClientListener() {
				
				@Override
				public void onPacketReceive(Packet packet) {
					if(packet instanceof PacketExecute) {
						BungeeMain.super.getProxy().getPluginManager().dispatchCommand
							(BungeeMain.super.getProxy().getConsole(), ((PacketExecute) packet).
									getServerCommand());
					}else if(packet instanceof PacketPlayerPing) {
						PacketPlayerPing pp = (PacketPlayerPing) packet;
						
						BungeeMain.super.getProxy().getPlayer(pp.getName()).sendMessage(new TextComponent
								("§6[§ePing§6]§r Ton ping §eproxy§r est de §e"
						+BungeeMain.super.getProxy().getPlayer(pp.getName()).getPing()+" ms§r !"));
					}
				}
				
			});
			
			ConnectionHandler ch = c;
			
			ch.eval();
			
			ch.sendPacket(new PacketLogin(this.log.getKey(), log.getPassword()));
			ch.sendPacket(new PacketVersion(VersionType.BUNGEECORD_VERSION));
			ch.sendPacket(new PacketCommand("name main-bungeecord"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.friends = new Friends(this);
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
		
		man.registerCommand(this, new CoinsChecker(this, "coins"));
		man.registerCommand(this, new Website("site"));
		man.registerCommand(this, new Discord("discord"));
		man.registerCommand(this, new FriendsCmd(this, c, "amis", this.friends));
		man.registerCommand(this, new FireRankCmd("firerank", this.rank, this.sql));
		man.registerCommand(this, new RegisterCmd("register", this, this.auth, this.sql));
		man.registerCommand(this, new LoginCmd("login", this.auth, this));
		man.registerCommand(this, new FireWhiteList("firewl", this));
		
		man.registerListener(this, new Events(this.friends, this));
	}
	
	@Override
	public void onDisable() {
		sql.disconnect();
		
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
	public FireWL getWhiteListSystem() {
		return this.wl;
	}
}
