package fr.glowstoner.fireapi.network.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.packets.EncryptedPacket;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;

public class ClientServerThread extends Thread {
	   
	   private ObjectInputStream in;
	   private Client c;
	   
	   public ClientServerThread(Client c, ObjectInputStream in) {
		   this.in = in;
		   this.c = c;
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
					   break;
				   }
				   
				   if(o != null && o instanceof Packet) {
					   Packet p = (Packet) o;
					   
					   if(p instanceof EncryptedPacket) {
						   EncryptedPacket ep = (EncryptedPacket) p;
						   
						   p = new PacketEncoder(FireNetwork.getInstance().getKey())
								   .decode(ep);
					   }
					   
					   FireNetwork.getInstance().getListeners()
					   		.callOnPacketReceiveClientListener(p, this.c);
				   }
			   } catch (Exception e) {
				   e.printStackTrace();
				   break;
			   }
		   }
		   
		   try {
			this.c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		   
		   super.interrupt();
	   	}
	}