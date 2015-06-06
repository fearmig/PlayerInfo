package org.mig.playerinfo.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;
import code.husky.mysql.MySQL;
/*
 * This class is solely to log the entry transaction of a player into the server 
 * in an Async thread.
 */
public class EntryTransaction extends BukkitRunnable{
	private String u;
	private String i;
	private String n;
	private String s;
	private String names;
	private String ips;
	
	private MySQL db;
	
	/*
	 * Constructor that takes in UUID, IP, Name, Server, and the MySql database
	 */
	public EntryTransaction(String uuid, String ip, String name, String server
						,MySQL dataBase) throws ClassNotFoundException, SQLException{
		u = uuid;
		i = ip;
		n = name;
		s = server;
		
		db = dataBase;
		if(!db.checkConnection())
			db.openConnection();
	}
	
	/*
	 * Run EntryTransaction in seperate thread.
	 */
	
	@Override
	public void run() {
		
		Statement statement;
		try {
			fixPrior();
			statement = this.db.getConnection().createStatement();
			statement.executeUpdate("INSERT INTO `transactions` (`UUID`,`Enter`,`Leave`,`Server`,`PlayerIP`)" + 
							" VALUES ('"+u+"', NOW(), '0','"+s+"','"+i+"');");
			enterPlayer();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * updates the player in the players table or inserts them if they don't exist
	 */
	private void enterPlayer() throws SQLException{
		Statement statement = null;
		if(playerExists()){
			if(!names.contains(n)){
				names = names + "%" + n;
				statement = this.db.getConnection().createStatement();
				statement.executeUpdate("UPDATE `players` SET `Names`='"+names+"' WHERE `UUID`='" + u +"';");
			}
			if(!ips.contains(i)){
				ips = ips + "%" + i;
				statement = this.db.getConnection().createStatement();
				statement.executeUpdate("UPDATE `players` SET `IPs`='"+ips+"' WHERE `UUID`='" + u +"';");
			}
		}
		else{
			statement = this.db.getConnection().createStatement();
			statement.executeUpdate("INSERT INTO `players` (`UUID`,`Names`,`IPs`,`OrigJoin`)" + 
					" VALUES ('"+u+"','"+n+"','"+i+"', NOW());");
		}
		statement.close();
	}
	
	/*
	 * return if a player exists and if they do log their name and ips
	 */
	private boolean playerExists() throws SQLException{
		Statement statement;
		statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM `players` WHERE `UUID`='" + u +"';");
		
		if(rs.first()){
			names = rs.getString("Names");
			ips = rs.getString("IPs");
			statement.close();
			return true;
		}
		else{
			statement.close();
			return false;
		}
	}
	
	
	/*
	 * Fix if there are previous sessions that did not log a exit time.
	 */
	private void fixPrior() throws SQLException{
		
		Statement statementA;
		statementA = this.db.getConnection().createStatement();
		ResultSet rs = statementA.executeQuery("SELECT * FROM `transactions` WHERE `UUID`='" + u 
				+"' AND `Leave`='0';");
		
		if(rs.first()){
			String temp = rs.getString("Enter");
			Statement statementB;
			statementB = this.db.getConnection().createStatement();
			statementB.executeUpdate("UPDATE `transactions` SET `Leave`= '"+ temp +"' WHERE `UUID`='" + u 
					+"' AND `Leave`='0';");
			statementB.close();
		}
		rs.close();
		statementA.close();
		
	}
	
}
