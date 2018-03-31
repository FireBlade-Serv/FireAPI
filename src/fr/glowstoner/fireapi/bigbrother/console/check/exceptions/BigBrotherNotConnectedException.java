package fr.glowstoner.fireapi.bigbrother.console.check.exceptions;

public class BigBrotherNotConnectedException extends Exception{

	private static final long serialVersionUID = 4208328111752502987L;

	public BigBrotherNotConnectedException() {
		super("Votre client n'est pas connecté !");
	}
	
	public BigBrotherNotConnectedException(String message) {
		super("Votre client n'est pas connecté ! "+message);
	}
}