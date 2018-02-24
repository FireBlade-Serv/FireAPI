package fr.glowstoner.fireapi.gediminas.console;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.connectionsapi.network.events.Listeners;
import fr.glowstoner.connectionsapi.network.events.ServerListener;
import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.connectionsapi.network.packets.PacketPing;
import fr.glowstoner.connectionsapi.network.packets.PacketText;
import fr.glowstoner.connectionsapi.network.packets.command.PacketCommand;
import fr.glowstoner.connectionsapi.network.packets.login.PacketLogin;
import fr.glowstoner.connectionsapi.network.packets.login.enums.LoginResult;
import fr.glowstoner.fireapi.bungeecord.friends.packets.PacketFriends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.action.FriendsActionTransmetterGUI;
import fr.glowstoner.fireapi.gediminas.console.login.GediminasLoginGetter;
import fr.glowstoner.fireapi.gediminas.console.packets.PacketVersion;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.PacketPlayerPing;
import fr.glowstoner.fireapi.gediminas.console.packets.ping.enums.PingState;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpy;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyHistory;
import fr.glowstoner.fireapi.gediminas.spy.GediminasSpyUtils;
import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyHistoryGetter;
import fr.glowstoner.fireapi.gediminas.spy.packets.enums.GediminasSpyHistoryGetterState;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class GediminasListener implements ServerListener{
	
	private Listeners listeners;
	private Map<ConnectionHandler, Integer> limit = new HashMap<>();
	private Map<VersionType, List<ConnectionHandler>> connectionstype = new ConcurrentHashMap<>();
	private List<ConnectionHandler> connected = new CopyOnWriteArrayList<>();
	private GediminasLoginGetter log;
	private GediminasSpy gs;
	
	public GediminasListener(GediminasSpy gs, Listeners listeners) {
		this.listeners = listeners;
		this.gs = gs;
		
		this.log = new GediminasLoginGetter();
		
		try {
			System.out.println("[Gediminas] Chargement logins getter ...");
			
			this.log.load();
			
			System.out.println("[Gediminas] La clé de sécurité utilisée est "+this.log.getKey());
			System.out.println("[Gediminas] Le mot de passe utilisé est "+this.log.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPacketReceive(Packet packet) {
		try {
			ConnectionHandler server = packet.getConnection();
			
			String name = (server.getName().equals("default-name")) ? server.getIP() : server.getName();
			
			if(!(packet instanceof PacketPing)) {
				if(!(packet instanceof PacketSpyAction)) {
					System.out.println("[Gediminas] Packet reçu : "+name+" -> "+packet.getClass().getSimpleName());
				}else {
					System.out.println("[Gediminas] Packet reçu : "+name+" -> "+
							packet.getClass().getSimpleName()+" / "+((PacketSpyAction) packet).getAction().name());
				}
			}
			
			if(server.isLogged().equals(LoginResult.NOT_LOGGED)) {
				if(packet instanceof PacketLogin) {
					PacketLogin pl = (PacketLogin) packet;
					
					try {
						String cpass = pl.decryptPass(this.log.getKey());
						
						System.out.println("[Gediminas] PacketLogin reçu, valeur du PASS (crypt) : "+pl.getCryptPassword());
						System.out.println("[Gediminas] PacketLogin reçu, valeur du PASS (D_key) : "+cpass);
						
						if(cpass.equals(this.log.getPassword())) {
							server.sendPacket(new PacketText("[Gediminas] Connection réussie !"));
							server.setLoginResult(LoginResult.LOGGED);
							
							listeners.callOnConnectionSuccessfullServerListener(server);
						}else {
							server.sendMessage("[Gediminas] Mot de passe incorect !");
							
							if(limit.containsKey(server)) {
								limit.replace(server, limit.get(server) + 1);
								
								if(limit.get(server) == 3) {
									server.sendPacket(new PacketText(
											"[Gediminas] Trop de tentatives ratées ! Déconnexion ..."));
									
									server.close();
								}
							}else {
								limit.put(server, 1);
							}
						}
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | BadPaddingException | IOException e) {
						
						return;
					}
				}else {
					server.sendPacket(new PacketText("[Gediminas] Vous devez vous connecter !"));
					
					return;
				}
			}
			
			if(packet instanceof PacketCommand) {
				this.listeners.callOnCommandServerListener((PacketCommand) packet); 
			}else if(packet instanceof PacketVersion) {
				PacketVersion ver = (PacketVersion) packet;
				
				if(!this.connectionstype.containsKey(VersionType.SPIGOT_VERSION)) {
					if(ver.getType().equals(VersionType.SPIGOT_VERSION)) {
						List<ConnectionHandler> list = new ArrayList<>();
						list.add(server);
						
						this.connectionstype.put(VersionType.SPIGOT_VERSION, list);
					}
				}else {
					if(ver.getType().equals(VersionType.SPIGOT_VERSION)) {
						List<ConnectionHandler> list = this.connectionstype.get(VersionType.SPIGOT_VERSION);
						list.add(server);
						
						this.connectionstype.replace(VersionType.SPIGOT_VERSION, list);
					}
				}
				
				if(!this.connectionstype.containsKey(VersionType.BUNGEECORD_VERSION)) {
					if(ver.getType().equals(VersionType.BUNGEECORD_VERSION)) {
						List<ConnectionHandler> list = new ArrayList<>();
						list.add(server);
						
						this.connectionstype.put(VersionType.BUNGEECORD_VERSION, list);
					}
				}else {
					if(ver.getType().equals(VersionType.BUNGEECORD_VERSION)) {
						List<ConnectionHandler> list = this.connectionstype.get(VersionType.BUNGEECORD_VERSION);
						list.add(server);
						
						this.connectionstype.replace(VersionType.BUNGEECORD_VERSION, list);
					}
				}
			}else if(packet instanceof PacketFriends) {
				PacketFriends pf = (PacketFriends) packet;
				
				if(pf.getAction() instanceof FriendsActionTransmetterGUI) {
					FriendsActionTransmetterGUI fa = (FriendsActionTransmetterGUI) pf.getAction();
					
					if(fa.to().equals(VersionType.SPIGOT_VERSION)) {
						for(ConnectionHandler chs : this.connected) {
							if(chs.getName().equals(fa.getServerDestination())) {
								chs.sendPacket(packet);
							}
						}
					}
				}
			}else if(packet instanceof PacketPlayerPing) {
				PacketPlayerPing pp = (PacketPlayerPing) packet;
				
				if(pp.getState().equals(PingState.INIT_SERVER)) {
					pp.setState(PingState.BUNGEE_REQUEST);
					
					try {
						this.getConnectionByNameOrIP("main-bungeecord").sendPacket(pp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else if(packet instanceof PacketSpyAction) {
				PacketSpyAction spy = (PacketSpyAction) packet;
				
				if(this.gs.ifDataFileExists(spy.getPlayerName())) {
					this.gs.updateDataFile(spy.getPlayerName(),
							GediminasSpyUtils.mergeHistory(this.gs.getHistory(spy.getPlayerName()), spy));
				}else {
					GediminasSpyHistory gh = new GediminasSpyHistory(spy.getPlayerName(), spy.getIP());
					
					gh.putMessage(spy.getActionDate(), spy.getAction(), spy.getFormatedMsg(), spy.getRawMsg());
					
					this.gs.createNewDataFile(spy.getPlayerName(), gh);
				}
			}else if(packet instanceof PacketSpyHistoryGetter) {
				PacketSpyHistoryGetter get = (PacketSpyHistoryGetter) packet;
				
				if(get.getState().equals(GediminasSpyHistoryGetterState.REQUEST)) {
					get.setState(GediminasSpyHistoryGetterState.SEND);
					
					get.setHistory(this.gs.getHistory(get.getPlayerName()));
					
					server.sendPacket(get);
				}
			}
		}catch (Exception e) {
			return;
		}
	}

	@Override
	public void onConnection(ConnectionHandler ch) {
		ch.sendMessage("~~~~~~~~~~Gediminas~~~~~~~~~~");
		ch.sendMessage("by Glowstoner");
		ch.sendMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ch.sendMessage("\nProtocol version : 1.0");
		
		this.refreshLists();
	}

	@Override
	public void onConnectionSuccessfull(ConnectionHandler connection) {
		connection.sendMessageWithPrefix("Bienvenue !");
		
		this.connected.add(connection);
		
		System.out.println("[Gediminas] "+connection.getIP()+" s'est connecté !");
	}

	@Override
	public void onCommand(ConnectionHandler c, String command, String[] args) {
		this.listeners.callCommand(c, command, args, "Commande inconnue ! "
				+ "Utilisez /aide pour avoir la liste des commandes !");
	}
	
	private void refreshLists() {
		List<ConnectionHandler> newl = this.connected;
		
		for(ConnectionHandler c : this.connected) {
			if(!c.isConnectedOrValid()) {
				newl.remove(c);
			}
		}
		
		this.connected = newl;
		
		for(List<ConnectionHandler> conlist : this.connectionstype.values()) {
			for(ConnectionHandler in : conlist) {
				if(!in.isConnectedOrValid()) {
					if(this.connectionstype.get(VersionType.BUNGEECORD_VERSION).contains(in)) {
						List<ConnectionHandler> l = this.connectionstype.
								get(VersionType.BUNGEECORD_VERSION);
						
						l.remove(in);
						System.out.println("[Gediminas] Le serveur "+in.getName()+" à été retiré ! (BungeeCord)");
						
						this.connectionstype.replace(VersionType.BUNGEECORD_VERSION, l);
					}
					
					if(this.connectionstype.get(VersionType.SPIGOT_VERSION).contains(in)) {
						List<ConnectionHandler> l = this.connectionstype.
								get(VersionType.SPIGOT_VERSION);
						
						l.remove(in);
						System.out.println("[Gediminas] "+in.getName()+" à été retiré ! (Spigot)");
						
						this.connectionstype.replace(VersionType.SPIGOT_VERSION, l);
					}
				}
			}
		}
	}
	
	public ConnectionHandler getConnectionByNameOrIP(String name) {
		this.refreshLists();
		
		//String name = (ch.getName().equals("default-name")) ? ch.getIP() : ch.getName();
		
		for(ConnectionHandler chs : this.getConnected()) {
			String n = (chs.getName().equals("default-name")) ? chs.getIP() : chs.getName();
			
			if(n.equals(name)) {
				return chs;
			}
		}
		
		return null;
	}
	
	public List<ConnectionHandler> getConnected() {
		refreshLists();
		
		return this.connected;
	}
	
	public Map<VersionType, List<ConnectionHandler>> getServersConnections() {
		refreshLists();
		
		return this.connectionstype;
	}
}