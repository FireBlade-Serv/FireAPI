package fr.glowstoner.fireapi.gediminas.console.commands;

import java.util.Iterator;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.events.Listeners;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;

public class HelpCommand implements CommandExecutor {

	private Listeners l;

	public HelpCommand(Listeners listeners) {
		this.l = listeners;
	}

	@Override
	public void execute(ConnectionHandler connection, String command, String[] args) {
		if(this.l.getCommands().size() == 0) {
			connection.sendMessageWithPrefix("Il n'y a aucune commande disponible !");
		}else {
			Iterator<String> cmdit = this.l.getCommands().keySet().iterator();
			
			while(cmdit.hasNext()) {
				String next = cmdit.next();
				
				if(this.l.getAliases().containsKey(next)) {
					connection.sendMessageWithPrefix("/"+next+" : "+this.l.getCommands().
							get(next).description()+" Alias : "+this.l.getAliases().get(next).toString());
				}else {
					connection.sendMessageWithPrefix("/"+next+" : "+this.l.getCommands().
							get(next).description());
				}
			}
		}
	}

	@Override
	public String description() {
		return "Vous permet d'avoir l'aide !";
	}

}
