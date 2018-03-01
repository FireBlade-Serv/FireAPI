package fr.glowstoner.fireapi.bungeecord.commands;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.fireapi.FireAPI;
import fr.glowstoner.fireapi.bungeecord.BungeeMain;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.events.LoginSuccessEvent;
import fr.glowstoner.fireapi.bungeecord.events.enums.LoginSourceType;
import fr.glowstoner.fireapi.player.FirePlayer;
import fr.glowstoner.fireapi.rank.Rank;
import fr.glowstoner.fireapi.sql.FireSQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RegisterCommand extends Command {
	
	private final String pre = "§6[§eAuth§6]§r ";
	
	private BungeeMain instance;
	private FireAuth auth;
	private FireSQL sql;

	public RegisterCommand(FireAPI api, String name) {
		super(name);
		
		this.instance = (BungeeMain) api.getBungeePlugin();
		this.auth = api.getAuthentification();
		this.sql = api.getSQL();
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer) sender;
			FirePlayer fp = new FirePlayer(pp, this.instance);
			
			if(pp.getServer().getInfo().equals(this.instance.getProxy().getServerInfo("login"))) {
				if(this.sql.getCryptPassword(pp.getName()).equals("§default-not-set")) {
					//not registered
						
					if(args.length == 0) {
						pp.sendMessage(new TextComponent(this.pre+"§cUsage : /register <mot-de-passe> <même-mot-de-passe>"));
					}else if(args.length == 1) {
						pp.sendMessage(new TextComponent(this.pre+"§cVeuillez confirmer votre mot de passe !"));
						pp.sendMessage(new TextComponent(this.pre+"§cUsage : /register <mot-de-passe> <même-mot-de-passe>"));
					}else if(args.length == 2) {
						if(args[0].equals(args[1])) {
							try {
								auth.registerPassword(pp.getName(), args[0]);
								
								pp.sendMessage(new TextComponent(this.pre+
										"§aVous vous êtes inscrit avec succès ! Bon jeu "+pp.getName()+" !"));
								
								//register good
								
								this.instance.getProxy().getPluginManager().
									callEvent(new LoginSuccessEvent(pp, LoginSourceType.REGISTRATION_SUCCESSFULL));
							} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
									| NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
								pp.disconnect(new TextComponent("§cUne erreur à été détectée !\n§cVeuillez réssayer plus tard !"));
								
								e.printStackTrace();
							}
							
						}else {
							pp.sendMessage(new TextComponent(this.pre+"§cErreur les deux entrées ne sont pas identiques !"));
						}
					}
				}else {
					//registered
					
					pp.sendMessage(new TextComponent(this.pre+"§cVous êtes déjà inscrit utilisez "
							+ "/login <mot-de-passe> §cpour vous connecter"));
				}
			}else {
				if(fp.hasRankAndSup(Rank.MANAGER_IG)) {
					if(args.length == 0) {
						pp.sendMessage(new TextComponent(this.pre+"§cUsage : /register unregister <pseudo>"));
					}else if(args.length == 1) {
						pp.sendMessage(new TextComponent(this.pre+"§cUsage : /register unregister <pseudo>"));
					}else if(args.length == 2) {
						if(args[0].equalsIgnoreCase("unregister")) {
							if(this.instance.getSQL().hasFireAccount(args[1])) {
								if(args[1].equalsIgnoreCase("CONSOLE")) {
									pp.sendMessage(new TextComponent(this.pre+"§cErreur, vous ne pouvez pas désinscrire la console !"));
									
									return;
								}
								
								this.instance.getSQL().setCryptPassword(args[1], "§default-not-set");
								
								pp.sendMessage(new TextComponent(this.pre+"§aLe joueur \""+args[1]+"\" est maintenant plus inscrit !"));
							}else {
								pp.sendMessage(new TextComponent(this.pre+"§cErreur, le joueur spécifié ne s'est jamais connecté"));
							}
						}else {
							pp.sendMessage(new TextComponent(this.pre+"§cUsage : /register unregister <pseudo>"));
						}
					}else {
						pp.sendMessage(new TextComponent(this.pre+"§cUsage : /register unregister <pseudo>"));
					}
				}
			}
		}else {
			sender.sendMessage(new TextComponent(this.pre+"§cErreur, vous êtes la console c'est triste,"
					+ " vous ne pouvez executer cette commande, mais bon c'est la vie."));
		}
	}

}
