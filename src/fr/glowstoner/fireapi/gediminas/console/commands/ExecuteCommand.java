package fr.glowstoner.fireapi.gediminas.console.commands;

import java.io.IOException;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.packets.command.CommandExecutor;
import fr.glowstoner.fireapi.gediminas.console.GediminasListener;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketExecute;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class ExecuteCommand implements CommandExecutor {

	private GediminasListener gl;

	public ExecuteCommand(GediminasListener gl) {
		this.gl = gl;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /execute <spigot/bungeecord> cmd");
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("spigot")) {
				c.sendMessageWithPrefix("Usage : /execute spigot cmd");
			}else if(args[0].equalsIgnoreCase("bungeecord")) {
				c.sendMessageWithPrefix("Usage : /execute bungeecord cmd");
			}else {
				c.sendMessageWithPrefix("Usage : /execute <spigot/bungeecord> cmd");
			}
		}else {
			StringBuilder builder = new StringBuilder();
			
			for(String arg : args) {
				if(!arg.equalsIgnoreCase(args[0])) {
					builder.append(arg+" ");
				}
			}
			
			try {
				if(args[0].equalsIgnoreCase("spigot")) {
					for(ConnectionHandler ch : this.gl.getServersConnections().
							get(VersionType.SPIGOT_VERSION)) {
						
						c.sendMessageWithPrefix("Vous avez bien envoyé votre commande à "+ch.getName());
						ch.sendPacket(new PacketExecute(builder.toString()));
					}
				}else if(args[0].equalsIgnoreCase("bungeecord")) {
					for(ConnectionHandler ch : this.gl.getServersConnections().
							get(VersionType.BUNGEECORD_VERSION)) {
						
						c.sendMessageWithPrefix("Vous avez bien envoyé votre commande à "+ch.getName());
						ch.sendPacket(new PacketExecute(builder.toString()));
					}
				}else {
					c.sendMessageWithPrefix("Usage : /execute <spigot/bungeecord> cmd");
				}
			}catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public String description() {
		return "Vous permet d'executer des commandes sur les serveurs";
	}

}
