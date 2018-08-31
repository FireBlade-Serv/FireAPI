package fr.glowstoner.fireapi.network.packets;

public class PacketConnectionError extends Packet{

	private static final long serialVersionUID = 9185779750333678283L;

	private ConnectionErrorValues errorValue;
	private String text;
	
	public PacketConnectionError(String text, ConnectionErrorValues errorValue) {
		this.setText(text);
		this.setErrorValue(errorValue);
	}
	
	public PacketConnectionError() {
		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ConnectionErrorValues getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(ConnectionErrorValues errorValue) {
		this.errorValue = errorValue;
	}

	@Override
	public boolean encrypted() {
		return false;
	}

}
