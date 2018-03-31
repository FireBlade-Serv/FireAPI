package fr.glowstoner.fireapi.bigbrother.spy.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyHistory;
import fr.glowstoner.fireapi.bigbrother.spy.packets.enums.BigBrotherSpyHistoryGetterState;

public class PacketSpyHistoryGetter extends Packet implements Serializable{

	private static final long serialVersionUID = -7760382847904238148L;

	private BigBrotherSpyHistory history;
	private BigBrotherSpyHistoryGetterState state;
	private String playerName, source;
	
	public PacketSpyHistoryGetter(BigBrotherSpyHistory history,
			BigBrotherSpyHistoryGetterState state, String name, String target) {
		
		this.setHistory(history);
		this.setState(state);
		this.setPlayerName(name);
	}
	
	public BigBrotherSpyHistory getHistory() {
		return history;
	}

	public void setHistory(BigBrotherSpyHistory history) {
		this.history = history;
	}

	public BigBrotherSpyHistoryGetterState getState() {
		return state;
	}

	public void setState(BigBrotherSpyHistoryGetterState state) {
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
