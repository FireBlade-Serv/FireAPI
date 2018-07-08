package fr.glowstoner.fireapi.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import fr.glowstoner.fireapi.rank.Rank;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FireSQL extends TimerTask{

	private Connection connection;
	private String urlbase,host,database,user,pass;
	
	//connect other db
	public FireSQL(String urlbase, String host, String database, String user, String pass) {
		this.urlbase = urlbase;
		this.host = host;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}
	
	//connect fireblade's db
	public FireSQL() {
		FireConfigSQL conf = new FireConfigSQL();
		
		try {
			conf.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.urlbase = conf.getUrlbase();
		this.host = conf.getHost();
		this.database = conf.getDatabase();
		this.user = conf.getUser();
		this.pass = conf.getPassword();
	}
	
	public void connection(){
		try {
			connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void disconnect(){
		if(!isConnected()){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startSqlRefreshScheduler(long seconds) {
		Timer t = new Timer();
		t.scheduleAtFixedRate(this, 0L, (seconds * 1000));
	}
	
	public void refresh() {
		disconnect();
		connection();
	}
	
	public boolean isConnected(){
		return connection != null;
	}
	
	public boolean hasFireAccount(String name){
		
		try {
			PreparedStatement q = connection.prepareStatement("SELECT name FROM FireAPI WHERE name = ?");
			q.setString(1, name);
			ResultSet r = q.executeQuery();
			boolean hasAccount = r.next();
			q.close();
			
			return hasAccount;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean hasUltraAccount(String name){
		
		try {
			PreparedStatement q = connection.prepareStatement("SELECT pseudo FROM users WHERE pseudo = ?");
			q.setString(1, name);
			ResultSet r = q.executeQuery();
			boolean hasAccount = r.next();
			q.close();
			
			return hasAccount;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void createFireAccount(ProxiedPlayer p){
		if(!hasFireAccount(p.getName())){
			
			try {
				PreparedStatement q = connection.prepareStatement
						("INSERT INTO FireAPI(name, ip, coins, rank, password) VALUES (?, ?, ?, ?, ?)");
				
				q.setString(1, p.getName());
				q.setString(2, p.getAddress().getAddress().getHostAddress());
				q.setInt(3, 0);
				q.setString(4, Rank.MEMBRE.name());
				q.setString(5, "§default-not-set");
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void createFireAccount(Player p){
		if(!hasFireAccount(p.getName())){
			
			try {
				PreparedStatement q = connection.prepareStatement
						("INSERT INTO FireAPI(name, ip, coins, rank) VALUES (?, ?, ?, ?)");
				
				q.setString(1, p.getName());
				q.setString(2, p.getAddress().getHostName());
				q.setInt(3, 0);
				q.setString(4, Rank.MEMBRE.name());
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public int getFireMoney(String name){
		
		try {
			PreparedStatement q = connection.prepareStatement("SELECT coins FROM FireAPI WHERE name = ?");
			q.setString(1, name);
			
			int coins = 0;
			ResultSet rs = q.executeQuery();
			
			while(rs.next()){
				coins = rs.getInt("coins");
			}
			
			q.close();
			
			
			return coins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public int getUltraMoney(String name){
		
		try {
			PreparedStatement q = connection.prepareStatement("SELECT money FROM users WHERE pseudo = ?");
			q.setString(1, name);
			
			int coins = 0;
			ResultSet rs = q.executeQuery();
			
			while(rs.next()){
				coins = rs.getInt("money");
			}
			
			q.close();
			
			
			return coins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public void addFireCoins(String name, int coins){
		
		int pointsInt = getFireMoney(name);
		int NewPointsInt = pointsInt + coins;
		
		
		try {
			PreparedStatement rs = connection.prepareStatement("UPDATE FireAPI SET coins = ? WHERE name = ?");
			
			rs.setInt(1, NewPointsInt);
			rs.setString(2, name);
			
			rs.executeUpdate();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getFFAKills(String name) {
		try {
			PreparedStatement rs = connection.prepareStatement("SELECT kills FROM FireFFA WHERE name = ?");
			rs.setString(1, name);
			
			int kills = 0;
			
			ResultSet set = rs.executeQuery();
			
			while(set.next()) {
				kills = set.getInt("kills");
			}
			
			set.close();
			
			return kills;
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return -1;
	}
	
	public String getRank(String name) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT rank FROM FireAPI WHERE name = ?");
			ps.setString(1, name);
			
			String rank = null;
			
			ResultSet set = ps.executeQuery();
			
			while(set.next()) {
				rank = set.getString("rank");
			}
			
			set.close();
			
			return rank;
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public void setRank(String name, Rank rank) {
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE FireAPI SET rank = ? WHERE name = ?");
			ps.setString(1, rank.name());
			ps.setString(2, name);
			
			ps.executeUpdate();
			ps.close();
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getCryptPassword(String name) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT password FROM FireAPI WHERE name = ?");
			ps.setString(1, name);
			
			String pass = null;
			
			ResultSet set = ps.executeQuery();
			
			while(set.next()) {
				pass = set.getString("password");
			}
			
			return pass;
		}catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public void setCryptPassword(String name, String cryptpassword) {
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE FireAPI SET password = ? WHERE name = ?");
			ps.setString(1, cryptpassword);
			ps.setString(2, name);
			
			ps.executeUpdate();
			ps.close();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void putConsole() {
		if(!hasFireAccount("CONSOLE")){
			
			try {
				PreparedStatement q = connection.prepareStatement
						("INSERT INTO FireAPI(name, ip, coins, rank, password) VALUES (?, ?, ?, ?, ?)");
				
				q.setString(1, "CONSOLE");
				q.setString(2, "~");
				q.setInt(3, 666);
				q.setString(4, Rank.CONSOLE.name());
				q.setString(5, "~");
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void run() {
		System.out.println("[FireAPI] Processus de refresh sql ...");
		refresh();
	}
}