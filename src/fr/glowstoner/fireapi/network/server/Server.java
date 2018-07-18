package fr.glowstoner.fireapi.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import fr.glowstoner.fireapi.crypto.EncryptionKey;

public class Server implements Runnable {

	private EncryptionKey key;
	private ServerSocket server;
	private Thread t;
	
	public Server(EncryptionKey key, int port) throws IOException {
		this.server = new ServerSocket(port);
	}
	
	public void start() {
		this.t = new Thread(this);
		t.start();
	}
	
	public void addConnection(Socket socket) {
		ServerClientThread c = new ServerClientThread(socket);
		
		try {
			c.open(this.key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread t = new Thread(c);
		t.setName("bigbrother-client-thread-"+c.getIP());
		t.start();
	}
	
	public void close() {
		if(this.server != null) {
			try {
				this.server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while(this.t != null) {
			try {
				this.addConnection(this.server.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}