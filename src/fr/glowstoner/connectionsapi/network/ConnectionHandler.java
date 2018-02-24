package fr.glowstoner.connectionsapi.network;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.PacketPing;
import fr.glowstoner.connectionsapi.network.packets.PacketText;
import fr.glowstoner.connectionsapi.network.packets.login.enums.LoginResult;

public abstract class ConnectionHandler implements Serializable{

	private static final long serialVersionUID = -3902356770010744218L;

	private Socket socket;
	private ConnectionType conntype;
	private LoginResult lr;
	private String surname = "default-name";
	
	public ConnectionHandler(Socket socket) {
		this.conntype = this.type();
		
		if(this.conntype.equals(ConnectionType.SERVER_CONNECTION)) {
			this.lr = LoginResult.SERVER;
		}else {
			this.lr = LoginResult.NOT_LOGGED;
		}
		
		this.socket = socket;
	}
	
	public ConnectionHandler() {
		this.conntype = this.type();
		
		if(this.conntype.equals(ConnectionType.SERVER_CONNECTION)) {
			this.lr = LoginResult.SERVER;
		}else {
			this.lr = LoginResult.NOT_LOGGED;
		}
	}
	
	public void eval() {
		this.conntype = this.type();
		
		if(this.conntype.equals(ConnectionType.SERVER_CONNECTION)) {
			this.lr = LoginResult.SERVER;
		}else {
			this.lr = LoginResult.NOT_LOGGED;
		}
	}
	
	public void setSocket(Socket socket) {
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
	
	public ConnectionType getType() {
		return this.conntype;
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
		sendMessage("[Gediminas] "+text);
	}
	
	public String getName() {
		return surname;
	}

	public void setName(String surname) {
		this.surname = surname;
	}
	
	public boolean isConnectedOrValid() {
		try {
			PacketPing ping = new PacketPing();
			
			this.sendPacket(ping);
			
			return true;
		}catch (Exception ex) {
			return false;
		}
	}

	public abstract void sendPacket(Packet packet) throws IOException;
	
	public abstract void close() throws IOException;
	
	public abstract ConnectionType type();
}
