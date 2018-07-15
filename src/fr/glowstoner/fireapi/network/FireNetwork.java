package fr.glowstoner.fireapi.network;

import fr.glowstoner.fireapi.network.events.Listeners;

public class FireNetwork {

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
