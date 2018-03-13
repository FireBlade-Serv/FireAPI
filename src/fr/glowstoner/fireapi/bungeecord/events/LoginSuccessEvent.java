package fr.glowstoner.fireapi.bungeecord.events;

import fr.glowstoner.fireapi.bungeecord.events.enums.LoginSourceType;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class LoginSuccessEvent extends Event{

	private ProxiedPlayer pp;
	private LoginSourceType source;
	
	public LoginSuccessEvent(ProxiedPlayer pp, LoginSourceType type) {
		this.pp = pp;
		this.source = type;
	}
	
	public ProxiedPlayer getPlayer() {
		return this.pp;
	}
	
	public LoginSourceType getSourceType() {
		return this.source;
	}
}
