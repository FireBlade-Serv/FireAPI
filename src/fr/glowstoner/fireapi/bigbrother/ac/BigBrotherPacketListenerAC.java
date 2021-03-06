package fr.glowstoner.fireapi.bigbrother.ac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bukkit.nms.FireReflection;
import fr.glowstoner.fireapi.bukkit.nms.packetlistener.PacketReceiveListener;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction.EnumPlayerAction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInLook;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPosition;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class BigBrotherPacketListenerAC implements PacketReceiveListener, Runnable{
	
	private Map<Player, List<Object>> spackets = new ConcurrentHashMap<>();
	private FireAPI api;
	private BigBrotherAC ac;
	
	public BigBrotherPacketListenerAC(FireAPI api, BigBrotherAC ac) {
		this.api = api;
		this.ac = ac;
	}

	@Override
	public void onPacketReceive(Player p, Object packet) {
		if(packet instanceof PacketPlayInFlying) {
			PacketPlayInFlying fly = (PacketPlayInFlying) packet;
			
			String status = (fly.f()) ? "ON GROUND" : "FLYING";
			
			System.out.println(fly.getClass().getSimpleName()+" / "+status);
		}else if(packet instanceof PacketPlayInEntityAction) {
			PacketPlayInEntityAction ea = (PacketPlayInEntityAction) packet;
			
			System.out.println(ea.getClass().getSimpleName()+" / "+ea.b());
		}else {
			System.out.println(packet.getClass().getSimpleName());
		}
		
		if(packet instanceof PacketPlayInUseEntity) {
			PacketPlayInUseEntity puse = (PacketPlayInUseEntity) packet;
			int id = (int) FireReflection.getFieldValueByName(puse, "a");
			
			if(BigBrotherKillAuraAC.getFakePlayers().containsKey(p)) {
				if(BigBrotherKillAuraAC.getFakePlayers().get(p) == id) {
					if(BigBrotherKillAuraAC.getClicked().get(p) == false) {
						BigBrotherKillAuraAC.getClicked().replace(p, true);
					}
				}
			}
		}
		
		if(this.spackets.containsKey(p)) {
			List<Object> list = this.spackets.get(p);
			
			list.add(packet);
			
			this.spackets.replace(p, list);
		}else {
			List<Object> list = new CopyOnWriteArrayList<>();
			
			list.add(packet);
			
			this.spackets.put(p, list);
		}
	}

	public void startPacketScheduler() {
		this.api.getBukkitPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(this.api.getBukkitPlugin(), this, 0L, 200L);
	}
	
	public void sendSortedPackets(Player p, List<Object> list) {
		List<PacketPlayInKeepAlive> ka = new ArrayList<>();
		List<PacketPlayInChat> chat = new ArrayList<>();
		List<PacketPlayInUseEntity> ue = new ArrayList<>();
		List<PacketPlayInPosition> pos = new ArrayList<>();
		List<PacketPlayInPositionLook> lpos = new ArrayList<>();
		List<PacketPlayInLook> l = new ArrayList<>();
		List<PacketPlayInBlockDig> dig = new ArrayList<>();
		List<PacketPlayInBlockPlace> pl = new ArrayList<>();
		List<PacketPlayInArmAnimation> aanim = new ArrayList<>();
		List<PacketPlayInEntityAction> ea = new ArrayList<>();
		List<PacketPlayInHeldItemSlot> his = new ArrayList<>();
		List<PacketPlayInFlying> fly = new ArrayList<>();
		List<PacketPlayInSettings> set = new ArrayList<>();
		List<PacketPlayInCustomPayload> payload = new ArrayList<>();
		List<Object> other = new ArrayList<>();
		
		for(Object in : list) {
			if(in instanceof PacketPlayInKeepAlive) {
				ka.add((PacketPlayInKeepAlive) in);
			}else if(in instanceof PacketPlayInChat) {
				chat.add((PacketPlayInChat) in);
			}else if(in instanceof PacketPlayInUseEntity) {
				ue.add((PacketPlayInUseEntity) in);
			}else if(in instanceof PacketPlayInPosition) {
				pos.add((PacketPlayInPosition) in);
			}else if(in instanceof PacketPlayInPositionLook) {
				lpos.add((PacketPlayInPositionLook) in);
			}else if(in instanceof PacketPlayInLook) {
				l.add((PacketPlayInLook) in);
			}else if(in instanceof PacketPlayInBlockDig) {
				dig.add((PacketPlayInBlockDig) in);
			}else if(in instanceof PacketPlayInBlockPlace) {
				pl.add((PacketPlayInBlockPlace) in);
			}else if(in instanceof PacketPlayInArmAnimation) {
				aanim.add((PacketPlayInArmAnimation) in);
			}else if(in instanceof PacketPlayInEntityAction) {
				ea.add((PacketPlayInEntityAction) in);
			}else if(in instanceof PacketPlayInHeldItemSlot) {
				his.add((PacketPlayInHeldItemSlot) in);
			}else if(in instanceof PacketPlayInFlying) {
				fly.add((PacketPlayInFlying) in);
			}else if(in instanceof PacketPlayInSettings) {
				set.add((PacketPlayInSettings) in);
			}else if(in instanceof PacketPlayInCustomPayload) {
				payload.add((PacketPlayInCustomPayload) in);
			}else {
				other.add(in);
			}
		}
		
		p.sendMessage("KeepAlive : "+ka.size()+" ; Chat : "+chat.size()+" ; UseEntity : "+ue.size()+
				" ; Position : "+pos.size()+" ; PositionLook : "+lpos.size()+" ; Look : "+l.size()+" ; Dig : "+
				dig.size()+" ; Place : "+pl.size()+" ; ArmAnimation : "+aanim.size()+" ; EntityAction : "+ea.size()+
				" ; HeldItemSlot : "+his.size()+" ; Flying : "+fly.size()+" ; Settings : "+set.size()+" ;"
				+ " Other Packets : "+other.size()+" ; CustomPayload : "+payload.size());
		
		List<PacketPlayInFlying> pflyhak = new ArrayList<>();
		List<PacketPlayInFlying> flyhakpos = new ArrayList<>();
		List<PacketPlayInEntityAction> actionentitypacket = new ArrayList<>();
		
		for(Object pfp : list) {
			
			if(pfp instanceof PacketPlayInFlying) {
				PacketPlayInFlying ppif = (PacketPlayInFlying) pfp;
				
				if(ppif.f()) {
					pflyhak.add(ppif);
				}
				
				flyhakpos.add(ppif);
			}
		}
		
		if(pflyhak.size() >= 250) {
			this.ac.moveAlert(p, pflyhak.size(), false);
		}
		
		if(flyhakpos.size() >= 450) {
			this.ac.moveAlert(p, flyhakpos.size(), true);
		}
		
		for(PacketPlayInEntityAction pea : ea) {
			if(pea.b().equals(EnumPlayerAction.START_SNEAKING) || pea.b().equals(EnumPlayerAction.STOP_SNEAKING)) {
				actionentitypacket.add(pea);
			}
		}
		
		if(actionentitypacket.size() >= 350) {
			this.ac.fastSneakAlert(p);
		}
	}

	@Override
	public void run() {
		Iterator<Player> it = this.spackets.keySet().iterator();
		
		while(it.hasNext()) {
			Player next = it.next();
			
			this.sendSortedPackets(next, this.spackets.get(next));
			
			this.spackets.clear();
		}
	}
}