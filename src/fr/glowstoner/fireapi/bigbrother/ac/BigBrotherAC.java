package fr.glowstoner.fireapi.bigbrother.ac;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bigbrother.ac.packet.PacketBigBrotherAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherActionAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherCheatAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherTypeAC;
import fr.glowstoner.fireapi.bigbrother.spy.enums.SpyAction;
import fr.glowstoner.fireapi.bigbrother.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.FirePacketListener;
import fr.glowstoner.fireapi.utils.LocationUtil;

public class BigBrotherAC implements Listener{
	
	private Map<Player, Integer> cps = new ConcurrentHashMap<>();
	private FireAPI api;
	
	public BigBrotherAC(FireAPI api) {
		this.api = api;
	}

	public void init() {
		BigBrotherPacketListenerAC gacpl = new BigBrotherPacketListenerAC(this.api, this);
		
		FirePacketListener.registerListener(gacpl);
		
		gacpl.startPacketScheduler();
		
		this.api.getBukkitPlugin().getServer().getPluginManager().registerEvents(this, this.api.getBukkitPlugin());
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
							api.getClient().sendPacket(new PacketBigBrotherAC(p.getName(), "Autoclick "+cps.get(p)+" CPS",
									BigBrotherTypeAC.CHEAT_DETECTION, BigBrotherActionAC.INFORM_STAFF,
									BigBrotherCheatAC.AUTOCLICK, ((CraftPlayer) p).getHandle().ping), api.encryptionKey());
							api.getClient().sendPacket(new PacketSpyAction(p.getName(), p.getAddress().getAddress().getHostAddress(),
									"Autoclick "+cps.get(p)+" CPS", SpyAction.PLAYER_BBAC_DETECTION), api.encryptionKey());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					cps.remove(p);
				}
				
			}, 20L);
		}
	}
	
	public void moveAlert(Player p, int packets, boolean position) {
		try {
			String message = (position) ? "MoveCheat ("+packets+" paquets, position mode)" :
				"MoveCheat ("+packets+" paquets)";
			
			this.api.getClient().sendPacket(new PacketBigBrotherAC(p.getName(), message,
					BigBrotherTypeAC.CHEAT_DETECTION, BigBrotherActionAC.INFORM_STAFF,
					BigBrotherCheatAC.MOVE, ((CraftPlayer) p).getHandle().ping), api.encryptionKey());
			this.api.getClient().sendPacket(new PacketSpyAction(p.getName(), p.getAddress().getAddress().getHostAddress(),
					"Flyhack ("+packets+" paquets)", SpyAction.PLAYER_BBAC_DETECTION), this.api.encryptionKey());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startKillAuraPlayerCheck(Player p) {
		BigBrotherKillAuraAC kc = new BigBrotherKillAuraAC(p);
		
		kc.startRegularCheck(this.api);
	}
	
	public void startKillAuraChecks(long period) {
		this.api.getBukkitPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(this.api.getBukkitPlugin(), new Runnable() {
			
			@Override
			public void run() {
				List<? extends Player> list = (List<? extends Player>) api.getBukkitPlugin().getServer().getOnlinePlayers();
				
				if(!list.isEmpty()) {
					startKillAuraPlayerCheck(list.get(new Random().nextInt(list.size())));
				}
			}
			
		}, 0L, period);
	}
	
	public void reachAlert(Player p, double distance) {
		try {
			this.api.getClient().sendPacket(new PacketBigBrotherAC(p.getName(), "Reach "+distance+" pl+",
					BigBrotherTypeAC.CHEAT_DETECTION, BigBrotherActionAC.INFORM_STAFF, BigBrotherCheatAC.REACH,
					((CraftPlayer) p).getHandle().ping), this.api.encryptionKey());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fastSneakAlert(Player p) {
		try {
			this.api.getClient().sendPacket(new PacketBigBrotherAC(p.getName(), "FastSneak",
					BigBrotherTypeAC.CHEAT_DETECTION, BigBrotherActionAC.INFORM_STAFF,
					BigBrotherCheatAC.FASTSNEAK, ((CraftPlayer) p).getHandle().ping), this.api.encryptionKey());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.PHYSICAL)) {
			this.putCPS(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			
			double distance = LocationUtil.getDistance(p.getLocation(), e.getEntity().getLocation());
						
			/*
			 * if(distance >= max){
			 * 		alert
			 * }
			 */
			
			//max = 4
			
			if(distance >= 4) {
				this.reachAlert(p, (distance - 4));
			}
			
			this.putCPS(p);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		
	}
}
