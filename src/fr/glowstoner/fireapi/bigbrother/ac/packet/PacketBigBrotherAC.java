package fr.glowstoner.fireapi.bigbrother.ac.packet;

import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherActionAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherCheatAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherTypeAC;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;

public class PacketBigBrotherAC extends Packet implements Encryptable{

	private static final long serialVersionUID = -15043241083533282L;

	private String playerName;
	private String message;
	private BigBrotherTypeAC type;
	private BigBrotherActionAC TODO;
	private BigBrotherCheatAC cheat;
	private int ping;
	
	public PacketBigBrotherAC(String name, String message, BigBrotherTypeAC type,
			BigBrotherActionAC todo, BigBrotherCheatAC cheat, int ping) {
		
		this.setMessage(message);
		this.setType(type);
		this.setPlayerName(name);
		this.setTODO(todo);
		this.setCheat(cheat);
		this.setPing(ping);
	}
	
	public PacketBigBrotherAC() {
		
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

	public BigBrotherTypeAC getType() {
		return type;
	}

	public void setType(BigBrotherTypeAC type) {
		this.type = type;
	}

	public BigBrotherActionAC getTODO() {
		return TODO;
	}

	public void setTODO(BigBrotherActionAC tODO) {
		TODO = tODO;
	}

	public BigBrotherCheatAC getCheat() {
		return cheat;
	}

	public void setCheat(BigBrotherCheatAC cheat) {
		this.cheat = cheat;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"playerName", "message", "type", "TODO", "cheat", "ping"};
	}
	
	@Override
	public boolean encrypted() {
		return true;
	}
}
