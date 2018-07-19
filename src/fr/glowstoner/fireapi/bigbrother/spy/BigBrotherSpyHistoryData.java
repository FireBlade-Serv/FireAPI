package fr.glowstoner.fireapi.bigbrother.spy;

import java.io.Serializable;

import fr.glowstoner.fireapi.bigbrother.spy.enums.SpyAction;
import fr.glowstoner.fireapi.calendar.FireCalendar;

public class BigBrotherSpyHistoryData implements Serializable{
	
	private static final long serialVersionUID = -569491528358996172L;

	private SpyAction action;
	private String msg, formatedMessage;
	private FireCalendar date;
	
	public BigBrotherSpyHistoryData(SpyAction action, String formatedMessage, String msg, FireCalendar date) {
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
	
	public FireCalendar getDate() {
		return this.date;
	}

	public String getFormatedMessage() {
		return formatedMessage;
	}

	public void setFormatedMessage(String formatedMessage) {
		this.formatedMessage = formatedMessage;
	}
}