package fr.glowstoner.fireapi.bigbrother.console.packets;

import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;

public class PacketExecute extends Packet implements Encryptable{

	private static final long serialVersionUID = -5731791793346143581L;

	private String serverCommand;
	
	public PacketExecute(String serverCommand) {
		this.setServerCommand(serverCommand);
	}
	
	public PacketExecute() {
		
	}
	
	public String getServerCommand() {
		return serverCommand;
	}

	public void setServerCommand(String serverCommand) {
		this.serverCommand = serverCommand;
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"serverCommand"};
	}
	
	@Override
	public boolean isCrypted() {
		return true;
	}
}
