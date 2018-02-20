package fr.glowstoner.fireapi.gediminas.console.check;

import java.util.Timer;
import java.util.TimerTask;

import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;
import fr.glowstoner.connectionsapi.network.packets.login.PacketLogin;
import fr.glowstoner.connectionsapi.network.packets.ping.PacketPing;
import fr.glowstoner.fireapi.gediminas.console.check.enums.GediminasConnectionCheckType;
import fr.glowstoner.fireapi.gediminas.console.check.exceptions.GediminasNotConnectedException;
import fr.glowstoner.fireapi.gediminas.console.login.GediminasConnectionInfos;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketVersion;
import lombok.Getter;

public class GediminasConnectionCheck extends TimerTask{
	
	@Getter private Timer t;
	@Getter private GediminasConnectionCheckType type;

	private Client c;
	private GediminasConnectionInfos infos;
	
	public GediminasConnectionCheck(Client c, GediminasConnectionCheckType type, GediminasConnectionInfos infos) {
		this.c = c;
		this.type = type;
	}
	
	public void check() throws GediminasNotConnectedException {
		try {
			c.sendPacket(new PacketPing());
		}catch (Exception e) {
			throw new GediminasNotConnectedException("Erreur sur l'envoi d'un packet (ping).");
		}
	}
	
	public void connectionCheck() throws GediminasNotConnectedException {
		try {
			c.sendPacket(new PacketLogin(this.infos.getKey(), this.infos.getPassword()));
			c.sendPacket(new PacketVersion(this.infos.getVersionType()));
			c.sendPacket(new PacketCommand("name "+this.infos.getId()));
		}catch (Exception ex) {
			throw new GediminasNotConnectedException("Erreur sur l'envoi d'un packet (connection protocol)");
		}
	}

	public void startChecks() {
		this.t = new Timer();
		
		switch (this.type) {
			case GLOBAL_CHECK:
				this.t.scheduleAtFixedRate(this, 0L, 600000L);
				
				break;
			case ERROR_CHECK:
				this.t.scheduleAtFixedRate(this, 0L, 5000L);
				
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
					e.printStackTrace();
					
					GediminasConnectionCheck check = new GediminasConnectionCheck
							(this.c, GediminasConnectionCheckType.ERROR_CHECK, this.infos);
					
					check.startChecks();
					
					this.t.cancel();
				}
				
				break;
			case ERROR_CHECK:
				System.out.println("[FireAPI] (Gediminas) Impossible de se connecter ! Tentative de reconnection ...");
				
				try {
					this.connectionCheck();
					this.check();
					
					System.out.println("[FireAPI] (Gediminas) Reconnection réussie ! Changement pour GLOBAL_CKECK ...");
					
					GediminasConnectionCheck check = new GediminasConnectionCheck
							(this.c, GediminasConnectionCheckType.GLOBAL_CHECK, this.infos);
					
					check.startChecks();
					
					this.t.cancel();
				} catch (GediminasNotConnectedException e) {
					System.out.println("[FireAPI] (Gediminas) Impossible de se connecter ! Tentative de reconnection ...");
					e.printStackTrace();
				}
				
				break;
		}
	}
}
