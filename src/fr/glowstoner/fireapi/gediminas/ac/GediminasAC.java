package fr.glowstoner.fireapi.gediminas.ac;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FirePacketListener;
import fr.glowstoner.fireapi.gediminas.ac.packet.PacketGediminasAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.PacketGediminasACType;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.PacketGediminasCheatAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.PacketGediminasTODOAC;

public class GediminasAC {
	
	private Map<Player, Integer> cps = new ConcurrentHashMap<>();
	private FireAPI api;
	
	public GediminasAC(FireAPI api) {
		this.api = api;
	}

	public void init() {
		GediminasACPacketListener gacpl = new GediminasACPacketListener(this.api, this);
		
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
							api.getClient().sendPacket(new PacketGediminasAC
									(p.getName(), "Autoclick / "+cps.get(p)+" CPS", PacketGediminasACType.CHEAT_DETECTION,
									PacketGediminasTODOAC.INFORM_STAFF, PacketGediminasCheatAC.AUTOCLICK));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else {
						System.out.println("click "+cps.get(p)+" CPS");
					}
					
					cps.remove(p);
				}
				
			}, 20L);
		}
	}
	
	public void flyAlert(Player p, int packets) {
		try {
			this.api.getClient().sendPacket(new PacketGediminasAC(p.getName(), "FlyHack / "+packets+" packets",
					PacketGediminasACType.CHEAT_DETECTION, PacketGediminasTODOAC.INFORM_STAFF, PacketGediminasCheatAC.FLYHACK));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
