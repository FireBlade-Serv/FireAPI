package fr.glowstoner.fireapi;

import fr.glowstoner.fireapi.bigbrother.BigBrother;
import fr.glowstoner.fireapi.bigbrother.console.client.BigBrotherClient;

public class Main {

	public static void main(String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("server")) {
				BigBrother g = new BigBrother();
				
				g.init();
			}else if(args[0].equalsIgnoreCase("client")) {
				BigBrotherClient c = new BigBrotherClient();
				c.askPassword();
				c.connect();
			}else {
				System.out.println("Argument inconnu pour "+args[0]+" !");
			}
		}else {
			System.out.println("[FireAPI] L'execution de ce plugin doit être faite par spigot ou bungeecord "
					+ "ou en utilisant les arguments BigBrother.");
		}
	}
}