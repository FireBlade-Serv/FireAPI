package fr.glowstoner.fireapi;

import fr.glowstoner.fireapi.gediminas.Gediminas;
import fr.glowstoner.fireapi.gediminas.console.GediminasClient;

public class Main {

	public static void main(String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("server")) {
				Gediminas g = new Gediminas(2566);
				
				g.init();
			}else if(args[0].equalsIgnoreCase("client")) {
				GediminasClient c = new GediminasClient();
				c.connect();
			}else {
				System.out.println("Argument inconnu pour "+args[0]+" !");
			}
		}else {
			System.out.println("L'execution de ce plugin doit être faite par Spigot ou Bungeecord !");
		}
	}
}