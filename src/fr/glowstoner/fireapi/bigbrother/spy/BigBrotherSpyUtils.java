package fr.glowstoner.fireapi.bigbrother.spy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.glowstoner.fireapi.bigbrother.spy.enums.SpyAction;
import fr.glowstoner.fireapi.bigbrother.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.calendar.FireCalendar;
import fr.glowstoner.fireapi.calendar.enums.FireCalendarFormat;
import fr.glowstoner.fireapi.network.ConnectionHandler;

public class BigBrotherSpyUtils {
	
	public static BigBrotherSpyHistory mergeHistory(BigBrotherSpyHistory base, PacketSpyAction ps) {
		base.putMessage(ps.toFireCalendar(), ps.getAction(), ps.getFormatedMsg(), ps.getRawMsg());
		
		return base;
	}

	public static void sendInfosToClient(ConnectionHandler c, String name) throws IOException, 
		ClassNotFoundException, URISyntaxException {
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/spy/"+name+".firespy");
		FileInputStream fi = new FileInputStream(f);
		ObjectInputStream oi = new ObjectInputStream(fi);
		
		BigBrotherSpyHistory h = (BigBrotherSpyHistory) oi.readObject();
		
		c.sendMessageWithPrefix("--------------SPY--------------");
		c.sendMessageWithPrefix("size = "+h.getMessages().size());
		c.sendMessageWithPrefix("name = "+h.getPlayerName());
		c.sendMessageWithPrefix("IP = "+h.getIP());
		c.sendMessageWithPrefix("-------------------------------\n");
		
		for(int i = 0 ; i < h.getMessages().size() ; i++) {
			BigBrotherSpyHistoryData dat = h.getMessages().get(i);
			
			c.sendMessageWithPrefix(dat.getFormatedMessage());
		}
		
		oi.close();
		fi.close();
	}
	
	public static void sendInfosChatToClient(ConnectionHandler c, String name) throws IOException,
		URISyntaxException, ClassNotFoundException {
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/spy/"+name+".firespy");
		FileInputStream fi = new FileInputStream(f);
		ObjectInputStream oi = new ObjectInputStream(fi);
		
		BigBrotherSpyHistory h = (BigBrotherSpyHistory) oi.readObject();
		
		for(int i = 0 ; i < h.getMessages().size() ; i++) {
			BigBrotherSpyHistoryData data = h.getMessages().get(i);
			
			if(data.getAction().equals(SpyAction.PLAYER_CHAT)) {
				c.sendMessageWithPrefix(data.getFormatedMessage());
			}
		}
		
		oi.close();
		fi.close();
	}
	
	public static Map<Integer, BigBrotherSpyHistoryData> getHistoryByAValue
		(FireCalendar date, FireCalendarFormat format, BigBrotherSpyHistory history) {
		
		Map<Integer, BigBrotherSpyHistoryData> map = new HashMap<>();
		
		for(int i = 0 ; i < history.getMessages().size() ; i++) {
			BigBrotherSpyHistoryData data = history.getMessages().get(i);
			
			if(data.getDate().get(format) == date.get(format)) {
				if(map.size() == 0) {
					map.put(0, data);
				}else {
					map.put(map.size(), data);
				}
			}
		}
		
		return map;
	}
	
	public static File[] getAllSpyFiles() {
		Path path = null;
		
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return new File(path.toString()+"/spy/").listFiles();
	}
	
	public static String getFormatedDate(FireCalendar date) {
		
		return date.getHourOfDay()+"h"+date.getMinute()+"min"+date.getSecond()+"sec"+date.getMilisecond()+"ms - "+
				date.getDayOfMonth()+"/"+date.getMonth()+"/"+date.getYear();
	}
	
	public static List<BigBrotherSpyHistoryData> getAllMessagesContainsMessage(String message, BigBrotherSpyHistory history) {
		List<BigBrotherSpyHistoryData> list = new ArrayList<>();
		
		for(int i = 0 ; i < history.getMessages().size() ; i++) {
			BigBrotherSpyHistoryData data = history.getMessages().get(i);
			
			if(data.getAction().equals(SpyAction.PLAYER_CHAT)) {
				String lmsg = message.toLowerCase();
				String ldata = data.getMessage().toLowerCase();
				
				if(ldata.contains(lmsg)) {
					list.add(data);
				}
			}
		}
		
		return list;
	}
}
