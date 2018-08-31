package fr.glowstoner.fireapi.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fr.glowstoner.fireapi.network.BaseConnector;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.FireNetwork;
import fr.glowstoner.fireapi.network.exceptions.UnsecureConnectionException;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.EncryptedPacket;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;
import fr.glowstoner.fireapi.network.packets.login.PacketLogin;

public class Client extends ConnectionHandler implements BaseConnector{
	
	private static final long serialVersionUID = -6666814765157664167L;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client(String ip, int port) throws IOException{
		this.socket = new Socket(ip, port);
		super.setSocket(this.socket);
	}

	@Override
	public void sendPacket(Packet packet) throws IOException {
		if(packet.encrypted()) {
			EncryptedPacket ep = new PacketEncoder(FireNetwork.getInstance().getKey())
					.encode((Encryptable) packet);
			
			this.out.writeObject(ep);
		}else {
			if(packet instanceof PacketLogin) {
				throw new UnsecureConnectionException(packet.getClass().getName());
			}
			
			this.out.writeObject(packet);
		}
		
		this.out.flush();
	}

	@Override
	public void open() throws IOException {  
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
      
		ClientServerThread re = new ClientServerThread(this, this.in);
		re.start();
	}
      
	@Override
	public void close() throws IOException {
		if(this.socket != null)  this.socket.close();
		if(this.in != null) this.in.close();
		if(this.out != null) this.out.close();
	}

	@Override
	public ConnectionType type() {
		return ConnectionType.CLIENT_CONNECTION;
	}
}