package fr.glowstoner.fireapi.bigbrother.console.commands;

import java.util.Iterator;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;
import fr.glowstoner.fireapi.network.events.Listeners;

public class HelpCommand implements CommandExecutor {

	private Listeners l;
	private EncryptionKey key;

	public HelpCommand(EncryptionKey key, Listeners listeners) {
		this.l = listeners;
		this.key = key;
	}

	@Override
	public void execute(ConnectionHandler connection, String command, String[] args) {
		if(this.l.getCommands().size() == 0) {
			connection.sendMessageWithPrefix("Il n'y a aucune commande disponible !", this.key);
		}else {
			Iterator<String> cmdit = this.l.getCommands().keySet().iterator();
			
			while(cmdit.hasNext()) {
				String next = cmdit.next();
				
				if(this.l.getAliases().containsKey(next)) {
					connection.sendMessageWithPrefix("/"+next+" : "+this.l.getCommands().
							get(next).description()+" Alias : "+this.l.getAliases().get(next).toString(), this.key);
				}else {
					connection.sendMessageWithPrefix("/"+next+" : "+this.l.getCommands().
							get(next).description(), this.key);
				}
			}
		}
	}

	@Override
	public String description() {
		return "Vous permet d'avoir l'aide !";
	}

}
