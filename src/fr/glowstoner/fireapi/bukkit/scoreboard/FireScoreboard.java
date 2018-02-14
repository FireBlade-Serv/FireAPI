package fr.glowstoner.fireapi.bukkit.scoreboard;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

public class FireScoreboard {
	
	private String DisplayName;
	
	private boolean line1A, line2A, line3A, line4A, line5A, line6A, line7A,
			line8A, line9A, line10A, line11A, line12A, line13A, line14A, line15A;
		
	private Player player;
	
	private Scoreboard scoreboard = new Scoreboard();
	private ScoreboardObjective objective;
	
	private ScoreboardScore line1, line2, line3, line4, line5, line6, line7,
			line8, line9, line10, line11, line12, line13, line14, line15;
	
	public FireScoreboard(Player p, String name){
		this.DisplayName = name;
		this.player = p;
		
		 objective = scoreboard.registerObjective(this.DisplayName, IScoreboardCriteria.b);
	}
	
	public void setLine(String msg, int line){
		if(line == 1){
			line1 = new ScoreboardScore(scoreboard, objective, msg);
			line1.setScore(15);
			
			line1A = true;
		}else if(line == 2){
			line2 = new ScoreboardScore(scoreboard, objective, msg);
			line2.setScore(14);
			
			line2A = true;
		}else if(line == 3){
			line3 = new ScoreboardScore(scoreboard, objective, msg);
			line3.setScore(13);
			
			line3A = true;
		}else if(line == 4){
			line4 = new ScoreboardScore(scoreboard, objective, msg);
			line4.setScore(12);
			
			line4A = true;
		}else if(line == 5){
			line5 = new ScoreboardScore(scoreboard, objective, msg);
			line5.setScore(11);
			
			line5A = true;
		}else if(line == 6){
			line6 = new ScoreboardScore(scoreboard, objective, msg);
			line6.setScore(10);
			
			line6A = true;
		}else if(line == 7){
			line7 = new ScoreboardScore(scoreboard, objective, msg);
			line7.setScore(9);
			
			line7A = true;
		}else if(line == 8){
			line8 = new ScoreboardScore(scoreboard, objective, msg);
			line8.setScore(8);
			
			line8A = true;
		}else if(line == 9){
			line9 = new ScoreboardScore(scoreboard, objective, msg);
			line9.setScore(7);
			
			line9A = true;
		}else if(line == 10){
			line10 = new ScoreboardScore(scoreboard, objective, msg);
			line10.setScore(6);
			
			line10A = true;
		}else if(line == 11){
			line11 = new ScoreboardScore(scoreboard, objective, msg);
			line11.setScore(5);
			
			line11A = true;
		}else if(line == 12){
			line12 = new ScoreboardScore(scoreboard, objective, msg);
			line12.setScore(4);
			
			line12A = true;
		}else if(line == 13){
			line13 = new ScoreboardScore(scoreboard, objective, msg);
			line13.setScore(3);
			
			line13A = true;
		}else if(line == 14){
			line14 = new ScoreboardScore(scoreboard, objective, msg);
			line14.setScore(2);
			
			line14A = true;
		}else if(line == 15){
			line15 = new ScoreboardScore(scoreboard, objective, msg);
			line15.setScore(1);
			
			line15A = true;
		}
	}
	
	public void sendPacket(){
		PacketPlayOutScoreboardObjective create = new PacketPlayOutScoreboardObjective(objective, 0);
		PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, objective);
		
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardObjective(objective, 1));
		
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(create);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(display);
		
		if(line1A == true){
			PacketPlayOutScoreboardScore packet1 = new PacketPlayOutScoreboardScore(line1);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet1);
		}
		
		if(line2A == true){
			PacketPlayOutScoreboardScore packet2 = new PacketPlayOutScoreboardScore(line2);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet2);
		}
		
		if(line3A == true){
			PacketPlayOutScoreboardScore packet3 = new PacketPlayOutScoreboardScore(line3);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet3);
		}
		
		if(line4A == true){
			PacketPlayOutScoreboardScore packet4 = new PacketPlayOutScoreboardScore(line4);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet4);
		}
		
		if(line5A == true){
			PacketPlayOutScoreboardScore packet5 = new PacketPlayOutScoreboardScore(line5);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet5);
		}
		
		if(line6A == true){
			PacketPlayOutScoreboardScore packet6 = new PacketPlayOutScoreboardScore(line6);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet6);
		}
		
		if(line7A == true){
			PacketPlayOutScoreboardScore packet7 = new PacketPlayOutScoreboardScore(line7);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet7);
		}
		
		if(line8A == true){
			PacketPlayOutScoreboardScore packet8 = new PacketPlayOutScoreboardScore(line8);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet8);
		}
		
		if(line9A == true){
			PacketPlayOutScoreboardScore packet9 = new PacketPlayOutScoreboardScore(line9);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet9);
		}
		
		if(line10A == true){
			PacketPlayOutScoreboardScore packet10 = new PacketPlayOutScoreboardScore(line10);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet10);
		}
		
		if(line11A == true){
			PacketPlayOutScoreboardScore packet11 = new PacketPlayOutScoreboardScore(line11);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet11);
		}
		
		if(line12A == true){
			PacketPlayOutScoreboardScore packet12 = new PacketPlayOutScoreboardScore(line12);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet12);
		}
		
		if(line13A == true){
			PacketPlayOutScoreboardScore packet13 = new PacketPlayOutScoreboardScore(line13);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet13);
		}
		
		if(line14A == true){
			PacketPlayOutScoreboardScore packet14 = new PacketPlayOutScoreboardScore(line14);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet14);
		}
		
		if(line15A == true){
			PacketPlayOutScoreboardScore packet15 = new PacketPlayOutScoreboardScore(line15);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet15);
		}
	}
}
