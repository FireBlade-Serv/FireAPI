package fr.glowstoner.fireapi.network;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.network.events.Listeners;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;
import fr.glowstoner.fireapi.network.server.Server;
import lombok.Getter;
public class FireNetwork {

	public static final int BIGBROTHER_PORT = 2568;
	public static final String IP = "localhost";
	
	@Getter private Listeners listeners = new Listeners();
	@Getter private BaseConnector baseConnector;
	
	private static FireNetwork instance;
	private EncryptionKey key;
	
	public FireNetwork(EncryptionKey key) {
		instance = this;
		
		this.key = key;
	}
	
	public void start(ConnectionType type, String password, boolean sleepTime) {
		try {
			switch (type) {
				case CLIENT_CONNECTION:
					Client c = new Client(IP, BIGBROTHER_PORT);
					
					this.baseConnector = c;
					
					c.open();
					
					c.sendPacket(new PacketLogin(password));
					
					System.out.println("[BigBrother] Connection initiale client -> serveur ... "
							+ "(sleep: "+sleepTime+")");
					
					if(sleepTime) {
						Thread.sleep(120l);
					}
					
					break;
				case SERVER_CONNECTION:
					Server s = new Server(BIGBROTHER_PORT);
					
					this.baseConnector = s;
					
					s.open();
					
					System.out.println("[BigBrother] Connection initiale serveur -> client ... ");
					
					break;
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public EncryptionKey getKey() {
		return this.key;
	}
	
	public static FireNetwork getInstance() {
		if(instance == null)
			throw new NullPointerException
			("Aucune instance FireNetwork locale n'a encore été créée !");
		
		return instance;
	}
}
