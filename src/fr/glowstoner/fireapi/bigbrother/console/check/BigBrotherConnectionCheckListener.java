package fr.glowstoner.fireapi.bigbrother.console.check;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;

public interface BigBrotherConnectionCheckListener {

	void onSocketChange(ConnectionHandler c);
	
}
