package fr.glowstoner.fireapi.gediminas.spy;

import java.io.Serializable;
import java.util.Calendar;

import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;

public class GediminasSpyHistoryData implements Serializable{
	
	private static final long serialVersionUID = -569491528358996172L;

	private SpyAction action;
	private String msg, formatedMessage;
	private Calendar date;
	
	public GediminasSpyHistoryData(SpyAction action, String formatedMessage, String msg, Calendar date) {
		this.action = action;
		this.msg = msg;
		this.date = date;
		
		this.setFormatedMessage(formatedMessage);
	}
	
	public SpyAction getAction() {
		return this.action;
	}
	
	public String getMessage() {
		return this.msg;
	}
	
	public Calendar getDate() {
		return this.date;
	}

	public String getFormatedMessage() {
		return formatedMessage;
	}

	public void setFormatedMessage(String formatedMessage) {
		this.formatedMessage = formatedMessage;
	}
}