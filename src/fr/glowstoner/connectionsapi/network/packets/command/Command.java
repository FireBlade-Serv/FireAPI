package fr.glowstoner.connectionsapi.network.packets.command;

import java.io.Serializable;

public class Command implements Serializable{

	private static final long serialVersionUID = 8166504003302654046L;
	
	private String raw;
	
	//command arg0 arg1
	
	public Command(String rawcommand) {
		this.raw = rawcommand;
	}

	public String getCommand() {
		return getParsedCommand()[0];
	}
	
	public String[] getArguments() {
		//glowapi code
		String[] allargs = getParsedCommand();
        
		StringBuilder builder = new StringBuilder();
		
		for (String arg : allargs) {					
			if (!arg.equals(allargs[0])) {
				builder.append(arg + " ");
			}
		}
		
		String[] args = builder.toString().split(" ");
		
		if(args[0].equals("")) {
			args = new String[0];
		}
		
		return args;
	}
	
	public String[] getParsedCommand() {
		return this.raw.split(" ");
	}
}
