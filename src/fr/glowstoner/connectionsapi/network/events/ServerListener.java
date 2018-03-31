package fr.glowstoner.connectionsapi.network.events;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.Packet;

public interface ServerListener {
	
	void onPacketReceive(Packet packet);

	void onConnection(ConnectionHandler connection);
	
	void onConnectionSuccessfull(ConnectionHandler connection);
	
	void onCommand(ConnectionHandler connection, String command, String[] args);
}
