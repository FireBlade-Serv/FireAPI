package fr.glowstoner.fireapi.bigbrother.console.client;

import java.io.Console;
import java.net.SocketException;
import java.util.Scanner;

import fr.glowstoner.fireapi.bigbrother.console.server.packets.PacketVersion;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.events.ClientListener;
import fr.glowstoner.fireapi.network.packets.ConnectionErrorValues;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketConnectionError;
import fr.glowstoner.fireapi.network.packets.PacketText;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.utils.PlatformType;
import fr.glowstoner.fireapi.utils.PlatformUtil;

public class BigBrotherClient implements ClientListener{
	
	private boolean outTime, connected;
	
	private EncryptionKey key;
	private String password;
	
	public BigBrotherClient() {
		for(int i = 0 ; i <= 50 ; i++) System.out.println();
		
		System.out.println("BigBrother Client");
	}	
	
	public void askPassword() {
		PlatformType pt = PlatformUtil.getPlatform();
		
		System.out.println("Type de plateforme détéctée: " + pt.name());
		
		if(pt.equals(PlatformType.WINDOWS)) {
			System.out.println("Mode compatibilité activé ! \n"
					+ "Il serait temps de passer sous linux !");
			
			this.askPasswordWindows();
		}else {
			this.askPasswordUnix();
		}
	}
	
	private void askPasswordUnix() {
		Console console = System.console();
		
		do {
			String k = new String(console.readPassword("\nClé de chiffrement : "));
			
			if(k.length() > 0) {
				this.key = new EncryptionKey(k);
			}
		} while(this.key == null);
		
		do {
			String mdp = new String(console.readPassword("\nMot de passe : "));
			
			if(mdp.length() > 0) {
				this.password = mdp;
			}
		} while(this.password == null);
	}
	
	private void askPasswordWindows() {
		Scanner sc = new Scanner(System.in);
		
		do {
			System.out.print("\nClé de chiffrement : ");
			String k = sc.nextLine();
			
			if(k.length() > 0) {
				this.key = new EncryptionKey(k);
			}
		} while(this.key == null);
		
		do {
			System.out.print("\nMot de passe : ");
			String mdp = sc.nextLine();
			
			if(mdp.length() > 0) {
				this.password = mdp;
			}
		} while(this.password == null);
		
		sc.close();
	}
	
	public void connect() {
		try {
			FireNetwork fn = new FireNetwork(this.key);
			
			fn.getListeners().registerClientListener(this);
			
			fn.start(ConnectionType.CLIENT_CONNECTION, this.password, true);
			
			Client c = (Client) fn.getBaseConnector();
			
			this.connected = true;
			
			try {
				c.sendPacket(new PacketVersion(VersionType.CLIENT_BIGBROTHER));
				c.sendPacket(new PacketCommand("name RandomPélo"));
				
				Thread.sleep(120l);
				
				Scanner sc = new Scanner(System.in);
				
				while(this.connected) {
					//sinon fait des erreurs cheloues
					System.out.print("");
					
					if(!this.outTime) {
						Thread.sleep(120l);
						
						System.out.print("\n > ");
						String line = sc.nextLine();
						
						if(line.equalsIgnoreCase("quit")) {
							c.close();
							break;
						}else if(line.isEmpty()) {
							continue;
						}
						
						c.sendPacket(new PacketCommand(line));
						
						this.outTime = true;
					}
				}
				
				sc.close();
			}catch (SocketException se) {
				System.out.println("\n[BigBrother] Vous avez été déconnecté ("+
						se.getMessage()+") ! Bye !");
				this.connected = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketText) {
			PacketText pt = (PacketText) p;
			
			System.out.print("\n"+pt.getText());
			
			this.outTime = false;
		}else if(p instanceof PacketConnectionError) {
			PacketConnectionError pce = (PacketConnectionError) p;
			
			System.out.println("\n"+pce.getText());
			
			if(pce.getErrorValue().equals(ConnectionErrorValues.WELCOME)) {
				return;
			}
			
			this.connected = false;
		}
	}
}
