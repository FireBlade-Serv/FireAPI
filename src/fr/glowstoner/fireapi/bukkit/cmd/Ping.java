package fr.glowstoner.fireapi.bukkit.cmd;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.PacketPlayerPing;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.enums.PingState;

public class Ping implements CommandExecutor {
	
	private ConnectionHandler c;
	
	public Ping(ConnectionHandler c) {
		this.c = c;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			int ping = ((CraftPlayer) p).getHandle().ping;
			
			p.sendMessage("§6[§ePing§6]§r Ton ping §eserveur§r est de §e"+ping+" ms§r !");
			
			try {
				c.sendPacket(new PacketPlayerPing(p.getName(), PingState.INIT_SERVER));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

}
