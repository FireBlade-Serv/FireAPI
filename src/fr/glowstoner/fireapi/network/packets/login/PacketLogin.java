package fr.glowstoner.fireapi.network.packets.login;

import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;

public class PacketLogin extends Packet implements Encryptable{

	private static final long serialVersionUID = -7078807062323289889L;
	
	private String password;

	public PacketLogin(String password) {
		this.setPassword(password);
	}

	public PacketLogin() {
		
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String[] encryptedFields() {
		return new String[] {"password"};
	}

	@Override
	public boolean encrypted() {
		return true;
	}
}
