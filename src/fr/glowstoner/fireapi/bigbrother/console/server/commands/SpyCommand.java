package fr.glowstoner.fireapi.bigbrother.console.server.commands;

import java.io.File;
import java.util.List;

import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpy;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyHistory;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyHistoryData;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyUtils;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class SpyCommand implements CommandExecutor {

	private BigBrotherSpy gs;
	
	public SpyCommand(BigBrotherSpy gs) {
		this.gs = gs;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /spy full <joueur>");
			c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
			c.sendMessageWithPrefix("Usage : /spy chat <joueur>");
			c.sendMessageWithPrefix("Usage : /spy list");
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("full")) {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>");
			}else if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
			}else if(args[0].equalsIgnoreCase("chat")) {
				c.sendMessageWithPrefix("Usage : /spy chat <joueur>");
			}else if(args[0].equalsIgnoreCase("list")) {
				File[] files = BigBrotherSpyUtils.getAllSpyFiles();
				
				if(files.length == 0) {
					c.sendMessageWithPrefix("Aucun joueur n'a été répertorié.");
				}else {
					c.sendMessageWithPrefix("Voici tout les fichiers de joueur répertoriés :");
					
					for(File f : files) {
						c.sendMessageWithPrefix("- "+f.getName());
					}
				}
			}else {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>");
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>");
				c.sendMessageWithPrefix("Usage : /spy chat <joueur>");
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("full")) {
				try {
					BigBrotherSpyUtils.sendInfosToClient(c, args[1]);
				}catch (Exception e) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé ! "+e.getClass().getSimpleName());
				}
			}else if(args[0].equalsIgnoreCase("chat")) {
				c.sendMessageWithPrefix("Veuillez patienter pendant la recherche. Cela peut prendre du temps ...");
				
				try {
					BigBrotherSpyUtils.sendInfosChatToClient(c, args[1]);
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
				
				BigBrotherSpyHistory h = null;
				
				try {
					h = this.gs.getHistory(args[1]);
				}catch (Exception e) {
					h = null;
				}
				
				if(h == null) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé !");
				}else {
					List<BigBrotherSpyHistoryData> occ = BigBrotherSpyUtils.getAllMessagesContainsMessage(args[2], h);
					
					if(occ.size() == 0) {
						c.sendMessageWithPrefix("Terminé !");
						c.sendMessageWithPrefix("Aucun message trouvé pour \""+args[2]+"\".");
					}else {
						c.sendMessageWithPrefix("Terminé !");
						c.sendMessageWithPrefix("Résulats trouvés : "+occ.size()+"\n");
						
						for(BigBrotherSpyHistoryData hs : occ) {
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