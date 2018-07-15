package fr.glowstoner.fireapi.bigbrother.console.commands;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class NameCommand implements CommandExecutor {
	
	private EncryptionKey key;

	public NameCommand(EncryptionKey key) {
		this.key = key;
	}
	
	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /name <name>", this.key);
		}else if(args.length == 1) {
			c.setName(args[0]);
			
			System.out.println("[BigBrother] L'utilisateur "+c.getIP()+" à changé de nom !"
					+ " Son nouveau nom est "+args[0]+" !");
			
			c.sendMessageWithPrefix("Bien. Votre nouveau nom est "+args[0]+" !", this.key);
		}else {
			c.sendMessageWithPrefix("Usage : /name <name>", this.key);
		}
	}

	@Override
	public String description() {
		return "Vous permet de changer votre name !";
	}

}
