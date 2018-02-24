package fr.glowstoner.fireapi.bukkit.nms.scoreboard;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.bukkit.nms.FireReflection;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore.EnumScoreboardAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;

/*
 * @author Glowstoner
 * 
 * FireReflection.java : https://gist.github.com/Glowstoner/10bb462f24c7fb0c3cc189a7bc82dfc7
 */

public class FireScoreboard {
	
	private Map<Integer, String> lines = new HashMap<>();
	private String title;
	private Player p;
	private PlayerConnection c;
	private int blank;
	
	public FireScoreboard(Player p, String title, Map<Integer, String> lines) {
		this.p = p;
		this.title = title;
		this.c = ((CraftPlayer) this.p).getHandle().playerConnection;
		
		this.lines = lines;
	}
	
	public void baseSend() {
		try {
			c.sendPacket(this.getScoreboardObjective(0));
			c.sendPacket(this.getScoreboardDisplayer());
			
			for(int i = 0 ; i < this.lines.size() ; i++) {
				c.sendPacket(this.getScoreboardScore(this.lines.get(i), (15 - i)));
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void removeLine(int line) {
		c.sendPacket(new PacketPlayOutScoreboardScore(this.lines.get(line)));
	}
	
	public void replaceLine(int line, String value) {
		this.lines.replace(line, value);
		
		try {
			c.sendPacket(this.getScoreboardScore(value, (15 - line)));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void updateLine(int line, String value) {
		this.removeLine(line);
		this.replaceLine(line, value);
	}
	
	public String getLine(int line) {
		return this.lines.get(line);
	}
	
	private PacketPlayOutScoreboardObjective getScoreboardObjective(int mode) throws IllegalArgumentException, IllegalAccessException {
		PacketPlayOutScoreboardObjective pso = new PacketPlayOutScoreboardObjective();
		
		Field name = FireReflection.getField(pso.getClass(), "a");
		name.set(pso, this.p.getName());
		
		Field display = FireReflection.getField(pso.getClass(), "b");
		display.set(pso, this.title);
		
		Field dtype = FireReflection.getField(pso.getClass(), "c");
		dtype.set(pso, EnumScoreboardHealthDisplay.INTEGER);
		
		Field smode = FireReflection.getField(pso.getClass(), "d");
		smode.set(pso, mode);
		
		return pso;
	}
	
	private PacketPlayOutScoreboardDisplayObjective getScoreboardDisplayer() throws IllegalArgumentException, IllegalAccessException {
		PacketPlayOutScoreboardDisplayObjective pdo = new PacketPlayOutScoreboardDisplayObjective(); //pedo mdr
		
		Field mode = FireReflection.getField(pdo.getClass(), "a");
		mode.set(pdo, 1);
		
		Field name = FireReflection.getField(pdo.getClass(), "b");
		name.set(pdo, this.p.getName());
		
		return pdo;
	}
	
	private PacketPlayOutScoreboardScore getScoreboardScore(String line, int score) throws IllegalArgumentException, IllegalAccessException {
		PacketPlayOutScoreboardScore pss = new PacketPlayOutScoreboardScore(this.check(line));
		
		Field name = FireReflection.getField(pss.getClass(), "b");
		name.set(pss, this.p.getName());
		
		Field scoref = FireReflection.getField(pss.getClass(), "c");
		scoref.set(pss, score);
		
		Field action = FireReflection.getField(pss.getClass(), "d");
		action.set(pss, EnumScoreboardAction.CHANGE);
		
		return pss;
	}
	
	private String check(String line) {
		if(line.length() == 0) {
			StringBuilder builder = new StringBuilder();
			
			for(int i = 0 ; i < (this.blank + 1) ; i++) {
				builder.append(" ");
			}
			
			this.blank++;
			
			return line+builder.toString();
		}else {
			return line;
		}
	}
}