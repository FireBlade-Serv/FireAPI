package fr.glowstoner.fireapi.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.nametagedit.plugin.NametagEdit;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.PacketInjector;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;

public class Events implements Listener {
	
	private FireAPI api;
	private PacketInjector injector;

	public Events(FireAPI api, PacketInjector injector) {
		this.api = api;
		this.injector = injector;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		this.injector.addPlayer(e.getPlayer());
		
		NametagEdit.getApi().setPrefix(e.getPlayer(), this.api.getChatUtils().getPrefix(e.getPlayer()));
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		this.injector.removePlayer(e.getPlayer());
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
}