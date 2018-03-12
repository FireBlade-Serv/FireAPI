package fr.glowstoner.fireapi.bigbrother.console.login;

import java.io.ObjectInputStream;

import fr.glowstoner.fireapi.crypt.FireCrypt;

public class BigBrotherLoginGetter {
	
	private String key, password;
	
	public BigBrotherLoginGetter() {
		
	}
	
	public void load() throws Exception {
		System.out.println("[BigBrother] (FireAPI) Chargement du fichier secret ...");

		ObjectInputStream in = new ObjectInputStream(super.getClass().
				getResourceAsStream("/fr/glowstoner/fireapi/bigbrother/console/login/bigbrother.dat"));
		
		BigBrotherLogin o = (BigBrotherLogin) in.readObject();
		
		System.out.println("[BigBrother] (FireAPI) Fichier secret importé ! Décryptage des logins BigBrother ...");
		
		FireCrypt crypt = new FireCrypt();
		
		crypt.setKey("jesuispasbelgeputain");
		
		this.key = crypt.decrypt(o.getKey());
		this.password = crypt.decrypt(o.getPassword());
		
		in.close();
		
		System.out.println("[BigBrother] (FireAPI) Importé !");
	}

	public String getKey() {
		return key;
	}

	public String getPassword() {
		return password;
	}
}
