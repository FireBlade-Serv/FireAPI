package fr.glowstoner.fireapi.bigbrother.console;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import fr.glowstoner.fireapi.bigbrother.ac.packet.PacketBigBrotherAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherActionAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherTypeAC;
import fr.glowstoner.fireapi.bigbrother.console.login.BigBrotherLoginGetter;
import fr.glowstoner.fireapi.bigbrother.console.packets.PacketVersion;
import fr.glowstoner.fireapi.bigbrother.console.packets.ping.PacketPlayerPing;
import fr.glowstoner.fireapi.bigbrother.console.packets.ping.enums.PingState;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpy;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyHistory;
import fr.glowstoner.fireapi.bigbrother.spy.BigBrotherSpyUtils;
import fr.glowstoner.fireapi.bigbrother.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.bigbrother.spy.packets.PacketSpyHistoryGetter;
import fr.glowstoner.fireapi.bigbrother.spy.packets.enums.BigBrotherSpyHistoryGetterState;
import fr.glowstoner.fireapi.bungeecord.friends.packets.PacketFriends;
import fr.glowstoner.fireapi.bungeecord.friends.packets.action.FriendsActionTransmetterGUI;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class BigBrotherListener implements ServerListener{
	
	private Listeners listeners;
	private Map<ConnectionHandler, Integer> limit = new HashMap<>();
	private Map<VersionType, List<ConnectionHandler>> connectionstype = new ConcurrentHashMap<>();
	private List<ConnectionHandler> connected = new CopyOnWriteArrayList<>();
	private BigBrotherLoginGetter log;
	private BigBrotherSpy gs;
	
	public BigBrotherListener(BigBrotherSpy gs, Listeners listeners) {
		this.listeners = listeners;
		this.gs = gs;
		
		this.log = new BigBrotherLoginGetter();
		
		try {
			System.out.println("[BigBrother] Chargement logins getter ...");
			
			this.log.load();
			
			System.out.println("[BigBrother] La clé de sécurité utilisée est "+this.log.getKey());
			System.out.println("[BigBrother] Le mot de passe utilisé est "+this.log.getPassword());
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
					System.out.println("[BigBrother] Packet reçu : "+name+" -> "+packet.getClass().getSimpleName());
				}else {
					System.out.println("[BigBrother] Packet reçu : "+name+" -> "+
							packet.getClass().getSimpleName()+" / "+((PacketSpyAction) packet).getAction().name());
				}
			}
			
			if(server.isLogged().equals(LoginResult.NOT_LOGGED)) {
				if(packet instanceof PacketLogin) {
					PacketLogin pl = (PacketLogin) packet;
					
					try {
						String cpass = pl.decryptPass(this.log.getKey());
						
						System.out.println("[BigBrother] PacketLogin reçu, valeur du PASS (crypt) : "+pl.getCryptPassword());
						System.out.println("[BigBrother] PacketLogin reçu, valeur du PASS (D_key) : "+cpass);
						
						if(cpass.equals(this.log.getPassword())) {
							server.sendPacket(new PacketText("[BigBrother] Connection réussie !"));
							server.setLoginResult(LoginResult.LOGGED);
							
							listeners.callOnConnectionSuccessfullServerListener(server);
						}else {
							server.sendMessage("[BigBrother] Mot de passe incorect !");
							
							if(limit.containsKey(server)) {
								limit.replace(server, limit.get(server) + 1);
								
								if(limit.get(server) == 3) {
									server.sendPacket(new PacketText(
											"[BigBrother] Trop de tentatives ratées ! Déconnexion ..."));
									
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
					server.sendPacket(new PacketText("[BigBrother] Vous devez vous connecter !"));
					
					return;
				}
			}
			
			if(packet instanceof PacketCommand) {
				this.listeners.callOnCommandServerListener((PacketCommand) packet); 
			}else if(packet instanceof PacketVersion) {
				PacketVersion ver = (PacketVersion) packet;
				
				if(!this.connectionstype.containsKey(VersionType.SPIGOT_VERSION)) {
					if(ver.getType().equals(VersionType.SPIGOT_VERSION)) {
						List<ConnectionHandler> list = new CopyOnWriteArrayList<>();
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
						List<ConnectionHandler> list = new CopyOnWriteArrayList<>();
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
						this.getServerConnectionByNameUnsafe(VersionType.BUNGEECORD_VERSION, "main-bungeecord").sendPacket(pp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else if(packet instanceof PacketSpyAction) {
				PacketSpyAction spy = (PacketSpyAction) packet;
				
				Calendar cal = GregorianCalendar.getInstance();
				cal.setTime(new Date());
				
				if(this.gs.ifDataFileExists(spy.getPlayerName())) {
					this.gs.updateDataFile(spy.getPlayerName(),
							BigBrotherSpyUtils.mergeHistory(this.gs.getHistory(spy.getPlayerName()), spy));
				}else {
					BigBrotherSpyHistory gh = new BigBrotherSpyHistory(spy.getPlayerName(), spy.getIP(),
							BigBrotherSpyUtils.toFireCalendar(cal));
					
					gh.putMessage(BigBrotherSpyUtils.toFireCalendar(spy.getActionDate()), spy.getAction(),
							spy.getFormatedMsg(), spy.getRawMsg());
					
					this.gs.createNewDataFile(spy.getPlayerName(), gh);
				}
			}else if(packet instanceof PacketSpyHistoryGetter) {
				PacketSpyHistoryGetter get = (PacketSpyHistoryGetter) packet;
				
				if(get.getState().equals(BigBrotherSpyHistoryGetterState.REQUEST)) {
					get.setState(BigBrotherSpyHistoryGetterState.SEND);
					
					get.setHistory(this.gs.getHistory(get.getPlayerName()));
					
					server.sendPacket(get);
				}
			}else if(packet instanceof PacketBigBrotherAC) {
				PacketBigBrotherAC gacp = (PacketBigBrotherAC) packet;
				
				if(gacp.getType().equals(BigBrotherTypeAC.CHEAT_DETECTION)) {
					if(gacp.getTODO().equals(BigBrotherActionAC.INFORM_STAFF)) {
						this.getServerConnectionByNameUnsafe(VersionType.BUNGEECORD_VERSION, "main-bungeecord").sendPacket(gacp);
					}
				}
			}
		}catch (Exception e) {
			return;
		}
	}

	@Override
	public void onConnection(ConnectionHandler ch) {
		ch.sendMessage("~~~~~~~~~~BigBrother~~~~~~~~~~");
		ch.sendMessage("by Glowstoner");
		ch.sendMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ch.sendMessage("\nProtocol version : 3.00");
		
		this.refreshLists();
	}

	@Override
	public void onConnectionSuccessfull(ConnectionHandler connection) {
		connection.sendMessageWithPrefix("Bienvenue !");
		
		this.connected.add(connection);
		
		System.out.println("[BigBrother] "+connection.getIP()+" s'est connecté !");
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
						System.out.println("[BigBrother] Le serveur "+in.getName()+" à été retiré ! (BungeeCord)");
						
						this.connectionstype.replace(VersionType.BUNGEECORD_VERSION, l);
					}
					
					if(this.connectionstype.get(VersionType.SPIGOT_VERSION).contains(in)) {
						List<ConnectionHandler> l = this.connectionstype.
								get(VersionType.SPIGOT_VERSION);
						
						l.remove(in);
						System.out.println("[BigBrother] "+in.getName()+" à été retiré ! (Spigot)");
						
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
	
	public ConnectionHandler getServerConnectionByNameUnsafe(VersionType type, String connectionName) {
		for(ConnectionHandler chs : this.connectionstype.get(type)) {
			if(chs.getName().equals(connectionName)) {
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