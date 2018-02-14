package fr.glowstoner.connectionsapi.network.packets.command;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;

public class PacketCommand extends Packet implements Serializable{

	private static final long serialVersionUID = -6709951686117074657L;
	
	private String command;
	private String[] arguments;
	
	public PacketCommand(String rawcommand) {
		Command c = new Command(rawcommand);
		
		setCommand(c.getCommand());
		setArguments(c.getArguments());
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getArguments() {
		return this.arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}

}
