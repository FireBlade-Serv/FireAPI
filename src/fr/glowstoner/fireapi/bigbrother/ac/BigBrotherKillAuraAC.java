package fr.glowstoner.fireapi.bigbrother.ac;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bigbrother.ac.packet.PacketBigBrotherAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherActionAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherCheatAC;
import fr.glowstoner.fireapi.bigbrother.ac.packet.enums.BigBrotherTypeAC;
import fr.glowstoner.fireapi.utils.LocationUtil;
import fr.glowstoner.fireapi.utils.MathUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;

public class BigBrotherKillAuraAC {
	
	@Getter private static Map<Player, Boolean> clicked = new HashMap<>();
	@Getter private static Map<Player, Integer> fakePlayers = new HashMap<>();
	
	@Getter private Player player;
	@Getter private boolean fakePlayerSpawned;
	@Getter private int ID;
	
	private EntityPlayer ep;
	
	public BigBrotherKillAuraAC(Player p) {
		this.player = p;
	}

	public void startRegularCheck(FireAPI api) {
		//spawn
		this.spawnFakePlayer((byte) 1);
		
		fakePlayers.put(this.player, this.ID);
		clicked.put(player, false);
			
		api.getBukkitPlugin().getServer().getScheduler().scheduleSyncDelayedTask(api.getBukkitPlugin(), new Runnable() {
				
			@Override
			public void run() {
				//unspawn

				removeFakePlayer();
				
				if(clicked.get(getPlayer()) == true) {					
					clicked.remove(getPlayer());
					fakePlayers.remove(getPlayer());
					
					//check 2
					startClickedCheck(api, (byte) 2);
				}else {					
					clicked.remove(getPlayer());
					fakePlayers.remove(getPlayer());
				}
			}
				
		}, (long) MathUtil.getRandomWithMin(20.0d, 5.0d));
	}
	
	public void startClickedCheck(FireAPI api, byte check) {
		//spawn
		this.spawnFakePlayer(check);
		
		fakePlayers.put(this.player, this.ID);
		clicked.put(this.player, false);
		
		api.getBukkitPlugin().getServer().getScheduler().scheduleSyncDelayedTask(api.getBukkitPlugin(), new Runnable() {
			
			@Override
			public void run() {
				//unspawn

				removeFakePlayer();
				
				if(clicked.get(getPlayer()) == true) {
					clicked.remove(getPlayer());
					fakePlayers.remove(getPlayer());
					
					if(check < (byte) 5) {
						startClickedCheck(api, (byte) (check + 1));
					}else {
						//cheat
						
						try {
							api.getClient().sendPacket(new PacketBigBrotherAC(getPlayer().getName(), "KillAura 5/5",
									BigBrotherTypeAC.CHEAT_DETECTION, BigBrotherActionAC.INFORM_STAFF, BigBrotherCheatAC.KILLAURA,
									((CraftPlayer) getPlayer()).getHandle().ping), api.encryptionKey());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}else {	
					clicked.remove(getPlayer());
					fakePlayers.remove(getPlayer());
					
					if(check == (byte) 4) {
						try {
							api.getClient().sendPacket(new PacketBigBrotherAC(getPlayer().getName(), "KillAura 4/5",
									BigBrotherTypeAC.CHEAT_DETECTION, BigBrotherActionAC.INFORM_STAFF, BigBrotherCheatAC.KILLAURA,
									((CraftPlayer) getPlayer()).getHandle().ping), api.encryptionKey());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}, (long) MathUtil.getRandomWithMin(20.0d, 5.0d));
	}
	
	//fake players
	
	private void spawnFakePlayer(byte check) {
		if(!this.isFakePlayerSpawned()) {
			//add
			this.ep = this.getEntityPlayer(this.player.getWorld());
			this.ID = this.ep.getBukkitEntity().getEntityId();
			
			this.ep.getBukkitEntity().getInventory().setBoots(this.getBootsItem());
				
			Location sloc = this.getFakePlayerSpawningLocation(this.player, check);
			
			this.ep.setLocation(sloc.getX(), sloc.getY(), sloc.getZ(), sloc.getYaw(), sloc.getPitch());
			
			this.getConnection().sendPacket(this.getInfoPacket(this.ep, EnumPlayerInfoAction.ADD_PLAYER));
			this.getConnection().sendPacket(this.getSpawnPacket(this.ep));
			
			this.fakePlayerSpawned = true;
		}
	}
	
	private void removeFakePlayer() {
		if(this.isFakePlayerSpawned()) {
			if(this.ep != null) {
				//remove
				
				this.getConnection().sendPacket(this.getInfoPacket(this.ep, EnumPlayerInfoAction.REMOVE_PLAYER));
				this.getConnection().sendPacket(this.getDestroyPacket(this.ep));
				
				this.fakePlayerSpawned = false;
			}
		}
	}
	
	private PlayerConnection getConnection() {
		return ((CraftPlayer) this.player).getHandle().playerConnection;
	}
	
	private GameProfile getGameProfile() {
		int ranint = (int) (Math.random() * 100);
		
		return new GameProfile(UUID.fromString("18e00b7a-4b4c-4468-aa93-bfa81fc8d422"), "§cBigBrother #"+ranint);
	}
	
	private EntityPlayer getEntityPlayer(World w) {
		return new EntityPlayer(MinecraftServer.getServer(), ((CraftWorld) w).getHandle(),
				this.getGameProfile(), new PlayerInteractManager(((CraftWorld) w).getHandle()));
	}
	
	private ItemStack getBootsItem() {
		return new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS);
	}
	
	private PacketPlayOutPlayerInfo getInfoPacket(EntityPlayer ep, EnumPlayerInfoAction info) {
		return new PacketPlayOutPlayerInfo(info, ep);
	}
	
	private PacketPlayOutNamedEntitySpawn getSpawnPacket(EntityPlayer ep) {
		return new PacketPlayOutNamedEntitySpawn(ep);
	}
	
	private PacketPlayOutEntityDestroy getDestroyPacket(EntityPlayer ep) {
		return new PacketPlayOutEntityDestroy(ep.getBukkitEntity().getEntityId());
	}
	
	private Location getFakePlayerSpawningLocation(Player p, byte check) {
		if(check == (byte) 1) {
			return LocationUtil.getLocationInOppositeDirection(p, 2.5d).subtract(0.0d, 1.0d, 0.0d);
		}else if(check == (byte) 2) {
			return LocationUtil.getLocationInOppositeDirection(p, 2.5d).add(0.0d, 1.0d, 0.0d);
		}else if(check == (byte) 3) {
			return LocationUtil.getLocationInOppositeDirection(p, 2.25d).add(0.75d, 0.5d, 0.25d);
		}else if(check == (byte) 4) {
			return p.getLocation().subtract(0.0d, 1.0d, 0.0d);
		}else if(check == (byte) 5) {
			return p.getLocation().add(0.0d, 2.0d, 0.0d);
		}else {
			return null;
		}
	}
}