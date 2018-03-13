package fr.glowstoner.fireapi;

import fr.glowstoner.fireapi.bukkit.BukkitMain;
import fr.glowstoner.fireapi.bungeecord.BungeeMain;

public class FirePlugin {
	
	public static FireAPI getBukkitAPI() {
		return BukkitMain.getAPI();
	}

	public static FireAPI getBungeeAPI() {
		return BungeeMain.getAPI();
	}
}
