package fr.glowstoner.connectionsapi.network.packets.command;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;

public interface CommandExecutor {
	
	void execute(ConnectionHandler connection, String command, String[] args);
	
	String description();

}
