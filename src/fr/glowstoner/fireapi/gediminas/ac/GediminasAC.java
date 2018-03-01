package fr.glowstoner.fireapi.gediminas.ac;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FirePacketListener;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC.EnumPacketGediminasACTODO;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC.EnumPacketGediminasACType;

public class GediminasAC {
	
	private Map<Player, Integer> cps = new ConcurrentHashMap<>();
	private FireAPI api;
	
	public GediminasAC(FireAPI api) {
		this.api = api;
	}

	public void init() {
		FirePacketListener.registerListener(new GediminasACPacketListener(this.api));
		
		this.api.getBukkitPlugin().getServer().getPluginManager().
			registerEvents(new GediminasEventsAC(this), this.api.getBukkitPlugin());
	}
	
	public void putCPS(Player p) {
		if(this.cps.containsKey(p)) {
			this.cps.replace(p, this.cps.get(p) + 1);
		}else {
			this.cps.put(p, 1);
			
			this.api.getBukkitPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.api.getBukkitPlugin(), new Runnable() {
				
				@Override
				public void run() {
					//alert = 18
					
					if(cps.get(p) >= 18 && cps.get(p) < 22) {
						try {
							api.getClient().sendPacket(new PacketGediminasAC
									(p.getName(), "Autoclick / "+cps.get(p)+" CPS", EnumPacketGediminasACType.CHEAT_DETECTION,
									EnumPacketGediminasACTODO.INFORM_STAFF));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					cps.remove(p);
				}
				
			}, 20L);
		}
	}
}
