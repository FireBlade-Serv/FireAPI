package fr.glowstoner.fireapi.gediminas.spy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;
import fr.glowstoner.fireapi.utils.calendar.FireCalendar;

public class GediminasSpyHistory implements Serializable{
	
	private static final long serialVersionUID = -5659480433388585225L;
	
	private String playerName, ip;
	private Map<Integer, GediminasSpyHistoryData> data;
	
	public GediminasSpyHistory(String name, String ip, FireCalendar now) {
		this.data = new HashMap<>();
		
		this.setIP(ip);
		this.setPlayerName(name);
		
		this.putMessage(now, SpyAction.INIT, "Joueur initialisé. Date = "+GediminasSpyUtils.getFormatedDate(now), "Joueur initialisé.");
	}
	
	public void putMessage(FireCalendar date, SpyAction action, String actionMsg, String baseMessage) {
		if(this.data.size() == 0) {
			this.data.put(0, new GediminasSpyHistoryData(action, actionMsg, baseMessage, date));
		}else {
			this.data.put(this.data.size(), new GediminasSpyHistoryData(action, actionMsg, baseMessage, date));
		}
	}
	
	public Map<Integer, GediminasSpyHistoryData> getMessages() {
		return this.data;
	}
	
	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}