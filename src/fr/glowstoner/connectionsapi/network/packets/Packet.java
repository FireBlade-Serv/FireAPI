package fr.glowstoner.connectionsapi.network.packets;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;

public abstract class Packet {
	
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
		return (this.local == null) ? false : true;
	}
	
	public abstract boolean isCrypted();
	
}