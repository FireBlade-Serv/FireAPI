package fr.glowstoner.fireapi.bigbrother.console.commands;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class ChatCommand implements CommandExecutor {
	
	private EncryptionKey key;
	
	public ChatCommand(EncryptionKey key) {
		this.key = key;
	}
	
	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /chat blablastalinehitlerlenine", this.key);
		}else {
			StringBuilder builder = new StringBuilder();
			
			for(String arg : args) {
				builder.append(arg+" ");
			}
			
			String name = (c.getName().equals("default-name")) ? c.getIP() : c.getName();
			System.out.println("[BigBrother] "+name+" >> "+builder.toString());
			
			c.sendMessageWithPrefix("Vous avez bien envoyé votre message !", this.key);
		}
	}

	@Override
	public String description() {
		return "Cette commande vous permet de parler en mode global !";
	}

}
