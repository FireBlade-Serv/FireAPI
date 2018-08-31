package fr.glowstoner.fireapi.network.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.packets.Packet;

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
	
	public void callCommand(ConnectionHandler connection, String command, String args[], String unable,
			EncryptionKey key) {
		
		if(cmds.containsKey(command.toLowerCase())) {
			this.cmds.get(command).execute(connection, command, args);
		}else {
			for(String al : this.aliases.keySet()) {
				if(this.aliases.get(al).contains(command.toLowerCase())) {
					this.cmds.get(al).execute(connection, command, args);
					
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