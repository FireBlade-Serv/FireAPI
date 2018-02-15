package fr.glowstoner.fireapi.gediminas.console;

import java.io.IOException;

import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.events.Listeners;
import fr.glowstoner.connectionsapi.network.server.Server;
import fr.glowstoner.fireapi.gediminas.console.commands.ChatCommand;
import fr.glowstoner.fireapi.gediminas.console.commands.ExecuteCommand;
import fr.glowstoner.fireapi.gediminas.console.commands.HelpCommand;
import fr.glowstoner.fireapi.gediminas.console.commands.ListCommand;
import fr.glowstoner.fireapi.gediminas.console.commands.NameCommand;
import fr.glowstoner.fireapi.gediminas.console.commands.StatusCommand;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpy;

public class Gediminas {
	
	private int port;
	private Server server;
	
	public Gediminas(int port) {
		this.port = port;
	}
	
	public void init() {
		ConnectionsAPI.init();
		
		System.out.println("~~~~~~~~~~Gediminas~~~~~~~~~~");
		System.out.println("by Glowstoner");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		GediminasSpy gs = new GediminasSpy();
		gs.initFolder();
		
		System.out.println("[Gediminas] Lancement listeners ...");
		
		GediminasListener gl = new GediminasListener(gs, getListeners());
		
		getListeners().registerServerListener(gl);
		System.out.println("[Gediminas] Listener lancés !");
		
		try {
			System.out.println("[Gediminas] Lancement du serveur Gediminas ...");
			this.server = new Server(this.port);
			System.out.println("[Gediminas] Serveur Gediminas lancé !");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.server.start();
		
		getListeners().registerCommand("chat", new ChatCommand());
		getListeners().registerCommand("name", new NameCommand());
		getListeners().registerCommand("help", new HelpCommand(this.getListeners()));
		getListeners().registerCommand("status", new StatusCommand());
		getListeners().registerCommand("list", new ListCommand(gl));
		getListeners().registerCommand("execute", new ExecuteCommand(gl));
		
		getListeners().addAlias("help", "aide");
		getListeners().addAlias("help", "?");
		getListeners().addAlias("execute", "exec");
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
