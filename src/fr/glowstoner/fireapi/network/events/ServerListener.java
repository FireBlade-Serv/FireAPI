package fr.glowstoner.fireapi.network.events;

import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.packets.Packet;

public interface ServerListener {
	
	void onPacketReceive(Packet packet);

	void onConnection(ConnectionHandler connection);
	
	void onConnectionSuccessfull(ConnectionHandler connection);
	
	void onCommand(ConnectionHandler connection, String command, String[] args);
}
