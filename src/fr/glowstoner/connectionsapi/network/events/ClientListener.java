package fr.glowstoner.connectionsapi.network.events;

import fr.glowstoner.connectionsapi.network.packets.Packet;

public interface ClientListener {
	
	void onPacketReceive(Packet packet);

}
