package fr.glowstoner.fireapi.bigbrother.console.server.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bigbrother.console.server.check.enums.BigBrotherConnectionCheckType;
import fr.glowstoner.fireapi.bigbrother.console.server.check.exceptions.BigBrotherNotConnectedException;
import fr.glowstoner.fireapi.bigbrother.console.server.login.BigBrotherConnectionInfos;
import fr.glowstoner.fireapi.bigbrother.console.server.packets.PacketVersion;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.packets.PacketPing;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;
import lombok.Getter;

public class BigBrotherConnectionCheck extends TimerTask{
	
	//listener
	private static List<BigBrotherConnectionCheckListener> listeners = new ArrayList<>();
	
	//vars base
	@Getter private Timer timer;
	@Getter private BigBrotherConnectionCheckType type;
	@Getter private Client client;
	
	private FireAPI api;
	private BigBrotherConnectionInfos infos;
	
	public BigBrotherConnectionCheck(FireAPI api, BigBrotherConnectionCheckType type, BigBrotherConnectionInfos infos) {
		this.api = api;
		this.client = this.api.getClient();
		this.type = type;
		this.infos = infos;
	}
	
	public void check() throws BigBrotherNotConnectedException {
		try {
			this.client.sendPacket(new PacketPing());
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new BigBrotherNotConnectedException("Erreur sur l'envoi d'un packet (ping).");
		}
	}
	
	public void connectionCheck() throws BigBrotherNotConnectedException {
		try {
			System.out.println("[FireAPI] Reconnection avec IP="+this.client.getIP()+" et PORT="+this.client.getPort());
			this.client = new Client(this.client.getIP(), this.client.getPort());
			
			this.client.open(this.infos.getKey());
			
			ConnectionHandler ch = (ConnectionHandler) this.client;
			
			ch.sendPacket(new PacketLogin(this.infos.getPassword()), this.infos.getKey());
			ch.sendPacket(new PacketVersion(this.infos.getVersionType()), this.infos.getKey());
			ch.sendPacket(new PacketCommand("name "+this.infos.getId()), this.infos.getKey());
			
			this.api.setClient((Client) ch);
			this.api.setChecker(this);
			
			this.callListener(ch);
		}catch (Exception ex) {
			throw new BigBrotherNotConnectedException("Erreur sur l'envoi d'un packet (connection protocol) "
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
				System.out.println("[FireAPI] (BigBrother) Processus de vérification de connection ...");
				
				try {
					this.check();
					
					System.out.println("[FireAPI] (BigBrother) Le processus de vérification de connection a réussi !");
				} catch (BigBrotherNotConnectedException e) {
					System.err.println("[FireAPI] (BigBrother) Une erreur est survenue ! Changement pour ERROR_CHECK ...");
					
					BigBrotherConnectionCheck check = new BigBrotherConnectionCheck
							(this.api, BigBrotherConnectionCheckType.ERROR_CHECK, this.infos);
					
					check.startChecks();
					
					this.timer.cancel();
					this.timer.purge();
				}
				
				break;
			case ERROR_CHECK:
				System.err.println("[FireAPI] (BigBrother) Impossible de se connecter ! Tentative de reconnection ...");
				
				try {
					this.connectionCheck();
					this.check();
					
					System.out.println("[FireAPI] (BigBrother) Reconnection réussie ! Changement pour GLOBAL_CHECK ...");
					
					BigBrotherConnectionCheck check = new BigBrotherConnectionCheck
							(this.api, BigBrotherConnectionCheckType.GLOBAL_CHECK, this.infos);
					
					check.startChecks();
					
					this.timer.cancel();
					this.timer.purge();
				} catch (BigBrotherNotConnectedException e) {
					System.err.println("[FireAPI] (BigBrother) La tentative de reconnection à échouée ! "+e.getClass().getSimpleName()+
							", "+e.getMessage());
				}
				
				break;
		}
	}
	
	public void registerListener(BigBrotherConnectionCheckListener listener) {
		listeners.add(listener);
	}
	
	public void callListener(ConnectionHandler ch) {
		for(BigBrotherConnectionCheckListener ls : listeners) {
			ls.onSocketChange(ch);
		}
	}
}