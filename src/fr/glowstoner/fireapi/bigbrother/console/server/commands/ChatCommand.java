package fr.glowstoner.fireapi.bigbrother.console.server.commands;

import fr.glowstoner.fireapi.bigbrother.console.server.BigBrotherListener;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class ChatCommand implements CommandExecutor {
	
	private BigBrotherListener l;

	public ChatCommand(BigBrotherListener listener) {
		this.l = listener;
	}
	
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
			
			c.sendMessageWithPrefix("Vous avez bien envoyé votre message !");
			
			this.sendToAll(builder.toString());
		}
	}
	
	private void sendToAll(String text) {
		for(ConnectionHandler chs : this.l.getConnected()) {
			chs.sendMessageWithPrefix(text);
		}
	}

	@Override
	public String description() {
		return "Cette commande vous permet de parler en mode global !";
	}

}
