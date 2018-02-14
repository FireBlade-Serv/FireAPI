package fr.glowstoner.fireapi.player;

public class InvalidVersionTypeException extends Exception{

	private static final long serialVersionUID = -6632917827963142464L;

	public InvalidVersionTypeException(String msg) {
		super(msg);
	}
	
	public InvalidVersionTypeException() {
		
	}
}
