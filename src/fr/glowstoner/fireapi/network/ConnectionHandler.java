package fr.glowstoner.fireapi.network;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketPing;
import fr.glowstoner.fireapi.network.packets.PacketText;
import fr.glowstoner.fireapi.network.packets.login.enums.LoginResult;

public abstract class ConnectionHandler implements Serializable{

	private static final long serialVersionUID = -3902356770010744218L;

	private Socket socket;
	private ConnectionType conntype;
	private LoginResult lr;
	private String surname = "default-name";
	
	public ConnectionHandler(Socket socket) {
		this.conntype = this.type();
		this.eval();
		this.socket = socket;
	}
	
	public ConnectionHandler() {
		this.conntype = this.type();
		this.eval();
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
	
	public void sendMessage(String text, EncryptionKey key) {
		try {
			this.sendPacket(new PacketText(text), key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageWithPrefix(String text, EncryptionKey key) {
		this.sendMessage("[BigBrother] "+text, key);
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
	
	public abstract void sendPacket(Encryptable packet, EncryptionKey key) throws IOException;
	
	public abstract void close() throws IOException;
	
	public abstract void open(EncryptionKey key) throws IOException;
	
	public abstract ConnectionType type();
}
