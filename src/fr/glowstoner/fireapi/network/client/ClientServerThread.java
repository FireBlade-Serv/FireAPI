package fr.glowstoner.fireapi.network.client;

import java.io.EOFException;
import java.io.ObjectInputStream;

import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.packets.EncryptedPacket;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;
import fr.glowstoner.fireapi.network.packets.PacketPing;

public class ClientServerThread extends Thread {
	   
	   private ObjectInputStream in;
	   private Client c;
	   private String key;
	   
	   public ClientServerThread(String key, Client c, ObjectInputStream in) {
		   this.in = in;
	   }
	   
	   @Override
	   public void run() {
		   while(true) {
			   Object o = null;
			   
			   try {
				   try {
					   o = this.in.readObject();
				   }catch(EOFException e) {
					   try {
						   o = this.in.readObject();
					   }catch (EOFException ex) {
						   break;
					   }
				   }
				   
				   if(o != null && o instanceof Packet) {
					   Packet p = (Packet) o;
					   
					   if(p instanceof PacketPing) {
						   this.c.sendPacket(p);
					   }
					   
					   if(p instanceof EncryptedPacket) {
						   EncryptedPacket ep = (EncryptedPacket) p;
						   
						   p = new PacketEncoder(this.key).decode(ep);
					   }
					   
					   FireNetwork.getListeners().callOnPacketReceiveClientListener(p, this.c);
				   }
			   } catch (Exception e) {
				   e.printStackTrace();
				   break;
			   }
		   }
		   
		   this.interrupt();
	   	}
	}