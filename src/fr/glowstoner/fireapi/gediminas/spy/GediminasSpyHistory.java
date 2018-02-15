package fr.glowstoner.fireapi.gediminas.spy;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;

public class GediminasSpyHistory implements Serializable{
	
	private static final long serialVersionUID = -5659480433388585225L;
	
	private String playerName, ip;
	private Map<Integer, GediminasSpyHistoryData> data;
	
	public GediminasSpyHistory(String name, String ip) {
		this.data = new HashMap<>();
	}
	
	public GediminasSpyHistory() {
		
	}
	
	public void putMessage(Calendar date, SpyAction action, String actionMsg) {
		if(this.data.size() == 0) {
			this.data.put(0, new GediminasSpyHistoryData(action, actionMsg, date));
		}else {
			this.data.put(this.data.size(), new GediminasSpyHistoryData(action, actionMsg, date));
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