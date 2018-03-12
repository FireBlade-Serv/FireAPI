package fr.glowstoner.fireapi.bigbrother;

import java.io.IOException;

import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.events.Listeners;
import fr.glowstoner.connectionsapi.network.server.Server;
import fr.glowstoner.fireapi.bigbrother.console.BigBrotherListener;
import fr.glowstoner.fireapi.bigbrother.console.commands.ChatCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.ExecuteCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.HelpCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.ListCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.NameCommand;
import fr.glowstoner.fireapi.bigbrother.console.commands.SpyCommand;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpy;

public class BigBrother {
	
	private int port;
	private Server server;
	
	public BigBrother(int port) {
		this.port = port;
	}
	
	public void init() {
		ConnectionsAPI.init();
		
		System.out.println("~~~~~~~~~~BigBrother~~~~~~~~~~");
		System.out.println("by Glowstoner");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		System.out.println("[BigBrother] Initialisation SPY ...");
		
		BigBrotherSpy gs = new BigBrotherSpy();
		gs.initFolder();
		
		System.out.println("[BigBrother] SPY initialisé !");
		
		System.out.println("[BigBrother] Lancement listeners ...");
		
		BigBrotherListener gl = new BigBrotherListener(gs, getListeners());
		
		getListeners().registerServerListener(gl);
		System.out.println("[BigBrother] Listener lancés !");
		
		try {
			System.out.println("[BigBrother] Lancement du serveur BigBrother ...");
			this.server = new Server(this.port);
			System.out.println("[BigBrother] Serveur BigBrother lancé !");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.server.start();
		
		getListeners().registerCommand("chat", new ChatCommand());
		getListeners().registerCommand("name", new NameCommand());
		getListeners().registerCommand("help", new HelpCommand(this.getListeners()));
		getListeners().registerCommand("list", new ListCommand(gl));
		getListeners().registerCommand("execute", new ExecuteCommand(gl));
		getListeners().registerCommand("spy", new SpyCommand(gs));
		
		getListeners().addAlias("help", "aide");
		getListeners().addAlias("help", "?");
		getListeners().addAlias("execute", "exec");
		getListeners().addAlias("execute", "ex");
	}
	
	public Listeners getListeners() {
		return ConnectionsAPI.getListeners();
	}

	public int getPort() {
		return port;
	}
	
	public Server getServer() {
		return this.server;
	}
}
