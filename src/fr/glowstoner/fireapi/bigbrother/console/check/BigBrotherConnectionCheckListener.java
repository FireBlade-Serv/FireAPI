package fr.glowstoner.fireapi.bigbrother.console.check;

import fr.glowstoner.fireapi.network.ConnectionHandler;

public interface BigBrotherConnectionCheckListener {

	void onSocketChange(ConnectionHandler c);
	
}
