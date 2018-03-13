package fr.glowstoner.fireapi.sql;

import java.io.Serializable;

public class FireSqlLogin implements Serializable {

	private static final long serialVersionUID = -4394706191358275074L;

	private String urlbase = "jdbc:mysql://", host, database, user, password;
	
	public FireSqlLogin(String host, String database, String user, String password) {
		this.setDatabase(database);
		this.setUser(user);
		this.setHost(host);
		this.setUser(user);
		this.setPassword(password);
	}

	public String getUrlbase() {
		return urlbase;
	}

	public void setUrlbase(String urlbase) {
		this.urlbase = urlbase;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
