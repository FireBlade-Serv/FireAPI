package fr.glowstoner.fireapi.bungeecord.commands.misc;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Website extends Command{

	public Website(String cmd) {
		super(cmd);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent comp = new TextComponent("§aCliquez ICI");
		comp.setClickEvent(new ClickEvent(Action.OPEN_URL, "http://www.fireblade-serv.eu/"));
		comp.setHoverEvent(new HoverEvent
				(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("Cliquez ici pour acceder au site internet !").
						color(ChatColor.GREEN).create()));
		TextComponent base = new TextComponent("§c[Site] ");
		base.addExtra(comp);
		
		sender.sendMessage(base);
	}
}
