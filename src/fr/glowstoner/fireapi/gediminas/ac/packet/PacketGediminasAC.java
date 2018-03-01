package fr.glowstoner.fireapi.gediminas.ac.packet;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import lombok.Getter;
import lombok.Setter;

public class PacketGediminasAC extends Packet implements Serializable{

	private static final long serialVersionUID = -3243818038093140083L;

	@Getter @Setter private String playerName;
	@Getter @Setter private String message;
	@Getter @Setter private EnumPacketGediminasACType type;
	@Getter @Setter private EnumPacketGediminasACTODO TODO;
	
	public PacketGediminasAC(String name, String message, EnumPacketGediminasACType type, EnumPacketGediminasACTODO todo) {
		this.setMessage(message);
		this.setType(type);
		this.setPlayerName(name);
		this.setTODO(todo);
	}
	
	public PacketGediminasAC() {
		
	}
	
	@Override
	public boolean isCrypted() {
		return false;
	}

	public enum EnumPacketGediminasACType{
		
		CHEAT_DETECTION;
		
	}
	
	public enum EnumPacketGediminasACTODO{
		
		INFORM_STAFF, BAN;
		
	}
}
