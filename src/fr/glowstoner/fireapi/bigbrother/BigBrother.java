package fr.glowstoner.fireapi.bigbrother;

import java.io.IOException;

import fr.glowstoner.fireapi.bigbrother.console.BigBrotherListener;
import fr.glowstoner.fireapi.bigbrother.console.commands.ChatCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.ExecuteCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.HelpCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.ListCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.NameCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.SpyCommand;
import fr.glowstoner.fireapi.bigbrother.console.login.BigBrotherLoginGetter;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpy;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.events.Listeners;
import fr.glowstoner.fireapi.network.server.Server;

public class BigBrother {
	
	private BigBrotherLoginGetter log;
	private int port;
	private Server server;
	
	public BigBrother(int port) {
		this.port = port;
	}
	
	public void init() {
		FireNetwork.init();
		
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
		
		BigBrotherListener gl = new BigBrotherListener(this.log, gs, getListeners());
		
		getListeners().registerServerListener(gl);
		System.out.println("[BigBrother] Listener lancés !");
		
		try {
			System.out.println("[BigBrother] Lancement du serveur BigBrother ...");
			this.server = new Server(this.log.getKey(), this.port);
			System.out.println("[BigBrother] Serveur BigBrother lancé !");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.server.start();
		
		getListeners().registerCommand("chat", new ChatCommand(this.log.getKey()));
		getListeners().registerCommand("name", new NameCommand(this.log.getKey()));
		getListeners().registerCommand("help", new HelpCommand(this.log.getKey(), this.getListeners()));
		getListeners().registerCommand("list", new ListCommand(this.log.getKey(), gl));
		getListeners().registerCommand("execute", new ExecuteCommand(gl, this.log.getKey()));
		getListeners().registerCommand("spy", new SpyCommand(this.log.getKey(), gs));
		
		getListeners().addAlias("help", "aide");
		getListeners().addAlias("help", "?");
		getListeners().addAlias("execute", "exec");
		getListeners().addAlias("execute", "ex");
	}
	
	public Listeners getListeners() {
		return FireNetwork.getListeners();
	}

	public int getPort() {
		return port;
	}
	
	public Server getServer() {
		return this.server;
	}
}
