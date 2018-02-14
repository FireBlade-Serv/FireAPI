package fr.glowstoner.fireapi.bungeecord.events;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.plugin.Event;

public class FireChannelReceiveEvent extends Event{
	
	private byte[] data;
	private Connection sender, receiver;

	//[MessageObject]-{Message}
	public FireChannelReceiveEvent(byte[] data, Connection sender, Connection receiver) {
		this.data = data;
		this.sender = sender;
		this.receiver = receiver;
	}

	public byte[] getData() {
		return this.data;
	}
	
	public Connection getSender() {
		return this.sender;
	}
	
	public Connection getReceiver() {
		return this.receiver;
	}
	
	public String getFullMessage() throws IOException {
		ByteArrayInputStream stream = new ByteArrayInputStream(this.data);
		DataInputStream in = new DataInputStream(stream);
		
		return in.readUTF();
	}
	
	public String getMessageObject() throws IOException {
		String[] det = getFullMessage().split("-");
		
		for(String part : det) {
			if(part.startsWith("[") && part.endsWith("]")) {
				return part.substring(1, part.length() - 1);
			}
		}
		
		return null;
	}
	
	public String getMessageData() throws IOException {
		String[] det = getFullMessage().split("-");
		
		for(String part : det) {
			if(part.startsWith("{") && part.endsWith("}")) {
				return part.substring(1, part.length() - 1);
			}
		}
		
		return null;
	}
}
