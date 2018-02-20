package fr.glowstoner.fireapi.bukkit.bossbar;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class FireBossBar {
	
	private Player p;
	
	public FireBossBar(Player p) {
		this.p = p;
	}
	
	public void gen(org.bukkit.World w, String title) {
		EntityEnderDragon ew = new EntityEnderDragon(((CraftWorld) w).getHandle());
		Location loc = this.p.getLocation();
		
		ew.setInvisible(true);
		ew.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
		ew.setCustomName(title);
		
		PacketPlayOutSpawnEntityLiving pel = new PacketPlayOutSpawnEntityLiving(ew);
		
		((CraftPlayer) this.p).getHandle().playerConnection.sendPacket(pel);
	}
}
