package fr.glowstoner.fireapi.network;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketPing;
import fr.glowstoner.fireapi.network.packets.PacketText;
import fr.glowstoner.fireapi.network.packets.login.enums.LoginResult;

public abstract class ConnectionHandler implements Serializable{

	private static final long serialVersionUID = -3902356770010744218L;

	protected Socket socket;
	
	private LoginResult lr = LoginResult.NOT_LOGGED;
	private String surname = "default-name";
	
	public ConnectionHandler(Socket socket) {
		this.socket = socket;
	}
	
	public ConnectionHandler() {
		
	}
	
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public String getIP() {
		return this.socket.getInetAddress().getHostAddress();
	}
	
	public int getPort() {
		return this.socket.getPort();
	}
	
	public LoginResult isLogged() {
		return this.lr;
	}
	
	public void setLoginResult(LoginResult lr) {
		this.lr = lr;
	}
	
	public void sendMessage(String text) {
		try {
			this.sendPacket(new PacketText(text));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageWithPrefix(String text) {
		this.sendMessage("[BigBrother] "+text);
	}
	
	public String getName() {
		return surname;
	}

	public void setName(String surname) {
		this.surname = surname;
	}
	
	public boolean isConnectedOrValid() {
		try {			
			this.sendPacket(new PacketPing());
			
			return true;
		}catch (Exception ex) {
			return false;
		}
	}

	public abstract void sendPacket(Packet packet) throws IOException;
	
	public abstract void close() throws IOException;
	
	public abstract void open() throws IOException;
	
	public abstract ConnectionType type();
}
