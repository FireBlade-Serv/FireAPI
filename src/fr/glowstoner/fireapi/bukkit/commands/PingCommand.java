package fr.glowstoner.fireapi.bukkit.commands;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bigbrother.console.packets.ping.PacketPlayerPing;
import fr.glowstoner.fireapi.bigbrother.console.packets.ping.enums.PingState;

public class PingCommand implements CommandExecutor {
	
	private ConnectionHandler c;
	
	public PingCommand(FireAPI api) {
		this.c = api.getClient(); 
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
		}else {
			sender.sendMessage("§6[§ePing§6]§r §cErreur, vous devez être en jeu pour pouvoir executer cette commande !");
		}
		
		return false;
	}

}
