package fr.glowstoner.fireapi.actionbar;

import org.bukkit.ChatColor;

import fr.glowstoner.fireapi.actionbar.enums.TransitionType;
import lombok.Data;

@Data
public class FireActionBarTransition {

	private String text;
	private TransitionType type;
	private ChatColor colorBase, colorExtra;
}
