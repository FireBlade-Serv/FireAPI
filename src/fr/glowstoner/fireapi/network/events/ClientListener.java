package fr.glowstoner.fireapi.network.events;

import fr.glowstoner.fireapi.network.packets.Packet;

public interface ClientListener {
	
	void onPacketReceive(Packet packet);

}
