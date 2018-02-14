package fr.glowstoner.fireapi.bungeecord.cmd;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.fireapi.bungeecord.BungeeMain;
import fr.glowstoner.fireapi.bungeecord.auth.FireAuth;
import fr.glowstoner.fireapi.bungeecord.events.LoginSuccessEvent;
import fr.glowstoner.fireapi.bungeecord.events.enums.LoginSourceType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LoginCmd extends Command {

	private final String pre = "§6[§eAuth§6]§r ";
	
	private FireAuth auth;
	private BungeeMain main;

	public LoginCmd(String name, FireAuth auth, BungeeMain main) {
		super(name);
		
		this.auth = auth;
		this.main = main;

	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer) sender;
			
			if(pp.getServer().getInfo().equals(this.main.getProxy().getServerInfo("login"))) {
				if(this.main.getSQL().getCryptPassword(pp.getName()).equals("default-not-set")) {
					pp.sendMessage(new TextComponent
							(this.pre+"§cVous n'avez pas de compte veuillez en créer un ! §c(/register)"));
				}else {
					if(args.length == 0) {
						pp.sendMessage(new TextComponent(this.pre+"§cUsage : /login <mot-de-passe>"));
					}else if(args.length == 1) {
						try {
							if(this.main.getSQL().getCryptPassword(pp.getName()).
									equals(this.auth.getEncryptedPassword(args[0]))) {
								
								pp.sendMessage(new TextComponent(this.pre+"§aVous vous êtes connecté avec succès !"
										+ " §aBon jeu "+pp.getName()+" !"));
								//mot de passe bon
								
								this.main.getProxy().getPluginManager().
									callEvent(new LoginSuccessEvent(pp, LoginSourceType.LOGIN_SUCCESSFUL));
							}else {
								pp.sendMessage(new TextComponent(this.pre+"§cMot de passe incorrect !"));
							}
						} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
								| NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}else {
			sender.sendMessage(new TextComponent(this.pre+"§cErreur, vous êtes la console c'est triste,"
					+ " vous ne pouvez executer cette commande, mais bon c'est la vie."));
		}
	}

}
