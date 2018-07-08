package fr.glowstoner.fireapi.sql;

import java.io.ObjectInputStream;

public class FireConfigSQL {
		
	private String urlbase, host, database, user, password;
	
	public FireConfigSQL() {
		
	}
	
	public void load() throws Exception {
		System.out.println("[FireAPI] Chargement du fichier secret ...");

		ObjectInputStream in = new ObjectInputStream(super.getClass().
				getResourceAsStream("/fr/glowstoner/fireapi/sql/fireSQL.dat"));
		
		FireSqlLogin o = (FireSqlLogin) in.readObject();
		
		System.out.println("[FireAPI] Fichier secret importé ! Récupération des logins SQL ...");
		
		this.urlbase = o.getUrlbase();
		this.host = o.getHost();
		this.database = o.getDatabase();
		this.user = o.getUser();
		this.password = o.getPassword();
		
		in.close();
		
		System.out.println("[FireAPI] Terminé !");
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
