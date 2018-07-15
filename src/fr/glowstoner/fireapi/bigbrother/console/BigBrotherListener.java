package fr.glowstoner.fireapi.bigbrother.console;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.glowstoner.fireapi.FireAPI;
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
import fr.glowstoner.fireapi.bungeecord.friends.packets.PacketFriends;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.command.packets.PacketCommand;
import fr.glowstoner.fireapi.network.events.Listeners;
import fr.glowstoner.fireapi.network.events.ServerListener;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketPing;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;
import fr.glowstoner.fireapi.network.packets.login.enums.LoginResult;
import fr.glowstoner.fireapi.player.enums.VersionType;

public class BigBrotherListener implements ServerListener{
	
	private static final String YELLOW_UNDERLINED = "\033[4;33m";
	private static final String RED_BOLD = "\033[1;31m";
	private static final String RESET = "\033[0m";
	
	private Listeners listeners;
	private Map<VersionType, List<ConnectionHandler>> connectionstype = new ConcurrentHashMap<>();
	private List<ConnectionHandler> connected = new CopyOnWriteArrayList<>();
	private BigBrotherLoginGetter log;
	private EncryptionKey key;
	private BigBrotherSpy gs;
	
	public BigBrotherListener(BigBrotherLoginGetter log, BigBrotherSpy gs, Listeners listeners) {
		this.listeners = listeners;
		this.gs = gs;
		this.log = log;
		this.key = this.log.getKey();
	}

	@Override
	public void onPacketReceive(Packet packet) {
		try {
			ConnectionHandler ch = packet.getConnection();
			
			String name = (ch.getName().equals("default-name")) ? ch.getIP() : ch.getName();
			
			if(!(packet instanceof PacketPing)) {
				if(!(packet instanceof PacketSpyAction)) {
					System.out.println("[BigBrother] Packet reçu : "+name+" -> "+packet.getClass().getSimpleName());
				}else {
					System.out.println("[BigBrother] Packet reçu : "+name+" -> "+
							packet.getClass().getSimpleName()+" / "+((PacketSpyAction) packet).getAction().name());
				}
			}
			
			if(ch.isLogged().equals(LoginResult.NOT_LOGGED)) {
				if(packet instanceof PacketLogin) {
					PacketLogin pl = (PacketLogin) packet;
					
					System.out.println("[BigBrother] Tentative de connection de "+name+
							" avec un mot de passe de "+pl.getPassword());
					
					if(pl.getPassword().equals(this.log.getPassword())) {
						this.sendMessage(ch, "[BigBrother] Connection réussie !");
						ch.setLoginResult(LoginResult.LOGGED);
						
						this.listeners.callOnConnectionSuccessfullServerListener(ch);
					}else {
						this.sendMessage(ch, "[BigBrother] Mot de passe incorect !");
						ch.close();
					}
				}else {
					ch.close();
					return;
				}
			}
			
			if(packet instanceof PacketCommand) {
				this.listeners.callOnCommandServerListener((PacketCommand) packet);
			}else if(packet instanceof PacketVersion) {
				this.addConnection(((PacketVersion) packet).getVersion(), ch);
			}else if(packet instanceof PacketFriends) {
				PacketFriends pf = (PacketFriends) packet;

				if(pf.getTo().equals(VersionType.SPIGOT_VERSION)) {
					this.getConnectionByNameOrIP(pf.getDestination()).sendPacket(pf, this.key);
				}
			}else if(packet instanceof PacketPlayerPing) {
				PacketPlayerPing pp = (PacketPlayerPing) packet;
				
				if(pp.getState().equals(PingState.INIT_SERVER)) {
					pp.setState(PingState.BUNGEE_REQUEST);
					
					try {
						this.getServerConnectionByNameUnsafe(VersionType.BUNGEECORD_VERSION, "main-bungeecord").
							sendPacket(pp, this.key);
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
			}else if(packet instanceof PacketBigBrotherAC) {
				PacketBigBrotherAC gacp = (PacketBigBrotherAC) packet;
				
				if(gacp.getType().equals(BigBrotherTypeAC.CHEAT_DETECTION)) {
					if(gacp.getTODO().equals(BigBrotherActionAC.INFORM_STAFF)) {
						this.getServerConnectionByNameUnsafe(VersionType.BUNGEECORD_VERSION,
								"main-bungeecord").sendPacket(gacp, this.key);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnection(ConnectionHandler ch) {
		this.showWelcome(ch);
		
		this.refreshLists();
	}

	@Override
	public void onConnectionSuccessfull(ConnectionHandler connection) {
		this.sendMessage(connection, "Bienvenue !");
		
		this.connected.add(connection);
		
		System.out.println("[BigBrother] "+connection.getIP()+" s'est connecté !");
	}

	@Override
	public void onCommand(ConnectionHandler c, String command, String[] args) {
		this.listeners.callCommand(c, command, args, "Commande inconnue ! "
				+ "Utilisez /aide pour avoir la liste des commandes !", this.key);
	}
	
	private void showWelcome(ConnectionHandler ch) {
		this.sendMessage(ch, RED_BOLD+"  ____  _         ____            _   _               ");
		this.sendMessage(ch, " |  _ \\(_)       |  _ \\          | | | |              ");
		this.sendMessage(ch, " | |_) |_  __ _  | |_) |_ __ ___ | |_| |__   ___ _ __ ");
		this.sendMessage(ch, " |  _ <| |/ _\\`| |  _ <| '__/ _ \\| __| '_ \\ / _ \\ '__|");
		this.sendMessage(ch, " | |_) | | (_| | | |_) | | | (_) | |_| | | |  __/ |   ");
		this.sendMessage(ch, " |____/|_|\\__, | |____/|_|  \\___/ \\__|_| |_|\\___|_|   ");
		this.sendMessage(ch, "           __/ |                                      ");
		this.sendMessage(ch, "          |___/                                       ");
		this.sendMessage(ch, RESET+"\n         Version de l'api : "+YELLOW_UNDERLINED+FireAPI.VERSION+"\n\n");
		this.sendMessage(ch, RESET+"Bonjour !");
	}
	
	private void sendMessage(ConnectionHandler ch, String message) {
		ch.sendMessageWithPrefix(message, this.log.getKey());
	}
	
	private void addConnection(VersionType type, ConnectionHandler ch) {
		List<ConnectionHandler> list = (this.connectionstype.containsKey(type)) ?
				this.connectionstype.get(type) : new CopyOnWriteArrayList<>();
				
		list.add(ch);
		
		if(this.connectionstype.containsKey(type)) {
			this.connectionstype.replace(type, list);
		}else {
			this.connectionstype.put(type, list);
		}
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
		this.refreshLists();
		return this.connected;
	}
	
	public Map<VersionType, List<ConnectionHandler>> getServersConnections() {
		this.refreshLists();
		return this.connectionstype;
	}
}