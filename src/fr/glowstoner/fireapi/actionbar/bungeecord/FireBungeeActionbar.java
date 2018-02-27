package fr.glowstoner.fireapi.actionbar.bungeecord;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FireBungeeActionbar {
	
	@Getter @Setter private ProxiedPlayer player;
	
	public FireBungeeActionbar(ProxiedPlayer pp) {
		this.setPlayer(pp);
	}

	public FireBungeeActionbar() {
		
	}
	
	public void send(String message) {
		this.player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
}
