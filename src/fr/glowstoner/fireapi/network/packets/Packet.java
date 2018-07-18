package fr.glowstoner.fireapi.network.packets;

import java.io.Serializable;

import fr.glowstoner.fireapi.network.ConnectionHandler;

public abstract class Packet implements Serializable{
	
	private static final long serialVersionUID = -400610387591075416L;
	
	private ConnectionHandler local;
	
	public Packet() {
		
	}
	
	public ConnectionHandler getConnection() {
		return this.local;
	}
	
	public void setLocalConnection(ConnectionHandler connection) {
		this.local = connection;
	}
	
	public boolean hasLocalConnection() {
		return this.local != null;
	}
	
	public abstract boolean isCrypted();
}