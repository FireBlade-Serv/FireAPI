package fr.glowstoner.fireapi.gediminas.console.commands;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;

public class StatusCommand implements CommandExecutor {

	@Override
	public void execute(ConnectionHandler connection, String command, String[] args) {
		
	}

	@Override
	public String description() {
		return "Vous permet de voir le status des connections serveur !";
	}

}
