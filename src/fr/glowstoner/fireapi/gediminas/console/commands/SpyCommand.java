package fr.glowstoner.fireapi.gediminas.console.commands;

import java.util.List;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpy;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyHistory;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyHistoryData;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyUtils;

public class SpyCommand implements CommandExecutor {

	private GediminasSpy gs;
	
	public SpyCommand(GediminasSpy gs) {
		this.gs = gs;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /spy full <joueur>");
			c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
			c.sendMessageWithPrefix("Usage : /spy chat <joueur>");
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("full")) {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>");
			}else if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
			}else if(args[0].equalsIgnoreCase("chat")) {
				c.sendMessageWithPrefix("Usage : /spy chat <joueur>");
			}else {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>");
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
				c.sendMessageWithPrefix("Usage : /spy chat <joueur>");
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("full")) {
				try {
					GediminasSpyUtils.sendInfosToClient(c, args[1]);
				}catch (Exception e) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé ! "+e.getClass().getSimpleName());
				}
			}else if(args[0].equalsIgnoreCase("chat")) {
				c.sendMessageWithPrefix("Veuillez patienter pendant la recherche. Cela peut prendre du temps ...");
				
				try {
					GediminasSpyUtils.sendInfosChatToClient(c, args[1]);
				}catch (Exception e) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé ! "+e.getClass().getSimpleName());
				}
			}else if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
			}else {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>");
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <message>");
			}
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Veuillez patienter pendant la recherche. Cela peut prendre du temps ...");
				
				GediminasSpyHistory h = null;
				
				try {
					h = this.gs.getHistory(args[1]);
				}catch (Exception e) {
					h = null;
				}
				
				if(h == null) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé !");
				}else {
					List<GediminasSpyHistoryData> occ = GediminasSpyUtils.getAllMessagesContainsMessage(args[2], h);
					
					if(occ.size() == 0) {
						c.sendMessageWithPrefix("Terminé !");
						c.sendMessageWithPrefix("Aucun message trouvé pour \""+args[2]+"\".");
					}else {
						c.sendMessageWithPrefix("Terminé !");
						c.sendMessageWithPrefix("Résulats trouvés : "+occ.size()+"\n");
						
						for(GediminasSpyHistoryData hs : occ) {
							c.sendMessageWithPrefix(hs.getFormatedMessage());
						}
					}
				}
			}
		}
	}

	@Override
	public String description() {
		return "Vous permet d'avoir les informations sur un joueur en particuler.";
	}
}
