package fr.glowstoner.fireapi.gediminas.ac;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GediminasEventsAC implements Listener {

	private GediminasAC gac;
	
	public GediminasEventsAC(GediminasAC gac) {
		this.gac = gac;
	}

	@EventHandler
	public void onInterract(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.PHYSICAL)) {
			this.gac.putCPS(e.getPlayer());
		}
	}
}
