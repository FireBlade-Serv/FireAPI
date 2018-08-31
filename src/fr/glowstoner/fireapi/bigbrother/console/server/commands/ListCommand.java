package fr.glowstoner.fireapi.bigbrother.console.server.commands;

import fr.glowstoner.fireapi.bigbrother.console.server.BigBrotherListener;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class ListCommand implements CommandExecutor {

	private BigBrotherListener gl;

	public ListCommand(BigBrotherListener gl) {
		this.gl = gl;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		c.sendMessageWithPrefix("Liste des connections :");
		
		for(ConnectionHandler ch : this.gl.getConnected()) {
			String name = (ch.getName().equals("default-name")) ? ch.getIP() : ch.getName();
			
			c.sendMessageWithPrefix("- "+name+", IP = "+ch.getIP());
		}
	}

	@Override
	public String description() {
		return "Permet de voir les clients qui sont actuellement connectés !";
	}

}
