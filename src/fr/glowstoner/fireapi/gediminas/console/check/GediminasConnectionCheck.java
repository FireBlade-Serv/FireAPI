package fr.glowstoner.fireapi.gediminas.console.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;
import fr.glowstoner.connectionsapi.network.packets.login.PacketLogin;
import fr.glowstoner.connectionsapi.network.packets.ping.PacketPing;
import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.gediminas.console.check.enums.GediminasConnectionCheckType;
import fr.glowstoner.fireapi.gediminas.console.check.exceptions.GediminasNotConnectedException;
import fr.glowstoner.fireapi.gediminas.console.login.GediminasConnectionInfos;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketVersion;
import lombok.Getter;

public class GediminasConnectionCheck extends TimerTask{
	
	//listener
	@Getter private List<GediminasConnectionCheckListener> listeners = new ArrayList<>();
	
	//vars base
	@Getter private Timer timer;
	@Getter private GediminasConnectionCheckType type;
	@Getter private Client client;
	
	private FireAPI api;
	private GediminasConnectionInfos infos;
	
	public GediminasConnectionCheck(FireAPI api, GediminasConnectionCheckType type, GediminasConnectionInfos infos) {
		this.api = api;
		this.client = this.api.getClient();
		this.type = type;
		this.infos = infos;
	}
	
	public void check() throws GediminasNotConnectedException {
		try {
			client.sendPacket(new PacketPing());
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new GediminasNotConnectedException("Erreur sur l'envoi d'un packet (ping).");
		}
	}
	
	public void connectionCheck() throws GediminasNotConnectedException {
		try {
			System.out.println("[FireAPI] Reconnection avec IP="+this.client.getIP()+" et PORT="+this.client.getPort());
			this.client = new Client(this.client.getIP(), this.client.getPort());
			
			this.client.start();
			
			ConnectionHandler ch = (ConnectionHandler) this.client;
			
			ch.sendPacket(new PacketLogin(this.infos.getKey(), this.infos.getPassword()));
			ch.sendPacket(new PacketVersion(this.infos.getVersionType()));
			ch.sendPacket(new PacketCommand("name "+this.infos.getId()));
			
			this.api.setClient((Client) ch);
			this.api.setChecker(this);
			
			this.callListener(ch);
		}catch (Exception ex) {
			throw new GediminasNotConnectedException("Erreur sur l'envoi d'un packet (connection protocol) "
					+ "ErreurClass = "+ex.getClass().getSimpleName()+", "+ex.getMessage());
		}
	}

	public void startChecks() {
		this.timer = new Timer();
		
		switch (this.type) {
			case GLOBAL_CHECK:
				this.timer.scheduleAtFixedRate(this, 0L, 600000L);
				
				break;
			case ERROR_CHECK:
				this.timer.scheduleAtFixedRate(this, 0L, 5000L);
				
				break;
		}
		
		this.api.setChecker(this);
	}

	@Override
	public void run() {
		switch (this.type) {
			case GLOBAL_CHECK:
				System.out.println("[FireAPI] (Gediminas) Processus de vérification de connection ...");
				
				try {
					this.check();
					
					System.out.println("[FireAPI] (Gediminas) Le processus de vérification de connection a réussi !");
				} catch (GediminasNotConnectedException e) {
					System.err.println("[FireAPI] (Gediminas) Une erreur est survenue ! Changement pour ERROR_CHECK ...");
					
					GediminasConnectionCheck check = new GediminasConnectionCheck
							(this.api, GediminasConnectionCheckType.ERROR_CHECK, this.infos);
					
					check.startChecks();
					
					this.timer.cancel();
					this.timer.purge();
				}
				
				break;
			case ERROR_CHECK:
				System.err.println("[FireAPI] (Gediminas) Impossible de se connecter ! Tentative de reconnection ...");
				
				try {
					this.connectionCheck();
					this.check();
					
					System.out.println("[FireAPI] (Gediminas) Reconnection réussie ! Changement pour GLOBAL_CHECK ...");
					
					GediminasConnectionCheck check = new GediminasConnectionCheck
							(this.api, GediminasConnectionCheckType.GLOBAL_CHECK, this.infos);
					
					check.startChecks();
					
					this.timer.cancel();
					this.timer.purge();
				} catch (GediminasNotConnectedException e) {
					System.err.println("[FireAPI] (Gediminas) La tentative de reconnection à échouée ! "+e.getClass().getSimpleName()+
							", "+e.getMessage());
				}
				
				break;
		}
	}
	
	public void registerListener(GediminasConnectionCheckListener listener) {
		this.listeners.add(listener);
	}
	
	public void callListener(ConnectionHandler ch) {
		for(GediminasConnectionCheckListener ls : this.listeners) {
			ls.onSocketChange(ch);
		}
	}
}
