package fr.glowstoner.fireapi.gediminas.ac.packet;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.PacketGediminasACType;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.PacketGediminasCheatAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.PacketGediminasTODOAC;
import lombok.Getter;
import lombok.Setter;

public class PacketGediminasAC extends Packet implements Serializable{

	private static final long serialVersionUID = -3243818038093140083L;

	@Getter @Setter private String playerName;
	@Getter @Setter private String message;
	@Getter @Setter private PacketGediminasACType type;
	@Getter @Setter private PacketGediminasTODOAC TODO;
	@Getter @Setter private PacketGediminasCheatAC cheat;
	
	public PacketGediminasAC(String name, String message, PacketGediminasACType type, PacketGediminasTODOAC todo, PacketGediminasCheatAC cheat) {
		this.setMessage(message);
		this.setType(type);
		this.setPlayerName(name);
		this.setTODO(todo);
		this.setCheat(cheat);
	}
	
	public PacketGediminasAC() {
		
	}
	
	@Override
	public boolean isCrypted() {
		return false;
	}
}
