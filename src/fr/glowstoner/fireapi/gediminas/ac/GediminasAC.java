package fr.glowstoner.fireapi.gediminas.ac;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FirePacketListener;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.GediminasActionAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.GediminasCheatAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.GediminasTypeAC;
import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;
import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyAction;

public class GediminasAC {
	
	private Map<Player, Integer> cps = new ConcurrentHashMap<>();
	private FireAPI api;
	
	public GediminasAC(FireAPI api) {
		this.api = api;
	}

	public void init() {
		GediminasPacketListenerAC gacpl = new GediminasPacketListenerAC(this.api, this);
		
		FirePacketListener.registerListener(gacpl);
		
		gacpl.startPacketScheduler();
		
		//this.api.getBukkitPlugin().getServer().getPluginManager().registerEvents(this, this.api.getBukkitPlugin());
	}
	
	public void putCPS(Player p) {
		if(this.cps.containsKey(p)) {
			this.cps.replace(p, this.cps.get(p) + 1);
		}else {
			this.cps.put(p, 1);
			
			this.api.getBukkitPlugin().getServer().getScheduler().runTaskLater(this.api.getBukkitPlugin(), new Runnable() {
				
				@Override
				public void run() {
					//alert = 18
					
					if(cps.get(p) >= 18) {
						try {
							api.getClient().sendPacket(new PacketGediminasAC(p.getName(), "Autoclick "+cps.get(p)+" CPS",
									GediminasTypeAC.CHEAT_DETECTION, GediminasActionAC.INFORM_STAFF,
									GediminasCheatAC.AUTOCLICK, ((CraftPlayer) p).getHandle().ping));
							api.getClient().sendPacket(new PacketSpyAction(p.getName(), p.getAddress().getAddress().getHostAddress(),
									"Autoclick "+cps.get(p)+" CPS", SpyAction.PLAYER_GAC_DETECTION));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					cps.remove(p);
				}
				
			}, 20L);
		}
	}
	
	public void flyAlert(Player p, int packets) {
		try {
			this.api.getClient().sendPacket(new PacketGediminasAC(p.getName(), "Flyhack ("+packets+" paquets)",
					GediminasTypeAC.CHEAT_DETECTION, GediminasActionAC.INFORM_STAFF,
					GediminasCheatAC.FLYHACK, ((CraftPlayer) p).getHandle().ping));
			this.api.getClient().sendPacket(new PacketSpyAction(p.getName(), p.getAddress().getAddress().getHostAddress(),
					"Flyhack ("+packets+" paquets)", SpyAction.PLAYER_GAC_DETECTION));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
