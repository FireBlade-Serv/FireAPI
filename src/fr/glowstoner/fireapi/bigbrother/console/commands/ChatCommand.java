package fr.glowstoner.fireapi.bigbrother.console.commands;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;

public class ChatCommand implements CommandExecutor {

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /chat blablastalinehitlerlenine");
		}else {
			StringBuilder builder = new StringBuilder();
			
			for(String arg : args) {
				builder.append(arg+" ");
			}
			
			String name = (c.getName().equals("default-name")) ? c.getIP() : c.getName();
			System.out.println("[BigBrother] "+name+" >> "+builder.toString());
		}
	}

	@Override
	public String description() {
		return "Cette commande vous permet de parler en mode global !";
	}

}
