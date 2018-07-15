package fr.glowstoner.fireapi.network.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.EncryptedPacket;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;

public class ServerClientThread extends ConnectionHandler implements Runnable{
	
	private static final long serialVersionUID = -3378783954703324349L;
	
	private transient EncryptionKey key;
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ServerClientThread(EncryptionKey key, Socket socket) {
		super(socket);
		
		this.socket = socket;
		this.key = key;
	}
	
	@Override
	public void open(EncryptionKey key) throws IOException {
		this.in = new ObjectInputStream(socket.getInputStream());
		this.out = new ObjectOutputStream(socket.getOutputStream());
		
		FireNetwork.getListeners().callOnConnectionServerListener(this);
	}
	
	@Override
	public void close() throws IOException {			
		if(socket != null) socket.close();
		if(in != null) in.close();
		if(out != null) out.close();
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	@Override
	public void run() {
		Object o = null;
		
		while(true) {
			try {
				try {
					o = this.in.readObject();
				}catch (EOFException ex) {
					o = this.in.readObject();
				}catch (IOException ex) {
					ex.printStackTrace();
					
					if(ex instanceof OptionalDataException) {
						OptionalDataException ode = (OptionalDataException) ex;
						
						System.out.println("eof = "+ode.eof);
						System.out.println("available = "+this.in.available());
						
						this.in.skipBytes(ode.length);
						break;
					}else {
						break;
					}
				}
				
				if(o != null && o instanceof Packet) {
					Packet p = (Packet) o;
					
					if(p instanceof EncryptedPacket) {
						EncryptedPacket ep = (EncryptedPacket) p;
						   
						p = new PacketEncoder(this.key.getKey()).decode(ep);
					}
					
					FireNetwork.getListeners().callPacketReceiveServerListener(p, this);
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				break;
			}
		}
		
		try {
			close();
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
		this.out.writeObject(new PacketEncoder(key.getKey()).
				encode((Encryptable) packet));
		this.out.flush();
	}

	@Override
	public ConnectionType type() {
		return ConnectionType.CLIENT_CONNECTION;
	}
}