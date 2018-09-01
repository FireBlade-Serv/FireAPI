package fr.glowstoner.fireapi.bigbrother;

import fr.glowstoner.fireapi.bigbrother.console.server.BigBrotherListener;
import fr.glowstoner.fireapi.bigbrother.console.server.commands.ChatCommand;
import fr.glowstoner.fireapi.bigbrother.console.server.commands.ExecuteCommand;
import fr.glowstoner.fireapi.bigbrother.console.server.commands.HelpCommand;
import fr.glowstoner.fireapi.bigbrother.console.server.commands.ListCommand;
import fr.glowstoner.fireapi.bigbrother.console.server.commands.NameCommand;
import fr.glowstoner.fireapi.bigbrother.console.server.commands.SpyCommand;
import fr.glowstoner.fireapi.bigbrother.console.server.login.BigBrotherLoginGetter;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpy;
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.FireNetwork;

public class BigBrother {
	
	private BigBrotherLoginGetter log;
	
	public BigBrother() {
		
	}
	
	public void init() {
		System.out.println("~~~~~~~~~~BigBrother~~~~~~~~~~");
		System.out.println("by Glowstoner");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		System.out.println("[BigBrother] Initialisation SPY ...");
		
		BigBrotherSpy gs = new BigBrotherSpy();
		gs.initFolder();
		
		System.out.println("[BigBrother] SPY initialisé !");
		
		System.out.println("[BigBrother] Lancement listeners ...");
		
		this.log = new BigBrotherLoginGetter();
		
		try {
			System.out.println("[BigBrother] Chargement logins getter ...");
			
			this.log.load();
			
			System.out.println("[BigBrother] La clé de sécurité utilisée est "+this.log.getKey().getKey());
			System.out.println("[BigBrother] Le mot de passe utilisé est "+this.log.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FireNetwork fn = new FireNetwork(this.log.getKey());
		
		fn.start(ConnectionType.SERVER_CONNECTION, this.log.getPassword(), false);
		
		BigBrotherListener gl = new BigBrotherListener(this.log, gs, fn.getListeners());
		
		fn.getListeners().registerServerListener(gl);
		System.out.println("[BigBrother] Listener lancés !");
		
		System.out.println("[BigBrother] Serveur BigBrother (FireNetwork) lancé !");
		
		fn.getListeners().registerCommand("chat", new ChatCommand(gl));
		fn.getListeners().registerCommand("name", new NameCommand());
		fn.getListeners().registerCommand("help", new HelpCommand(fn.getListeners()));
		fn.getListeners().registerCommand("list", new ListCommand(gl));
		fn.getListeners().registerCommand("execute", new ExecuteCommand(gl));
		fn.getListeners().registerCommand("spy", new SpyCommand(gs));
		
		fn.getListeners().addAlias("help", "aide");
		fn.getListeners().addAlias("help", "?");
		fn.getListeners().addAlias("execute", "exec");
		fn.getListeners().addAlias("execute", "ex");
	}
}
