package fr.glowstoner.fireapi.bukkit.nms.title;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class FireTitle {
	
	private int FadeIn, Stay, FadeOut;
	
	private String title;
	private String subtitle;
	
	private Player player;
	
	public FireTitle(Player p, String title, String subtitle, int in, int stay, int out){
		this.FadeIn = in;
		this.Stay = stay;
		this.FadeOut = out;
		
		this.title = title;
		this.subtitle = subtitle;
		
		this.player = p;
	}
	
	public void send(){
		IChatBaseComponent basetitle = ChatSerializer.a("{\"text\": \"" + this.title + "\"}");
		IChatBaseComponent basesubtitle = ChatSerializer.a("{\"text\": \"" + this.subtitle + "\"}");
		PacketPlayOutTitle titlepacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, basetitle);
		PacketPlayOutTitle subtitlepacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, basesubtitle);
		
		((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(titlepacket);
		((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(subtitlepacket);
		
		sendTime();
	}
	
	private void sendTime(){
		PacketPlayOutTitle time = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, this.FadeIn, this.Stay, this.FadeOut);
		((CraftPlayer)this.player).getHandle().playerConnection.sendPacket(time);
	}
}
