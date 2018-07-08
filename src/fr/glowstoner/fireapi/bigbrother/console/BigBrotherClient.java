package fr.glowstoner.fireapi.bigbrother.console;

import java.io.DataInputStream;

import fr.glowstoner.connectionsapi.ConnectionsAPI;
import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.client.Client;
import fr.glowstoner.connectionsapi.network.events.ClientListener;
import fr.glowstoner.connectionsapi.network.events.Listeners;
import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.PacketText;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;
import fr.glowstoner.connectionsapi.network.packets.login.PacketLogin;

public class BigBrotherClient {
	
	private DataInputStream in;
	private boolean logged;

	public BigBrotherClient() {
		System.out.println("BigBrother Client");
		 
		this.in = new DataInputStream(System.in);
		
		ConnectionsAPI.init();
		Listeners l = ConnectionsAPI.getListeners();
		
		l.registerClientListener(new ClientListener() {
			
			@Override
			public void onPacketReceive(Packet p) {
				if(p instanceof PacketText) {
					PacketText tp = (PacketText) p;
					
					System.out.println(tp.getMessage());
					
					if(tp.getMessage().equalsIgnoreCase("Bienvenue !")) {
						logged = true;
					}
				}
			}
		});
	}
	
	public void connect() {
		try {
			Client c = new Client("62.4.16.89", 2566);
			c.start();
			
			ConnectionHandler ch = c;
			
			while(true) {
				@SuppressWarnings("deprecation")
				String line = this.in.readLine();
				
				if(line.equalsIgnoreCase("stop")) {
					break;
				}
				
				if(!this.logged) {
					//login *key-pass
					if(line.startsWith("login")) {
						String sub = line.substring(6);
						
						String[] args = sub.split("-");
						
						String pass = null, key = null;
						
						for(String arg : args) {
							if(arg.startsWith("*")) {
								key = arg.substring(1);
							}else {
								pass = arg;
							}
						}
						
						ch.sendPacket(new PacketLogin(key, pass));
						
						System.out.println("Envoi d'un login avec KEY="+key+", PASS="+pass);
					}else {
						ch.sendPacket(new PacketCommand(line));
					}
				}
			}
			
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
