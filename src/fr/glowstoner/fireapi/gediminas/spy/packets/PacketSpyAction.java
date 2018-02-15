package fr.glowstoner.fireapi.gediminas.spy.packets;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.glowstoner.connectionsapi.network.packets.Packet;
import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;

public class PacketSpyAction extends Packet implements Serializable{

	private static final long serialVersionUID = 2494871161464801144L;

	private SpyAction action;
	private String playerName, actionMsg, ip;
	private Calendar date;
	
	public PacketSpyAction(String name, String ip, String actionmsg, SpyAction action) {
		this.setAction(action);
		this.setPlayerName(name);
		this.setActionMsg(actionmsg);
		this.setIP(ip);
		
		this.date = GregorianCalendar.getInstance();
	}
	
	public PacketSpyAction() {
		
	}
	
	public void setDateToNow() {
		this.date.setTime(new Date());
	}
	
	public String getFormatedDate() {
		int ms = this.date.get(Calendar.MILLISECOND);
		int s = this.date.get(Calendar.SECOND);
		int m = this.date.get(Calendar.MINUTE);
		int h = this.date.get(Calendar.HOUR);
		int d = this.date.get(Calendar.DAY_OF_MONTH);
		int mo = this.date.get(Calendar.MONTH);
		int y = this.date.get(Calendar.YEAR);
		
		return ms+"-"+s+"-"+m+"-"+h+"/"+d+"-"+mo+"-"+y;
	}
	
	public String getFormatedMsg() {
		return this.getFormatedDate()+"*"+this.action.name()+"*"+this.getActionMsg();
	}
	
	@Override
	public boolean isCrypted() {
		return false;
	}

	public SpyAction getAction() {
		return action;
	}

	public void setAction(SpyAction action) {
		this.action = action;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public Calendar getActionDate() {
		return this.date;
	}

	public String getActionMsg() {
		return actionMsg;
	}

	public void setActionMsg(String actionMsg) {
		this.actionMsg = actionMsg;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}
}