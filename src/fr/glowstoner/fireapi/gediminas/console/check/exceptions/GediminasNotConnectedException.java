package fr.glowstoner.fireapi.gediminas.console.check.exceptions;

public class GediminasNotConnectedException extends Exception{

	private static final long serialVersionUID = 4208328111752502987L;

	public GediminasNotConnectedException() {
		super("Votre client n'est pas connecté !");
	}
	
	public GediminasNotConnectedException(String message) {
		super("Votre client n'est pas connecté ! "+message);
	}
}