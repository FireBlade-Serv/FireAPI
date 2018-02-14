package fr.glowstoner.fireapi.gediminas.console.commands;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;

public class NameCommand implements CommandExecutor {

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /name <name>");
		}else if(args.length == 1) {
			c.setName(args[0]);
			
			System.out.println("[Gediminas] L'utilisateur "+c.getIP()+" à changé de nom !"
					+ " Son nouveau nom est "+args[0]+" !");
			
			c.sendMessageWithPrefix("Bien. Votre nouveau nom est "+args[0]+" !");
		}else {
			c.sendMessageWithPrefix("Usage : /name <name>");
		}
	}

	@Override
	public String description() {
		return "Vous permet de changer votre name !";
	}

}
