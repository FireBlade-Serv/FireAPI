package fr.glowstoner.fireapi.network;

import java.io.IOException;

//représente le connecteur de base class server/client
public interface BaseConnector {
	
	void open() throws IOException;
	void close() throws IOException;
	
}
