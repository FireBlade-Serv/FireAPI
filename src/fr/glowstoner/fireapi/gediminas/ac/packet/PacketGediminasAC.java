package fr.glowstoner.fireapi.gediminas.ac.packet;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.GediminasTypeAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.GediminasCheatAC;
import fr.glowstoner.fireapi.gediminas.ac.packet.enums.GediminasActionAC;

public class PacketGediminasAC extends Packet implements Serializable{

	private static final long serialVersionUID = -15043241083533282L;

	private String playerName;
	private String message;
	private GediminasTypeAC type;
	private GediminasActionAC TODO;
	private GediminasCheatAC cheat;
	private int ping;
	
	public PacketGediminasAC(String name, String message, GediminasTypeAC type, GediminasActionAC todo, GediminasCheatAC cheat, int ping) {
		this.setMessage(message);
		this.setType(type);
		this.setPlayerName(name);
		this.setTODO(todo);
		this.setCheat(cheat);
		this.setPing(ping);
	}
	
	public PacketGediminasAC() {
		
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GediminasTypeAC getType() {
		return type;
	}

	public void setType(GediminasTypeAC type) {
		this.type = type;
	}

	public GediminasActionAC getTODO() {
		return TODO;
	}

	public void setTODO(GediminasActionAC tODO) {
		TODO = tODO;
	}

	public GediminasCheatAC getCheat() {
		return cheat;
	}

	public void setCheat(GediminasCheatAC cheat) {
		this.cheat = cheat;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
