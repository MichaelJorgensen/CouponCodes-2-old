package net.lala.CouponCodes.api.events.database;

import java.sql.Connection;

import net.lala.CouponCodes.sql.options.DatabaseOptions;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * DatabaseCloseConnectionEvent.java - Extension of event used when the connection is closed to the database
 * @author mike101102
 */
@SuppressWarnings("serial")
public class DatabaseCloseConnectionEvent extends Event {

	private Connection con;
	private DatabaseOptions dop;
	
	public DatabaseCloseConnectionEvent(Connection con, DatabaseOptions dop) {
		super("DatabaseCloseConnectionEvent");
		this.con = con;
		this.dop = dop;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public DatabaseOptions getDatabaseOptions() {
		return dop;
	}
	
	/**
	 * Calls the event
	 */
	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
