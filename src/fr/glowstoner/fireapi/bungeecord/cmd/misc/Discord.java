package fr.glowstoner.fireapi.bungeecord.cmd.misc;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Discord extends Command{

	public Discord(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		TextComponent comp = new TextComponent("§aCliquez ICI");
		comp.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://discordapp.com/invite/H7acUcX"));
		comp.setHoverEvent(new HoverEvent
				(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("Cliquez ici pour acceder au discord !").
						color(ChatColor.GREEN).create()));
		TextComponent base = new TextComponent("§b[Discord] ");
		base.addExtra(comp);
		
		sender.sendMessage(base);
	}
}
