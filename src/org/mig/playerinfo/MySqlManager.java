package org.mig.playerinfo;

import java.sql.SQLException;
import java.sql.Statement;
import code.husky.mysql.MySQL;

/*
 * This class was made to handle the building of the database if it does not exist already and connecting
 * to the database.
 */
public class MySqlManager {
	private PlayerInfo main;
	private MySQL db;
	
	/*
	 * constructor
	 */
	public MySqlManager(){
		main = PlayerInfo.getMain();
	}
	
	/*
	 * Create two MySql tables if they do not already exist. One table will be called players and will contain
	 * 4 coloums: thier uuid, their name history, their ip history, and their original join date. The second
	 * table will be called transactions and will contain many transactions of the players UUID, their
	 * enter time, their leave time, the server they were in, and they IP they were using.
	 */
	public void setupDB() throws SQLException, ClassNotFoundException{
		
		this.db = new MySQL(main,main.getConfig().getString("MySqlHost"),main.getConfig().getString("MySqlPort")
				,main.getConfig().getString("MySqlDatabase"),main.getConfig().getString("MySqlUsername")
				,main.getConfig().getString("MySqlPassword"));
		this.db.openConnection();
		
		main.getLogger().info("Connected to MySQL database");
		Statement statement = this.db.getConnection().createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `players` (`UUID` varchar(50), `Names` TEXT, `IPs` TEXT, `OrigJoin` TEXT)");
		statement.close();
		main.getLogger().info("Created / Connected to players DB");
		
		Statement statementA = this.db.getConnection().createStatement();
		statementA.executeUpdate("CREATE TABLE IF NOT EXISTS `transactions` (`UUID` varchar(50), `Enter` TEXT, `Leave` TEXT, `Server` TEXT, `PlayerIP` varchar(50))");
		statementA.close();
		main.getLogger().info("Created / Connected to transactional DB");
	}
	
	/*
	 * Return the database that has been set up.
	 */
	public MySQL getDB(){
		return db;
	}
}