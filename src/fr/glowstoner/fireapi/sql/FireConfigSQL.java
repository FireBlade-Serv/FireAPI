package fr.glowstoner.fireapi.sql;

import java.io.ObjectInputStream;

import fr.glowstoner.fireapi.crypt.FireCrypt;

public class FireConfigSQL {
		
	private String urlbase, host, database, user, password;
	
	public FireConfigSQL() {
		
	}
	
	public void load() throws Exception {
		System.out.println("[FireAPI] Chargement du fichier secret ...");

		ObjectInputStream in = new ObjectInputStream(super.getClass().
				getResourceAsStream("/fr/glowstoner/fireapi/sql/fireSQL.dat"));
		
		FireSqlLogin o = (FireSqlLogin) in.readObject();
		
		System.out.println("[FireAPI] Fichier secret importé ! Décryptage des logins SQL ...");
		
		FireCrypt crypt = new FireCrypt();
		
		crypt.setKey("jesuispasbelgeputain");
		
		this.urlbase = o.getUrlbase();
		this.host = crypt.decrypt(o.getHost());
		this.database = crypt.decrypt(o.getDatabase());
		this.user = crypt.decrypt(o.getUser());
		this.password = crypt.decrypt(o.getPassword());
		
		in.close();
		
		System.out.println("[FireAPI] Importé !");
	}
	
	public String getUrlbase() {
		return this.urlbase;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public String getDatabase() {
		return this.database;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getPassword() {
		return this.password;
	}
}
