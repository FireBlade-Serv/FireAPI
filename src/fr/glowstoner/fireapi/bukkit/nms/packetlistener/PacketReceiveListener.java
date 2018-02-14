package fr.glowstoner.fireapi.bukkit.nms.packetlistener;

import org.bukkit.entity.Player;

public interface PacketReceiveListener {
	
	 void onPacketReceive(Player p, Object packet);

}
