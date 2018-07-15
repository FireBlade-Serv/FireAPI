package fr.glowstoner.fireapi.bungeecord.auth;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.glowstoner.fireapi.bungeecord.BungeeMain;
import fr.glowstoner.fireapi.crypto.FireEncrypt;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class FireAuth {
	
	private final String pre = "§6[§eAuth§6]§r ";
	
	private BungeeMain main;
	private File file;
	private String key;
	
	public FireAuth(BungeeMain main) {
		this.main = main;
	}
	
	public void loadConfiguration() throws IOException {
		File file = new File(this.main.getDataFolder(), "auth.yml");
		
		this.file = file;
		
		if(!file.exists()) {
			file.createNewFile();
			
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
			config.set("crypt-pass-key", "stalineestsympa");
			this.key = config.getString("crypt-pass-key");
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, this.file);
		}else {
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
			this.key = config.getString("crypt-pass-key");
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, this.file);
		}
	}
	
	public String getClearPassword(String name) throws UnsupportedEncodingException,
		NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		FireEncrypt crypt = new FireEncrypt();
		crypt.setKey(this.key);
		return crypt.decrypt(this.main.getSQL().getCryptPassword(name));
	}
	
	public void registerPassword(String name, String clearpassword) throws UnsupportedEncodingException,
		NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		FireEncrypt crypt = new FireEncrypt();
		crypt.setKey(this.key);
		this.main.getSQL().setCryptPassword(name, crypt.encrypt(clearpassword));
	}
	
	public String getEncryptedPassword(String clearpassword) throws UnsupportedEncodingException,
		NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		FireEncrypt crypt = new FireEncrypt();
		crypt.setKey(this.key);
		return crypt.encrypt(clearpassword);
	}

	public String getRegisterMessage() {
		return this.pre+"§cVous devez vous inscrire ! /register <mot-de-passe> §c<même-mot-de-passe>";
	}
	
	public Title getRegisterTitle() {
		Title title = this.main.getProxy().createTitle();
		title.title(new TextComponent("§cVous devez vous inscrire !"));
		title.subTitle(new TextComponent("§c/register <motdepasse> <motdepasse>"));
		
		return title;
	}
	
	public String getLoginMessage() {
		return this.pre+"§cVous devez vous connecter ! /login <mot-de-passe>";
	}
	
	public Title getLoginTitle() {
		Title title = this.main.getProxy().createTitle();
		title.title(new TextComponent("§cVous devez vous connecter !"));
		title.subTitle(new TextComponent("§c/login <motdepasse>"));
		
		return title;
	}
	
	public String getSecurityKey() {
		return this.key;
	}
}