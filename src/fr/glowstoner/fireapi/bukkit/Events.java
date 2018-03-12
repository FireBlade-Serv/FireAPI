package fr.glowstoner.fireapi.bukkit;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bigbrother.console.check.BigBrotherConnectionCheckListener;
import fr.glowstoner.fireapi.bigbrother.spy.enums.SpyAction;
import fr.glowstoner.fireapi.bigbrother.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FireInjector;
import fr.glowstoner.fireapi.bukkit.tag.FireTag;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;
import lombok.Setter;

public class Events implements Listener, BigBrotherConnectionCheckListener{
	
	private @Setter ConnectionHandler client;
	
	private FireAPI api;
	private FireInjector injector;
	private String id;
	private FireTag tag;

	public Events(FireAPI api) {
		this.api = api;
		this.injector = this.api.getPacketInjector();
		this.client = this.api.getClient();
		this.id = this.api.id();
		this.tag = this.api.getTagSystem();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.injector.addPlayer(e.getPlayer());
		
		e.setJoinMessage(null);
		
		this.tag.add(e.getPlayer());
		
		try {
			this.client.sendPacket(new PacketSpyAction(e.getPlayer().getName(), e.getPlayer()
					.getAddress().getAddress().getHostAddress(), this.id, SpyAction.PLAYER_SERVER_CONNECTION));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if(this.api.id().equals("login-spigot")) {
			e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0, 12, 0));
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		this.injector.removePlayer(e.getPlayer());
		
		e.setQuitMessage(null);
		
		this.tag.remove(e.getPlayer());
		
		try {
			this.client.sendPacket(new PacketSpyAction(e.getPlayer().getName(), e.getPlayer()
					.getAddress().getAddress().getHostAddress(), this.id, SpyAction.PLAYER_SERVER_DISCONNECT));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		FirePlayer fp = new FirePlayer(e.getPlayer(), this.api);
		
		String msg = (fp.hasRankAndSup(Rank.YOUTUBER)) ?
			e.getMessage().replace("&", "§").replace("%", "%%") :
			e.getMessage().replace("%", "%%");

		e.setFormat("§8["+this.api.getChatUtils().getStringRank(fp.getName())+"§8] §7"+
		this.api.getChatUtils().getRankColor(fp.getName())+fp.getName()+" §8>> §r"+msg);
	}

	@Override
	public void onSocketChange(ConnectionHandler c) {
		this.setClient(c);
	}
	
}