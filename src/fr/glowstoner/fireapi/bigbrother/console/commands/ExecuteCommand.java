package fr.glowstoner.fireapi.bigbrother.console.commands;

import java.io.IOException;

import fr.glowstoner.fireapi.bigbrother.console.BigBrotherListener;
import fr.glowstoner.fireapi.bigbrother.console.packets.PacketExecute;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.CommandExecutor;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class ExecuteCommand implements CommandExecutor {

	private BigBrotherListener gl;
	private EncryptionKey key;

	public ExecuteCommand(BigBrotherListener gl, EncryptionKey key) {
		this.gl = gl;
		this.key = key;
	}

	@Override
	public void execute(ConnectionHandler c, String command, String[] args) {
		if(args.length == 0) {
			c.sendMessageWithPrefix("Usage : /execute <spigot/bungeecord> cmd", this.key);
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("spigot")) {
				c.sendMessageWithPrefix("Usage : /execute spigot cmd", this.key);
			}else if(args[0].equalsIgnoreCase("bungeecord")) {
				c.sendMessageWithPrefix("Usage : /execute bungeecord cmd", this.key);
			}else {
				c.sendMessageWithPrefix("Usage : /execute <spigot/bungeecord> cmd", this.key);
			}
		}else {
			StringBuilder builder = new StringBuilder();
			
			//exec bungeecord firerank set Glowstoner FIRE
			//[cmd]  [arg1]   [arg2] [arg3] [args4]  [arg5]
			
			for(String arg : args) {
				if(!arg.equals(args[0])) {
					if(arg.equals(args[(args.length - 1)])) {
						builder.append(arg);
					}else {
						builder.append(arg+" ");
					}
				}
			}
			
			try {
				if(args[0].equalsIgnoreCase("spigot")) {
					for(ConnectionHandler ch : this.gl.getServersConnections().
							get(VersionType.SPIGOT_VERSION)) {
						
						c.sendMessageWithPrefix("Vous avez bien envoyé votre commande à "+ch.getName(), this.key);
						ch.sendPacket(new PacketExecute(builder.toString()), this.key);
					}
				}else if(args[0].equalsIgnoreCase("bungeecord")) {
					for(ConnectionHandler ch : this.gl.getServersConnections().
							get(VersionType.BUNGEECORD_VERSION)) {
						
						c.sendMessageWithPrefix("Vous avez bien envoyé votre commande à "+ch.getName(), this.key);
						ch.sendPacket(new PacketExecute(builder.toString()), this.key);
					}
				}else {
					c.sendMessageWithPrefix("Usage : /execute <spigot/bungeecord> cmd", this.key);
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
