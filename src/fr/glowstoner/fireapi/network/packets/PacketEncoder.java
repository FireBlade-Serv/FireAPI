package fr.glowstoner.fireapi.network.packets;

import java.lang.reflect.Field;

import fr.glowstoner.fireapi.crypto.EncryptFunction;

public class PacketEncoder {
	
	private String key;
	
	public PacketEncoder(String key) {
		this.key = key;
	}
	
	public EncryptedPacket encode(Encryptable packet) {
		EncryptedPacket ep = new EncryptedPacket(packet.getClass());
		
		for(String fs : packet.encryptedFields()) {
			try {
				Field f = packet.getClass().getDeclaredField(fs);
				f.setAccessible(true);
				
				Object av = f.get(packet.getClass().cast(packet));
				
				ep.addValue(f.getName(), EncryptFunction.encrypt(this.key, av));
			} catch (NoSuchFieldException | SecurityException |
					IllegalArgumentException | IllegalAccessException e) {
				
				e.printStackTrace();
			}
		}
		
		return ep;
	}
	
	public Packet decode(EncryptedPacket ep) {
		try {
			Packet o = (Packet) ep.getPacketClass().newInstance();
			
			for(String fs : ep.getData().keySet()) {
				Field f = o.getClass().getDeclaredField(fs);
				
				f.setAccessible(true);
				
				f.set(o, EncryptFunction.decrypt(key, ep.getElement(fs)));
			}
			
			return o;
		} catch (InstantiationException | IllegalAccessException |
				NoSuchFieldException | SecurityException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
}
