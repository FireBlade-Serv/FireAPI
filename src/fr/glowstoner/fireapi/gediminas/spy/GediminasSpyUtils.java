package fr.glowstoner.fireapi.gediminas.spy;

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

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.gediminas.spy.enums.SpyAction;
import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyAction;

public class GediminasSpyUtils {
	
	public static GediminasSpyHistory mergeHistory(GediminasSpyHistory base, PacketSpyAction ps) {
		base.putMessage(ps.getActionDate(), ps.getAction(), ps.getFormatedMsg(), ps.getRawMsg());
		
		return base;
	}

	public static void sendInfosToClient(ConnectionHandler c, String name) throws IOException, 
		ClassNotFoundException, URISyntaxException {
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/spy/"+name+".firespy");
		FileInputStream fi = new FileInputStream(f);
		ObjectInputStream oi = new ObjectInputStream(fi);
		
		GediminasSpyHistory h = (GediminasSpyHistory) oi.readObject();
		
		c.sendMessageWithPrefix("--------------SPY--------------");
		c.sendMessageWithPrefix("size = "+h.getMessages().size());
		c.sendMessageWithPrefix("name = "+h.getPlayerName());
		c.sendMessageWithPrefix("IP = "+h.getIP());
		c.sendMessageWithPrefix("-------------------------------\n");
		
		for(int i = 0 ; i < h.getMessages().size() ; i++) {
			GediminasSpyHistoryData dat = h.getMessages().get(i);
			
			c.sendMessageWithPrefix(dat.getFormatedMessage());
		}
		
		oi.close();
		fi.close();
	}
	
	public static void sendInfosChatToClient(ConnectionHandler c, String name) throws IOException,
		URISyntaxException, ClassNotFoundException{
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/spy/"+name+".firespy");
		FileInputStream fi = new FileInputStream(f);
		ObjectInputStream oi = new ObjectInputStream(fi);
		
		GediminasSpyHistory h = (GediminasSpyHistory) oi.readObject();
		
		for(int i = 0 ; i < h.getMessages().size() ; i++) {
			GediminasSpyHistoryData data = h.getMessages().get(i);
			
			if(data.getAction().equals(SpyAction.PLAYER_CHAT)) {
				c.sendMessageWithPrefix(data.getFormatedMessage());
			}
		}
		
		oi.close();
		fi.close();
	}
	
	public static Map<Integer, GediminasSpyHistoryData> getHistoryByAValue
		(Calendar date, int value, GediminasSpyHistory history) {
		
		Map<Integer, GediminasSpyHistoryData> map = new HashMap<>();
		
		for(int i = 0 ; i < history.getMessages().size() ; i++) {
			GediminasSpyHistoryData data = history.getMessages().get(i);
			
			if(data.getDate().get(value) == date.get(value)) {
				if(map.size() == 0) {
					map.put(0, data);
				}else {
					map.put(map.size(), data);
				}
			}
		}
		
		return map;
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
	
	public static List<GediminasSpyHistoryData> getAllMessagesContainsMessage(String message, GediminasSpyHistory history) {
		List<GediminasSpyHistoryData> list = new ArrayList<>();
		
		for(int i = 0 ; i < history.getMessages().size() ; i++) {
			GediminasSpyHistoryData data = history.getMessages().get(i);
			
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
