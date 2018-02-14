package fr.glowstoner.fireapi.rank;

import net.md_5.bungee.api.ChatColor;

public enum Rank {
	
	MEMBRE(ChatColor.GRAY, 1), FIRE(ChatColor.YELLOW, 2), ULTRA(ChatColor.GREEN, 3), ULTIMATE(ChatColor.DARK_PURPLE, 4),
	YOUTUBER(ChatColor.DARK_GREEN, 5), GUIDE(ChatColor.DARK_GREEN, 6), ASSISTANT(ChatColor.GOLD, 7),
	RESPONSABLE_COM(ChatColor.GOLD, 8), MODÉRATEUR(ChatColor.DARK_AQUA, 9), MANAGER_IG(ChatColor.RED, 10),
	STAFF_MANAGER(ChatColor.RED, 11), BUILDER(ChatColor.GOLD, 12), DÉVELOPPEUR(ChatColor.BLUE, 13),
	ADMINISTRATEUR(ChatColor.DARK_RED, 14), CONSOLE(ChatColor.BLACK, 15);

	private ChatColor color;
	private int power;
	
	private Rank(ChatColor color, int power) {
		this.color = color;
		this.power = power;
	}
	
	public ChatColor getColor() {
		return this.color;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public boolean isConsole() {
		return this.equals(Rank.CONSOLE);
	}
}
