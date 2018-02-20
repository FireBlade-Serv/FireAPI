package fr.glowstoner.fireapi.gediminas.console.check;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;

public interface GediminasConnectionCheckListener {

	void onSocketChange(ConnectionHandler c);
	
}
