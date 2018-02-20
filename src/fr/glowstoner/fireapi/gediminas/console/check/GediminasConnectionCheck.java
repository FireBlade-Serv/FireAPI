package fr.glowstoner.fireapi.gediminas.console.check;

import java.util.Timer;
import java.util.TimerTask;

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
			
			this.client.sendPacket(new PacketLogin(this.infos.getKey(), this.infos.getPassword()));
			this.client.sendPacket(new PacketVersion(this.infos.getVersionType()));
			this.client.sendPacket(new PacketCommand("name "+this.infos.getId()));
			
			this.api.setClient(this.client);
		}catch (Exception ex) {
			throw new GediminasNotConnectedException("Erreur sur l'envoi d'un packet (connection protocol) "
					+ "ErreurClass = "+ex.getClass().getSimpleName());
		}
	}

	public void startChecks() {
		this.timer = new Timer();
		
		switch (this.type) {
			case GLOBAL_CHECK:
				this.timer.scheduleAtFixedRate(this, 0L, 10000L);
				
				break;
			case ERROR_CHECK:
				this.timer.scheduleAtFixedRate(this, 0L, 5000L);
				
				break;
		}
	}

	@Override
	public void run() {
		switch (this.type) {
			case GLOBAL_CHECK:
				System.out.println("[FireAPI] (Gediminas) Processus de vérification de connection ...");
				
				try {
					this.check();
					
					System.out.println("[FireAPI] (Gediminas) Test de connection réussi !");
				} catch (GediminasNotConnectedException e) {
					System.out.println("[FireAPI] (Gediminas) Une erreur est survenue ! Changement pour ERROR_CHECK ...");
					
					GediminasConnectionCheck check = new GediminasConnectionCheck
							(this.api, GediminasConnectionCheckType.ERROR_CHECK, this.infos);
					
					check.startChecks();
					
					this.timer.cancel();
				}
				
				break;
			case ERROR_CHECK:
				System.out.println("[FireAPI] (Gediminas) Impossible de se connecter ! Tentative de reconnection ...");
				
				try {
					this.connectionCheck();
					this.check();
					
					System.out.println("[FireAPI] (Gediminas) Reconnection réussie ! Changement pour GLOBAL_CKECK ...");
					
					GediminasConnectionCheck check = new GediminasConnectionCheck
							(this.api, GediminasConnectionCheckType.GLOBAL_CHECK, this.infos);
					
					check.startChecks();
					
					this.timer.cancel();
				} catch (GediminasNotConnectedException e) {
					System.out.println("[FireAPI] (Gediminas) Impossible de se connecter ! Tentative de reconnection ...");
				}
				
				break;
		}
	}
}
