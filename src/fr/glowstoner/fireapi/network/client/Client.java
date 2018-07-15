package fr.glowstoner.fireapi.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.network.ConnectionType;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;
import fr.glowstoner.fireapi.network.packets.PacketEncoder;

public class Client extends ConnectionHandler{
	
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
	public void open(EncryptionKey key) throws IOException {  
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
      
		ClientServerThread re = new ClientServerThread(key.getKey(), this, this.in);
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
		return ConnectionType.SERVER_CONNECTION;
	}
}