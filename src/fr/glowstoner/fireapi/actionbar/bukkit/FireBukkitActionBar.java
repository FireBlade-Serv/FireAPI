package fr.glowstoner.fireapi.actionbar.bukkit;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class FireBukkitActionBar{
	
	@Getter @Setter private Player player;
	
	public FireBukkitActionBar(Player p) {
		this.setPlayer(p);
	}
	
	public FireBukkitActionBar() {
		
	}

	public void send(String message) {
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
			
		((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(ppoc);
	}
}