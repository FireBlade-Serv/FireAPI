package fr.glowstoner.fireapi.gediminas.spy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.glowstoner.connectionsapi.network.ConnectionHandler;
import fr.glowstoner.fireapi.gediminas.spy.packets.PacketSpyAction;

public class GediminasSpyUtils {
	
	public static GediminasSpyHistory mergeHistory(GediminasSpyHistory base, PacketSpyAction ps) {
		base.putMessage(ps.getActionDate(), ps.getAction(), ps.getFormatedMsg());
		
		return base;
	}

	public static void sendInfosToClient(ConnectionHandler c, String name) throws IOException, 
		ClassNotFoundException, URISyntaxException {
		
		Path path = Paths.get(ClassLoader.getSystemResource("").toURI());
		
		File f = new File(path.toString()+"/"+name+".firespy");
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
			
			System.out.println(dat.getMessage());
		}
		
		oi.close();
		fi.close();
	}
}
