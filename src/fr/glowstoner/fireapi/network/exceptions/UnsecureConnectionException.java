package fr.glowstoner.fireapi.network.exceptions;

import java.io.IOException;

public class UnsecureConnectionException extends IOException{

	private static final long serialVersionUID = -5121806267296480415L;

	public UnsecureConnectionException(String packetName) {
		super("Vous venez d'établir une connection non-cryptée sur un packet sensible ! "+packetName);
	}
}
