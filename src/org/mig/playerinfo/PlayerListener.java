package org.mig.playerinfo;

import java.net.InetSocketAddress;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mig.playerinfo.async.EntryTransaction;
import org.mig.playerinfo.async.LeaveTransaction;


public class PlayerListener implements Listener{
	
	/*
	 * When a player joins try to put an entry into the transaction database of
	 * when the player joined, the players UUID, the player's ip, and the server.
	 */
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
		
		Player p = event.getPlayer();
		InetSocketAddress ia = p.getAddress();
		try {
			EntryTransaction et = new EntryTransaction(p.getUniqueId().toString(), ia.getHostString(), 
					p.getName(), PlayerInfo.getMain().getServer().getIp()+":"
					+ PlayerInfo.getMain().getServer().getPort(), PlayerInfo.getSql().getDB());
			
			et.runTaskAsynchronously(PlayerInfo.getMain());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * When a player leaves try to put an entry into the transaction database of
	 * when the player left the server.
	 */
	@EventHandler
    public void onLeave(PlayerQuitEvent event) {
		
		Player p = event.getPlayer();
		try {
			LeaveTransaction lt = new LeaveTransaction(p.getUniqueId().toString(), PlayerInfo.getSql().getDB());
			
			lt.runTaskAsynchronously(PlayerInfo.getMain());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * When a player gets kicked try to put an entry into the transaction database of
	 * when the player was removed from the server.
	 * 
	 * note: there will be no designation of the fact that a player was kicked instead
	 * of just leaving
	 */
	@EventHandler
    public void onKick(PlayerKickEvent event) {
		Player p = event.getPlayer();
		try {
			LeaveTransaction lt = new LeaveTransaction(p.getUniqueId().toString(), PlayerInfo.getSql().getDB());
			
			lt.runTaskAsynchronously(PlayerInfo.getMain());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
