package fr.glowstoner.connectionsapi.network.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.ConnectionType;
import fr.glowstoner.connectionsapi.network.packets.Packet;

public class Server implements Runnable {

	private ServerSocket server;
	private Thread t;
	
	public Server(int port) throws IOException {
		this.server = new ServerSocket(port);
	}
	
	public void start() {
		this.t = new Thread(this);
		t.start();
	}
	
	public void addConnection(Socket socket) {
		ServerClientThread c = new ServerClientThread(socket);
		
		try {
			c.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread t = new Thread(c);
		t.start();
	}
	
	public void close() {
		if(this.server != null) {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while(t != null) {
			try {
				addConnection(this.server.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class ServerClientThread extends ConnectionHandler implements Runnable, Serializable{
		
		private static final long serialVersionUID = -3378783954703324349L;
		
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		public ServerClientThread(Socket socket) {
			super(socket);
			
			this.socket = socket;
		}
		
		public void open() throws IOException {
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
			
			ConnectionsAPI.getListeners().callOnConnectionServerListener(this);
		}
		
		@Override
		public void close() throws IOException {			
			if(socket != null) socket.close();
			if(in != null) in.close();
			if(out != null) out.close();
		}
		
		public Socket getSocket() {
			return this.socket;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					Object o = null;
					
					try {
						o = this.in.readObject();
					}catch (EOFException ex) {
						break;
					}catch (OptionalDataException ex) {
						System.out.println("[Gediminas] OptionnalDataException détécté !");
						System.out.println("[Gediminas] eof = "+ex.eof+", length = "+ex.length);
						ex.printStackTrace();
					}
					
					if(o != null && o instanceof Packet) {
						Packet p = (Packet) o;
						
						ConnectionsAPI.getListeners().callPacketReceiveServerListener(p, this);
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					break;
				}
			}
			
			try {
				close();
			} catch (IOException e) {
				return;
			}
		}

		@Override
		public void sendPacket(Packet packet) throws IOException {
			this.out.writeObject(packet);
			this.out.flush();
		}

		@Override
		public ConnectionType type() {
			return ConnectionType.CLIENT_CONNECTION;
		}
	}
}