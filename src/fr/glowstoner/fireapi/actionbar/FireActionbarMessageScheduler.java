package fr.glowstoner.fireapi.actionbar;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.actionbar.bukkit.FireBukkitActionBar;
import fr.glowstoner.fireapi.actionbar.enums.TransitionType;
import lombok.Getter;

public class FireActionbarMessageScheduler extends BukkitRunnable{
	
	@Getter private Map<Integer, FireActionBarTransition> map = new HashMap<>();
	@Getter private int moment, now;
	
	private FireAPI api;
	private Player p;
	private boolean all;
	private FireActionBarTransition text;
	
	public FireActionbarMessageScheduler(FireAPI api, Map<Integer, FireActionBarTransition> map, boolean all, Player p) {
		this.map = map;
		this.api = api;
		this.all = all;
		this.p = p;
	}
	
	public void start(long period) {
		super.runTaskTimer(this.api.getBukkitPlugin(), 0L, period);
	}

	@Override
	public void run() {
		this.text = this.map.get(this.now);
		
		if(this.text.getType().equals(TransitionType.SLIDE)) {
			if(this.all) {
				for(Player pall : this.api.getBukkitPlugin().getServer().getOnlinePlayers()) {
					new FireBukkitActionBar(pall).send(FireActionbarUtils.slideText
							(this.text.getText(), this.moment, this.text.getColorBase(), this.text.getColorExtra()));
				}
			}else {
				new FireBukkitActionBar(this.p).send(FireActionbarUtils.slideText
						(this.text.getText(), this.moment, this.text.getColorBase(), this.text.getColorExtra()));
			}
			
			if(((this.text.getText().length() * 2) + 25) == this.moment) {
				this.moment = 0;
				
				if(this.now == (this.map.size() - 1)) {
					this.now = 0;
				}else {
					this.now++;
				}
			}else {
				this.moment++;
			}
		}
	}
}