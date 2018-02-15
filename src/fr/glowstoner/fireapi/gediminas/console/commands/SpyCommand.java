package fr.glowstoner.fireapi.gediminas.console.commands;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyUtils;

public class SpyCommand implements CommandExecutor {

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage /spy <joueur>");
		}else if(args.length == 1) {
			try {
				GediminasSpyUtils.sendInfosToClient(c, args[0]);
			}catch (Exception e) {
				c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé !");
			}
		}
	}

	@Override
	public String description() {
		return "Vous permet d'avoir les informations sur un joueur en particuler.";
	}

}
