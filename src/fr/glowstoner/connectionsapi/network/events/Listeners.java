package fr.glowstoner.connectionsapi.network.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;

public class Listeners {
	
	private List<ServerListener> sls = new CopyOnWriteArrayList<>();
	private List<ClientListener> cls = new CopyOnWriteArrayList<>();
	
	private Map<String, CommandExecutor> cmds = new HashMap<>();
	private Map<String, List<String>> aliases = new HashMap<>();
	
	public Listeners() {
		
	}

	public void registerServerListener(ServerListener listener) {
		sls.add(listener);
	}
	
	public void callPacketReceiveServerListener(Packet packet, ConnectionHandler conn) {
		packet.setLocalConnection(conn);
		
		for(ServerListener sl : this.sls) {
			sl.onPacketReceive(packet);
		}
	}
	
	public void callOnConnectionServerListener(ConnectionHandler conn) {
		for(ServerListener sl : this.sls) {
			sl.onConnection(conn);
		}
	}
	
	public void callOnConnectionSuccessfullServerListener(ConnectionHandler connection) {
		for(ServerListener sl : this.sls) {
			sl.onConnectionSuccessfull(connection);
		}
	}
	
	public void registerClientListener(ClientListener listener) {
		this.cls.add(listener);
	}
	
	public void callOnPacketReceiveClientListener(Packet packet, ConnectionHandler local) {
		packet.setLocalConnection(local);
		
		for(ClientListener cl : this.cls) {
			cl.onPacketReceive(packet);
		}
	}
	
	public void callOnCommandServerListener(PacketCommand command) {
		for(ServerListener sl : this.sls) {
			sl.onCommand(command.getConnection(), command.getCommand(), command.getArguments());
		}
	}
	
	public void registerCommand(String command, CommandExecutor executor) {
		this.cmds.put(command, executor);
	}
	
	public void callCommand(ConnectionHandler connection, String command, String args[], String unable) {
		if(cmds.containsKey(command.toLowerCase())) {
			this.cmds.get(command).execute(connection, command, args);
		}else {
			Iterator<String> it = this.aliases.keySet().iterator();
			
			while(it.hasNext()) {
				String next = it.next();
				
				if(this.aliases.get(next).contains(command.toLowerCase())) {
					this.cmds.get(next).execute(connection, command, args);
					
					return;
				}
			}
			
			connection.sendMessageWithPrefix(unable);
		}
	}
	
	public Map<String, CommandExecutor> getCommands() {
		return this.cmds;
	}
	
	public Map<String, List<String>> getAliases() {
		return this.aliases;
	}
	
	public void addAlias(String commandbase, String alias) {
		if(this.aliases.containsKey(commandbase)) {
			this.aliases.get(commandbase).add(alias);
		}else {
			List<String> list = new ArrayList<>();
			
			list.add(alias);
			
			this.aliases.put(commandbase, list);
		}
	}
}