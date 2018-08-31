package fr.glowstoner.fireapi.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import fr.glowstoner.fireapi.network.BaseConnector;

public class Server implements BaseConnector, Runnable {

	private ServerSocket server;
	private Thread t;
	
	public Server(int port) throws IOException {
		this.server = new ServerSocket(port);
	}
	
	@Override
	public void open() {
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
		t.setName("bigbrother-client-thread-"+c.getIP());
		t.start();
	}
	
	@Override
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