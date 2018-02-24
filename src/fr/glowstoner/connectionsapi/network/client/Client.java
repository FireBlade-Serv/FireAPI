package fr.glowstoner.connectionsapi.network.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.ConnectionType;
import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.PacketPing;

public class Client extends ConnectionHandler implements Serializable{
	
	private static final long serialVersionUID = -6666814765157664167L;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client(String ip, int port) throws IOException{
		socket = new Socket(ip, port);
		
		super.setSocket(this.socket);
	}
	
	@Override
	public void sendPacket(Packet packet) throws IOException {
		out.writeObject(packet);
		out.flush();
	}

   public void start() throws IOException {  
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      
      ClientServerThread re = new ClientServerThread(this.in);
      re.start();
   }
      
   @Override
   public void close() throws IOException {
	   if(socket != null)  socket.close();
	   if(in != null) in.close();
	   if(out != null) out.close();
   }
   
   public class ClientServerThread extends Thread {
	   
	   private ObjectInputStream in;
	   private boolean active;
	   
	   public ClientServerThread(ObjectInputStream in) {
		   this.in = in;
		   this.active = true;
	   }
	   
	   @Override
	   public void run() {
		   while(this.active) {
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
						   Client.this.sendPacket(p);
					   }
					   
					  ConnectionsAPI.getListeners().callOnPacketReceiveClientListener(p, Client.this);
				   }
			   } catch (Exception e) {
				   e.printStackTrace();
				   
				   active = false;
			   }
		   }
		   
		   this.interrupt();
	   	}
   	}

   @Override
   public ConnectionType type() {
	   return ConnectionType.SERVER_CONNECTION;
   }
}