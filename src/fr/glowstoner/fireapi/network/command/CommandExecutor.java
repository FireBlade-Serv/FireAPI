package fr.glowstoner.fireapi.network.command;

import fr.glowstoner.fireapi.network.ConnectionHandler;

public interface CommandExecutor {
	
	void execute(ConnectionHandler connection, String command, String[] args);
	
	String description();

}
