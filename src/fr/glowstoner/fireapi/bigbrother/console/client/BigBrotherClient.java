package fr.glowstoner.fireapi.bigbrother.console.client;

import java.io.Console;
import java.net.SocketException;
import java.util.Scanner;

import fr.glowstoner.fireapi.bigbrother.console.server.packets.PacketVersion;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.events.ClientListener;
import fr.glowstoner.fireapi.network.events.Listeners;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketPing;
import fr.glowstoner.fireapi.network.packets.PacketText;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;
import fr.glowstoner.fireapi.player.enums.VersionType;
import fr.glowstoner.fireapi.utils.PlatformType;
import fr.glowstoner.fireapi.utils.PlatformUtil;

public class BigBrotherClient implements ClientListener{
	
	private EncryptionKey key;
	private String password;
	private boolean outTime;
	
	public BigBrotherClient() {
		for(int i = 0 ; i <= 50 ; i++) System.out.println();
		
		System.out.println("BigBrother Client");
		 
		FireNetwork.init();
		Listeners l = FireNetwork.getListeners();
		
		l.registerClientListener(this);
	}
	
	public void askPassword() {
		if(PlatformUtil.getPlatform().equals(PlatformType.WINDOWS)) {
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
			Client c = new Client("62.4.16.89", 2568);
			
			c.open(this.key);
			
			c.sendPacket(new PacketLogin(this.password), this.key);
			
			Thread.sleep(150l);
			
			try {
				c.sendPacket(new PacketVersion(VersionType.CLIENT_BIGBROTHER), this.key);
				c.sendPacket(new PacketCommand("name RandomPélo"), this.key);
				
				Thread.sleep(120l);
				
				Scanner sc = new Scanner(System.in);
				
				while(true) {
					c.sendPacket(new PacketPing());
					
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
						
						c.sendPacket(new PacketCommand(line), this.key);
						
						this.outTime = true;
					}
				}
				
				sc.close();
			}catch (SocketException se) {
				System.out.println("\n[BigBrother] Vous avez été déconnecté ("+
						se.getMessage()+") ! Bye !");
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
		}
	}
}
