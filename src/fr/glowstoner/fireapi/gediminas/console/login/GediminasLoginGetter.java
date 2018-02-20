package fr.glowstoner.fireapi.gediminas.console.login;

import java.io.ObjectInputStream;

import fr.glowstoner.fireapi.crypt.FireCrypt;

public class GediminasLoginGetter {
	
	private String key, password;
	
	public GediminasLoginGetter() {
		
	}
	
	public void load() throws Exception {
		System.out.println("[Gediminas] (FireAPI) Chargement du fichier secret ...");

		ObjectInputStream in = new ObjectInputStream(super.getClass().
				getResourceAsStream("/fr/glowstoner/fireapi/gediminas/console/login/gediminas.dat"));
		
		GediminasLogin o = (GediminasLogin) in.readObject();
		
		System.out.println("[Gediminas] (FireAPI) Fichier secret importé ! Décryptage des logins Gediminas ...");
		
		FireCrypt crypt = new FireCrypt();
		
		crypt.setKey("jesuispasbelgeputain");
		
		this.key = crypt.decrypt(o.getKey());
		this.password = crypt.decrypt(o.getPassword());
		
		in.close();
		
		System.out.println("[Gediminas] (FireAPI) Importé !");
	}

	public String getKey() {
		return key;
	}

	public String getPassword() {
		return password;
	}
}
