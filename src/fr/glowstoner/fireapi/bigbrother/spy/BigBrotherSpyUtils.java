package fr.glowstoner.fireapi.bigbrother.spy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.glowstoner.fireapi.bigbrother.spy.enums.SpyAction;
import fr.glowstoner.fireapi.bigbrother.spy.packets.PacketSpyAction;
import fr.glowstoner.fireapi.crypto.EncryptionKey;
import fr.glowstoner.fireapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.utils.calendar.FireCalendar;
import fr.glowstoner.fireapi.utils.calendar.enums.FireCalendarFormat;
import lombok.NonNull;

public class BigBrotherSpyUtils {
	
	public static BigBrotherSpyHistory mergeHistory(BigBrotherSpyHistory base, PacketSpyAction ps) {
		base.putMessage(BigBrotherSpyUtils.toFireCalendar(ps.getActionDate()), ps.getAction(), ps.getFormatedMsg(), ps.getRawMsg());
		
		return base;
	}

	public static void sendInfosToClient(ConnectionHandler c, String name, EncryptionKey key) throws IOException, 
		ClassNotFoundException, URISyntaxException {
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/spy/"+name+".firespy");
		FileInputStream fi = new FileInputStream(f);
		ObjectInputStream oi = new ObjectInputStream(fi);
		
		BigBrotherSpyHistory h = (BigBrotherSpyHistory) oi.readObject();
		
		c.sendMessageWithPrefix("--------------SPY--------------", key);
		c.sendMessageWithPrefix("size = "+h.getMessages().size(), key);
		c.sendMessageWithPrefix("name = "+h.getPlayerName(), key);
		c.sendMessageWithPrefix("IP = "+h.getIP(), key);
		c.sendMessageWithPrefix("-------------------------------\n", key);
		
		for(int i = 0 ; i < h.getMessages().size() ; i++) {
			BigBrotherSpyHistoryData dat = h.getMessages().get(i);
			
			c.sendMessageWithPrefix(dat.getFormatedMessage(), key);
		}
		
		oi.close();
		fi.close();
	}
	
	public static void sendInfosChatToClient(ConnectionHandler c, String name, EncryptionKey key) throws IOException,
		URISyntaxException, ClassNotFoundException{
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/spy/"+name+".firespy");
		FileInputStream fi = new FileInputStream(f);
		ObjectInputStream oi = new ObjectInputStream(fi);
		
		BigBrotherSpyHistory h = (BigBrotherSpyHistory) oi.readObject();
		
		for(int i = 0 ; i < h.getMessages().size() ; i++) {
			BigBrotherSpyHistoryData data = h.getMessages().get(i);
			
			if(data.getAction().equals(SpyAction.PLAYER_CHAT)) {
				c.sendMessageWithPrefix(data.getFormatedMessage(), key);
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
		@NonNull Path path = null;
		
		try {
			path = Paths.get(ClassLoader.getSystemResource("").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return new File(path.toString()+"/spy/").listFiles();
	}
	
	public static String getFormatedDate(Calendar date) {
		int ms = date.get(Calendar.MILLISECOND);
		int s = date.get(Calendar.SECOND);
		int m = date.get(Calendar.MINUTE);
		int h = date.get(Calendar.HOUR_OF_DAY);
		int d = date.get(Calendar.DAY_OF_MONTH);
		int mo = date.get(Calendar.MONTH);
		int y = date.get(Calendar.YEAR);
		
		return h+"h"+m+"min"+s+"sec"+ms+"ms - "+d+"/"+mo+"/"+y;
	}
	
	public static String getFormatedDate(FireCalendar date) {
		int ms = date.get(FireCalendarFormat.MILISECOND);
		int s = date.get(FireCalendarFormat.SECOND);
		int m = date.get(FireCalendarFormat.MINUTE);
		int h = date.get(FireCalendarFormat.HOUR_OF_DAY);
		int d = date.get(FireCalendarFormat.DAY_OF_MOUTH);
		int mo = date.get(FireCalendarFormat.MOUTH);
		int y = date.get(FireCalendarFormat.YEAR);
		
		return h+"h"+m+"min"+s+"sec"+ms+"ms - "+d+"/"+mo+"/"+y;
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
	
	public static FireCalendar toFireCalendar(Calendar date) {
		return FireCalendar.builder()
				.dayOfMonth(date.get(Calendar.DAY_OF_MONTH))
				.hourOfDay(date.get(Calendar.HOUR_OF_DAY))
				.milisecond(date.get(Calendar.MILLISECOND))
				.minute(date.get(Calendar.MINUTE))
				.second(date.get(Calendar.SECOND))
				.month((date.get(Calendar.MONTH) + 1))
				.year(date.get(Calendar.YEAR))
				.build();
	}
}
