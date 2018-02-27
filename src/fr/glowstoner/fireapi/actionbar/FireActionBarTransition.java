package fr.glowstoner.fireapi.actionbar;

import org.bukkit.ChatColor;

import fr.glowstoner.fireapi.actionbar.enums.TransitionType;
import lombok.Data;

@Data
public class FireActionBarTransition {

	private String text;
	private TransitionType type;
	private ChatColor colorBase, colorExtra;
	
	public FireActionBarTransition(String text, TransitionType type, ChatColor colorBase, ChatColor colorExtra) {
		this.setColorBase(colorBase);
		this.setColorExtra(colorExtra);
		this.setType(type);
		this.setText(text);
	}
	
	public FireActionBarTransition() {
		
	}
}
