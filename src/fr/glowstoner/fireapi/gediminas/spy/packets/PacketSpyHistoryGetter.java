package fr.glowstoner.fireapi.gediminas.spy.packets;

import java.io.Serializable;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyHistory;

public class PacketSpyHistoryGetter extends Packet implements Serializable{

	private static final long serialVersionUID = -7760382847904238148L;

	private GediminasSpyHistory history;
	
	public PacketSpyHistoryGetter(GediminasSpyHistory history) {
		this.setHistory(history);
	}
	
	public GediminasSpyHistory getHistory() {
		return history;
	}

	public void setHistory(GediminasSpyHistory history) {
		this.history = history;
	}

	@Override
	public boolean isCrypted() {
		return false;
	}

}
