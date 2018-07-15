package fr.glowstoner.fireapi.bigbrother.console.commands;

import java.io.File;
import java.util.List;

import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpy;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyHistory;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyHistoryData;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyUtils;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;

public class SpyCommand implements CommandExecutor {

	private BigBrotherSpy gs;
	private EncryptionKey key;
	
	public SpyCommand(EncryptionKey key, BigBrotherSpy gs) {
		this.gs = gs;
		this.key = key;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /spy full <joueur>", this.key);
			c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>", this.key);
			c.sendMessageWithPrefix("Usage : /spy chat <joueur>", this.key);
			c.sendMessageWithPrefix("Usage : /spy list", this.key);
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("full")) {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>", this.key);
			}else if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>", this.key);
			}else if(args[0].equalsIgnoreCase("chat")) {
				c.sendMessageWithPrefix("Usage : /spy chat <joueur>", this.key);
			}else if(args[0].equalsIgnoreCase("list")) {
				c.sendMessageWithPrefix("Voici tout les fichiers de joueur répertoriés :", this.key);
				
				for(File f : BigBrotherSpyUtils.getAllSpyFiles()) {
					c.sendMessageWithPrefix("- "+f.getName(), this.key);
				}
			}else {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>", this.key);
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>", this.key);
				c.sendMessageWithPrefix("Usage : /spy chat <joueur>", this.key);
			}
		}else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("full")) {
				try {
					BigBrotherSpyUtils.sendInfosToClient(c, args[1], this.key);
				}catch (Exception e) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé ! "+e.getClass().getSimpleName(),
							this.key);
				}
			}else if(args[0].equalsIgnoreCase("chat")) {
				c.sendMessageWithPrefix("Veuillez patienter pendant la recherche. Cela peut prendre du temps ...",
						this.key);
				
				try {
					BigBrotherSpyUtils.sendInfosChatToClient(c, args[1], this.key);
				}catch (Exception e) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé ! "+e.getClass().getSimpleName(),
							this.key);
				}
			}else if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <format>", this.key);
			}else {
				c.sendMessageWithPrefix("Usage : /spy full <joueur>", this.key);
				c.sendMessageWithPrefix("Usage : /spy search <joueur> <message>", this.key);
			}
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("search")) {
				c.sendMessageWithPrefix("Veuillez patienter pendant la recherche. Cela peut prendre du temps ...",
						this.key);
				
				BigBrotherSpyHistory h = null;
				
				try {
					h = this.gs.getHistory(args[1]);
				}catch (Exception e) {
					h = null;
				}
				
				if(h == null) {
					c.sendMessageWithPrefix("Erreur, ce joueur n'a pas été trouvé !", this.key);
				}else {
					List<BigBrotherSpyHistoryData> occ = BigBrotherSpyUtils.getAllMessagesContainsMessage(args[2], h);
					
					if(occ.size() == 0) {
						c.sendMessageWithPrefix("Terminé !", this.key);
						c.sendMessageWithPrefix("Aucun message trouvé pour \""+args[2]+"\".", this.key);
					}else {
						c.sendMessageWithPrefix("Terminé !", this.key);
						c.sendMessageWithPrefix("Résulats trouvés : "+occ.size()+"\n", this.key);
						
						for(BigBrotherSpyHistoryData hs : occ) {
							c.sendMessageWithPrefix(hs.getFormatedMessage(), this.key);
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