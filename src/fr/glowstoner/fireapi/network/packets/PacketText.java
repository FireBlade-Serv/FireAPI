package fr.glowstoner.fireapi.network.packets;

public class PacketText extends Packet implements Encryptable{

	private static final long serialVersionUID = 5258811709224710605L;

	private String text;
	
	public PacketText(String text) {
		this.setText(text);
	}
	
	public PacketText() {
		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"text"};
	}

	@Override
	public boolean isCrypted() {
		return true;
	}
	
	@Override
	public String toString() {
		return "text: "+this.text;
	}
}
