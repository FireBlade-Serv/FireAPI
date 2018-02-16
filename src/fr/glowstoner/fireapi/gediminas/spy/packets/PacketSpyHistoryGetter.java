package fr.glowstoner.fireapi.gediminas.spy.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyHistory;
import fr.glowstoner.fireapi.gediminas.spy.packets.enums.GediminasSpyHistoryGetterState;

public class PacketSpyHistoryGetter extends Packet implements Serializable{

	private static final long serialVersionUID = -7760382847904238148L;

	private GediminasSpyHistory history;
	private GediminasSpyHistoryGetterState state;
	private String playerName, source;
	
	public PacketSpyHistoryGetter(GediminasSpyHistory history,
			GediminasSpyHistoryGetterState state, String name, String target) {
		
		this.setHistory(history);
		this.setState(state);
		this.setPlayerName(name);
	}
	
	public GediminasSpyHistory getHistory() {
		return history;
	}

	public void setHistory(GediminasSpyHistory history) {
		this.history = history;
	}

	public GediminasSpyHistoryGetterState getState() {
		return state;
	}

	public void setState(GediminasSpyHistoryGetterState state) {
		this.state = state;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getSourcePlayer() {
		return this.source;
	}
	
	public void setSourcePlayer(String source) {
		this.source = source;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}

}
