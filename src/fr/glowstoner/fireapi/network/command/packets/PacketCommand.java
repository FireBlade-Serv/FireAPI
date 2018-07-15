package fr.glowstoner.fireapi.network.command.packets;

import java.util.Arrays;
import java.util.List;

import fr.glowstoner.fireapi.network.command.CommandParser;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;

public class PacketCommand extends Packet implements Encryptable{

	private static final long serialVersionUID = -6709951686117074657L;
	
	private String command;
	private List<String> arguments;
	
	public PacketCommand(String rawcommand) {
		CommandParser c = new CommandParser(rawcommand);
		
		this.setCommand(c.getCommand());
		this.setArguments(c.getArguments()); 
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getArguments() {
		String[] a = new String[this.arguments.size()];
		
		for(int i = 0 ; i < this.arguments.size() ; i++) {
			a[i] = this.arguments.get(i);
		}
		
		return a;
	}

	public void setArguments(String[] arguments) {
		this.arguments = Arrays.asList(arguments);
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"command", "arguments"};
	}
	
	@Override
	public boolean isCrypted() {
		return true;
	}

}
