package fr.glowstoner.fireapi.bigbrother.console.server.commands;

import fr.glowstoner.fireapi.bigbrother.console.server.BigBrotherListener;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class ListCommand implements CommandExecutor {

	private BigBrotherListener gl;
	private EncryptionKey key;

	public ListCommand(EncryptionKey key, BigBrotherListener gl) {
		this.gl = gl;
		this.key = key;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		c.sendMessageWithPrefix("Liste des connections :", this.key);
		
		for(ConnectionHandler ch : this.gl.getConnected()) {
			String name = (ch.getName().equals("default-name")) ? ch.getIP() : ch.getName();
			
			c.sendMessageWithPrefix("- "+name+", IP = "+ch.getIP(), this.key);
		}
	}

	@Override
	public String description() {
		return "Permet de voir les clients qui sont actuellement connectés !";
	}

}
