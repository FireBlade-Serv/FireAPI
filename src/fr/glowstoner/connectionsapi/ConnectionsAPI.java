package fr.glowstoner.connectionsapi;

import fr.glowstoner.connectionsapi.network.events.Listeners;

public class ConnectionsAPI {

	private static Listeners l;
	
	public static void init() {
		if(l == null) {
			l = new Listeners();
		}
	}
	
	public static Listeners getListeners() {
		return l;
	}
	
}
