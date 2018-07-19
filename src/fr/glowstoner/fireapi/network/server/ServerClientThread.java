package fr.glowstoner.fireapi.network.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.SocketException;

import fr.glowstoner.fireapi.bigbrother.console.server.login.BigBrotherLoginGetter;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.EncryptedPacket;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;
import fr.glowstoner.fireapi.network.packets.PacketText;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;

public class ServerClientThread extends ConnectionHandler implements Runnable{
	
	private static final long serialVersionUID = -3378783954703324349L;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ServerClientThread(Socket socket) {
		super(socket);
	}
	
	@Override
	public void open(EncryptionKey key) throws IOException {
		this.in = new ObjectInputStream(super.socket.getInputStream());
		this.out = new ObjectOutputStream(super.socket.getOutputStream());
		
		FireNetwork.getListeners().callOnConnectionServerListener(this);
	}
	
	@Override
	public void close() throws IOException {			
		if(super.socket != null) super.socket.close();
		if(this.in != null) this.in.close();
		if(this.out != null) this.out.close();
	}
	
	public Socket getSocket() {
		return super.socket;
	}
	
	@Override
	public void run() {
		Object o = null;
		
		BigBrotherLoginGetter bg = new BigBrotherLoginGetter();
		
		try {
			bg.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		while(true) {
			try {
				try {
					o = this.in.readObject();
				}catch (EOFException ex) {
					try {
						o = this.in.readObject();
					}catch (IOException ex2) {
						if(ex2 instanceof OptionalDataException) {
							OptionalDataException ode = (OptionalDataException) ex2;
							
							ode.printStackTrace();
							
							System.out.println("eof = "+ode.eof);
							System.out.println("available = "+this.in.available());
							
							this.in.skipBytes(ode.length);
							break;
						}else {
							break;
						}
					}
					
				}catch(SocketException ex) {
					if(ex.getMessage().equals("Connection reset") || ex.getMessage().equals("Socket closed")) {
						return;
					}
				}
				
				if(o != null && o instanceof Packet) {
					Packet p = (Packet) o;
					
					if(p instanceof EncryptedPacket) {
						EncryptedPacket ep = (EncryptedPacket) p;
						
						try {
							p = new PacketEncoder(bg.getKey()).decode(ep);
						}catch (Exception e) {
							System.out.println("[BigBrother] La clé de chiffrement est fausse !"
									+ " (error_decode/[list])");
							
							this.sendPacket(new PacketText("[BigBrother] Votre clé de chiffrement est fausse :("));
							break;
						}
						
						if(p instanceof PacketLogin) {
							if(((PacketLogin) p).getPassword().isEmpty()) {
								System.out.println("[BigBrother] La clé de chiffrement est fausse !"
										+ " (empty login/string)");
								
								this.sendPacket(new PacketText("[BigBrother] Votre clé de chiffrement est fausse :("));
								break;
							}
						}
					}
					
					FireNetwork.getListeners().callPacketReceiveServerListener(p, this);
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				break;
			}
		}
		
		try {
			this.close();
		} catch (IOException e) {
			return;
		}
	}

	@Override
	public void sendPacket(Packet packet) throws IOException {
		this.out.writeObject(packet);
		this.out.flush();
	}
	
	@Override
	public void sendPacket(Encryptable packet, EncryptionKey key) throws IOException {
		this.out.writeObject(new PacketEncoder(key).
				encode((Encryptable) packet));
		this.out.flush();
	}

	@Override
	public ConnectionType type() {
		return ConnectionType.CLIENT_CONNECTION;
	}
}