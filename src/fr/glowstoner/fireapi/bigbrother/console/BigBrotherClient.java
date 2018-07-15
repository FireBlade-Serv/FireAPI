package fr.glowstoner.fireapi.bigbrother.console;

import java.util.Scanner;

import fr.glowstoner.fireapi.bigbrother.console.packets.PacketVersion;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.client.Client;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.events.ClientListener;
import fr.glowstoner.fireapi.network.events.Listeners;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketText;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class BigBrotherClient implements ClientListener{
	
	private EncryptionKey key;
	private String password;
	
	public BigBrotherClient() {
		System.out.println("BigBrother Client");
		 
		FireNetwork.init();
		Listeners l = FireNetwork.getListeners();
		
		l.registerClientListener(this);
	}
	
	public void connect() {
		try {
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
			
			Client c = new Client("62.4.16.89", 2566);
			
			c.open(this.key);
			
			c.sendPacket(new PacketLogin(this.password), this.key);
			c.sendPacket(new PacketVersion(VersionType.CLIENT_BIGBROTHER), this.key);
			
			while(((ConnectionHandler) c).isConnectedOrValid()) {
				String line = sc.nextLine();
				
				if(line.equalsIgnoreCase("stop")) {
					c.close();
					break;
				}
				
				c.sendPacket(new PacketCommand(line), this.key);
			}
			
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketText) {
			PacketText tp = (PacketText) p;
			
			System.out.println(tp.getText());
		}
	}
}
