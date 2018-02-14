package fr.glowstoner.fireapi.bukkit.nms.packetlistener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PacketListener {
	
	private static List<PacketReceiveListener> l = new ArrayList<>();

	public static void registerListener(PacketReceiveListener listener) {
		l.add(listener);
	}
	
	public static void callListener(Player p, Object packet) {
		for(PacketReceiveListener in : l) {
			in.onPacketReceive(p, packet);
		}
	}
}
