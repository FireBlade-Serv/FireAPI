package fr.glowstoner.fireapi.bukkit.nms.packetlistener;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class PacketInjector {
	
	private Field eppc;
	private Class<?> pc;
	private Field pcnm;

	private Class<?> network;
	private Field channel;
	private Field listener;
	
	public PacketInjector() {
		this.eppc = FireReflection.getField(FireReflection.getClass("EntityPlayer"), "playerConnection");
		this.pc = FireReflection.getClass("PlayerConnection");
		this.pcnm = FireReflection.getField(this.pc, "networkManager");
		this.network = FireReflection.getClass("NetworkManager");
		this.channel = FireReflection.getField(this.network, "channel");
		this.listener = FireReflection.getField(this.network, "m");
	}

	 public void addPlayer(Player p) {
	        try {
	        	EntityPlayer ep = ((CraftPlayer) p).getHandle();
	        	
	            Channel ch = getChannel(getNetworkManager(ep));
	            if(ch.pipeline().get("PacketInjector") == null) {
	                PacketHandler h = new PacketHandler(p);
	                ch.pipeline().addBefore("packet_handler", "PacketInjector", h);
	            }
	        } catch (Throwable t) {
	            t.printStackTrace();
	        }
	    }
	 
	 public void removePlayer(Player p) {
		 try {
			 EntityPlayer ep = ((CraftPlayer) p).getHandle();
			 
			 Channel ch = getChannel(getNetworkManager(ep));
	            
			 if(ch.pipeline().get("PacketInjector") != null) {
				 ch.pipeline().remove("PacketInjector");
			 }
		 } catch (Throwable t) {
			 t.printStackTrace();
		 }
	 }
	 
	 public Object getNetworkManager(EntityPlayer ep) {
		 return FireReflection.getFieldValue(this.pcnm, FireReflection.getFieldValue(this.eppc, ep));
	 }
	 
	 private Channel getChannel(Object networkManager) {
		 Channel ch = null;
		 
		 try {
			 ch = FireReflection.getFieldValue(this.channel, networkManager);
		 } catch (Exception e) {
			 ch = FireReflection.getFieldValue(this.listener, networkManager);
		 }
		 
		 return ch;
	}
}