package org.mig.playerinfo.async;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;

import code.husky.mysql.MySQL;
/*
 * This class is solely to log the leaving transaction of a player into the server 
 * in an Async thread.
 */
public class LeaveTransaction extends BukkitRunnable{
	private String u;
	
	private MySQL db;
	
	/*
	 * Constructor that brings in the player's UUID and the MySQL database to log the transaction
	 */
	public LeaveTransaction(String uuid, MySQL dataBase) throws ClassNotFoundException, SQLException{
		u = uuid;
		db = dataBase;
		
		db.openConnection();
	}
	
	/*
	 * Runs the transaction in a seperate thread
	 */
	@Override
	public void run() {
		try {
			Statement statement;
			statement = this.db.getConnection().createStatement();
			statement.executeUpdate("UPDATE `transactions` SET `Leave`= NOW() WHERE `UUID`='" + u 
					+"' AND `Leave`='0';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}