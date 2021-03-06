package fr.glowstoner.fireapi.actionbar.utils;

import org.bukkit.ChatColor;

public class FireActionbarUtils {
	
	public static String slideText(String message, int moment, ChatColor colorBase, ChatColor colorExtra) {
		/*
		 * void
		 * 
		 * p
		 * pl
		 * pla
		 * play
		 * 
		 * ... [A] -->
		 * 
		 * play.fireblade-serv.eu
		 * 
		 * 5m color->color 0 (color base)
		 * 5m color->color 1 (color extra)
		 * 
		 * ... 25m
		 * 
		 * lay.fireblade-serv.eu
		 * ay.fireblade-serv.eu
		 *   
		 * ... [D] -->
		 * 
		 * void
		 */
		
		//int voidMD = 0;
		//int fullMD = message.length();
		//int voidMF = message.length() * 2;
		
		/*
		 * StringBuilder builder = new StringBuilder();
		 * 
		 * for(int i = 0 ; i < message.length() ; i++) {
		 * 		builder.append(" ");
		 * }
		 */
		
		if(moment == 0) {
			return "";
		}else if(moment < message.length()) {
			return colorBase+message.substring(0, moment);
		}else if(moment == message.length()) {
			return colorBase+message;
		}else if(moment >= message.length() && moment <= (message.length() + 25)) { 
			if(((((moment - message.length()) / 5) % 2) == 0) ) {
				//0, 2, 4
				//color base
				
				return colorBase+message;
			}else if(((((moment - message.length()) / 5) % 2) == 1)) {
				//1, 3, 5
				//color extra
				
				return colorExtra+message;
			}else {
				return "§c§lErreur !";
			}
		}else if(moment > (message.length() + 25)) {
			return colorBase+message.substring((moment - (message.length() + 25)));
		}else {
			return "§c§lErreur !";
		}
	}
}