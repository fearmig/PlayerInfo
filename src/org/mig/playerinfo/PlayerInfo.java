package org.mig.playerinfo;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;
/*
 * 
 * This Plugin is created to record basic information about players to allow server owners
 * to easly verify if a player has changed their name, or if they are using a second account
 * on the same IP. Also records in and out transactions from each server the plugin is installed.
 * When this plugin is in each server of a bungeecord network it is recommended to use the same
 * MySql table in order to combine all transactions into one table.
 * 
 * Authored: FearMig (Mike B.)
 * 
 */

public class PlayerInfo extends JavaPlugin{
	public static PlayerInfo main;
	public static MySqlManager sql;
	/*
	 * Method that invokes on enable of the plugin.
	 */
	public void onEnable(){
		//create the config file if it does not exist
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		//register the event listner
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		main = this;
		sql = new MySqlManager();
		
		//try to setup the database and if it fails disable the plugin.
		try {
			sql.setupDB();
		} catch (ClassNotFoundException | SQLException e) {
			onDisable();
			e.printStackTrace();
		}
		
		main = this;
	}
	/*
	 * Disables the plugin and cleans up any possiblities of memory leaks to be safe.
	 */
	public void onDisable(){
		main = null;
		sql = null;
	}
}
