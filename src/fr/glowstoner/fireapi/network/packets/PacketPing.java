package fr.glowstoner.fireapi.network.packets;

public class PacketPing extends Packet {

	private static final long serialVersionUID = -4826374884627726143L;
	
	//test packet for check connection
	public PacketPing() {
		
	}

	@Override
	public boolean isCrypted() {
		return false;
	}
}
