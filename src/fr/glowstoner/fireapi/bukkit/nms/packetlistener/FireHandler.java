package fr.glowstoner.fireapi.bukkit.nms.packetlistener;

import org.bukkit.entity.Player;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class FireHandler extends ChannelDuplexHandler{

	private Player p;
	
	public FireHandler(Player p) {
		this.p = p;
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		super.write(ctx, msg, promise);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
		FirePacketListener.callListener(p, packet);
		
		super.channelRead(ctx, packet);
	}
}
