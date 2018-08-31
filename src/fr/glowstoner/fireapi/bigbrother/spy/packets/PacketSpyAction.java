package fr.glowstoner.fireapi.bigbrother.spy.packets;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.glowstoner.fireapi.bigbrother.spy.enums.SpyAction;
import fr.glowstoner.fireapi.calendar.FireCalendar;
import fr.glowstoner.fireapi.network.packets.Encryptable;
import fr.glowstoner.fireapi.network.packets.Packet;

public class PacketSpyAction extends Packet implements Encryptable{

	private static final long serialVersionUID = 2494871161464801144L;

	private SpyAction action;
	private String playerName, actionMsg, ip;
	private int dayOfMonth, month, hourOfDay, second, minute, milisecond, year; 
	
	public PacketSpyAction(String name, String ip, String actionmsg, SpyAction action, boolean setDate) {
		this.setAction(action);
		this.setPlayerName(name);
		this.setActionMsg(actionmsg);
		this.setIP(ip);
		
		if(setDate) this.setDateToNow();
	}
	
	public PacketSpyAction() {
		
	}
	
	public void setDateToNow() {
		Calendar d = GregorianCalendar.getInstance();
		d.setTime(new Date());
		
		this.setDayOfMonth(d.get(Calendar.DAY_OF_MONTH));
		this.setMonth(d.get(Calendar.MONTH));
		this.setHourOfDay(d.get(Calendar.HOUR_OF_DAY));
		this.setSecond(d.get(Calendar.HOUR_OF_DAY));
		this.setMinute(d.get(Calendar.MINUTE));
		this.setMilisecond(d.get(Calendar.MILLISECOND));
		this.setYear(d.get(Calendar.YEAR));
	}
	
	public FireCalendar toFireCalendar() {
		return FireCalendar.builder()
				.dayOfMonth(this.dayOfMonth)
				.hourOfDay(this.hourOfDay)
				.milisecond(this.milisecond)
				.minute(this.minute)
				.month(this.month)
				.second(this.second)
				.year(this.year)
				.build();
	}
	
	public String getFormatedDate() {
		return this.hourOfDay+"h"+this.minute+"min"+this.second+"sec"+
				this.milisecond+"ms - "+this.dayOfMonth+"/"+this.month+"/"+this.year;
	}
	
	public String getFormatedMsg() {
		return this.action.name()+" @ "+this.getFormatedDate()+" @ "+this.getActionMsg();
	}
	
	public String getRawMsg() {
		return this.actionMsg;
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

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getMilisecond() {
		return milisecond;
	}

	public void setMilisecond(int milisecond) {
		this.milisecond = milisecond;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String[] encryptedFields() {
		return new String[] {"action", "playerName", "actionMsg", "ip", "dayOfMonth", "month", "hourOfDay", "second",
				"minute", "milisecond", "year"};
	}

	@Override
	public boolean encrypted() {
		return true;
	}
}