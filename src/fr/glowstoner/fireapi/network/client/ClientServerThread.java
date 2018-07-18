package fr.glowstoner.fireapi.network.client;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.packets.EncryptedPacket;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;

public class ClientServerThread extends Thread {
	   
	   private ObjectInputStream in;
	   private Client c;
	   private EncryptionKey key;
	   
	   public ClientServerThread(EncryptionKey key, Client c, ObjectInputStream in) {
		   this.in = in;
		   this.key = key;
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
				   }catch (SocketException se) {
					   System.out.println("\n[BigBrother] Vous avez été déconnecté ("+se.getMessage()+")! Bye !");
					   break;
				   }
				   
				   if(o != null && o instanceof Packet) {
					   Packet p = (Packet) o;
					   
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
		   
		   super.interrupt();
	   	}
	}