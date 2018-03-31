package fr.glowstoner.fireapi;

import fr.glowstoner.fireapi.bigbrother.BigBrother;
import fr.glowstoner.fireapi.bigbrother.console.BigBrotherClient;

public class Main {

	public static void main(String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("server")) {
				BigBrother g = new BigBrother(2566);
				
				g.init();
			}else if(args[0].equalsIgnoreCase("client")) {
				BigBrotherClient c = new BigBrotherClient();
				c.connect();
			}else {
				System.out.println("Argument inconnu pour "+args[0]+" !");
			}
		}else {
			System.out.println("L'execution de ce plugin doit être faite par Spigot ou Bungeecord !");
		}
	}
}