package fr.glowstoner.fireapi.bungeecord.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.rank.Rank;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command{
	
	private final String pre = "§6[§eStaffChat§6]§r ";
	
	@Getter private Map<ProxiedPlayer, Boolean> players = new HashMap<>();
	
	private boolean consoleListen;
	private FireAPI api;
	
	public StaffChatCommand(FireAPI api, String name) {
		super(name, null, "sc", "fsc", "firesc");
		
		this.api = api;
	}
	
	//! = all en jeu
	//: = staff chat
	//@ = party chat
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer) sender;
			
			if(this.api.getRankSystem().hasRankAndSup(pp.getName(), Rank.GUIDE)) {
				if(this.players.containsKey(pp)) {
					pp.sendMessage(new TextComponent(this.pre+"§aBien, vous n'entendez plus les discutions du staff !"));
					
					this.players.remove(pp);
				}else {
					pp.sendMessage(new TextComponent(this.pre+"§aBien, vous entendez maintenant les discussions du staff !"));
					pp.sendMessage(new TextComponent(this.pre+"§oRappel : Utilisez ':' devant vos messages pour parler au reste du staff !"));
					
					this.players.put(pp, true);
				}
			}else {
				pp.sendMessage(new TextComponent(this.pre+"§cErreur vous n'avez pas la permission d'utiliser cette commande !"));
			}
		}else {
			if(this.consoleListen) {
				sender.sendMessage(new TextComponent(this.pre+"§aBien, vous n'entendez plus les discutions du staff !"));
				
				this.consoleListen = false;
			}else {
				sender.sendMessage(new TextComponent(this.pre+"§aBien, vous entendez maintenant les discussions du staff !"));
				
				this.consoleListen = true;
			}
		}
	}

	public boolean consoleCanListen() {
		return this.consoleListen;
	}
	
	public void send(ProxiedPlayer p, String message) {
		String pname = this.api.getChatUtils().getStringRank(p.getName()) + " " 
				+ this.api.getChatUtils().getRankColor(p.getName()) + p.getName();
		
		Iterator<ProxiedPlayer> it = this.players.keySet().iterator();
		
		while(it.hasNext()) {
			ProxiedPlayer next = it.next();
			
			if(this.players.get(next)) {
				next.sendMessage(new TextComponent(this.pre+pname+"§o : "+message));
			}
		}
	}
}
