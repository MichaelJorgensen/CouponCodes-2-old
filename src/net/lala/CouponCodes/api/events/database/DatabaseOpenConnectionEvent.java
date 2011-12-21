package net.lala.CouponCodes.api.events.database;

import java.sql.Connection;

import net.lala.CouponCodes.sql.DatabaseOptions;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@SuppressWarnings("serial")
public class DatabaseOpenConnectionEvent extends Event {

	private DatabaseOptions dop;
	private Connection con;
	private Boolean success = false;
	
	public DatabaseOpenConnectionEvent(Connection con, DatabaseOptions dop, Boolean success) {
		super("DatabaseOpenConnectionEvent");
		this.dop = dop;
		this.con = con;
		this.success = success;
	}
	
	public DatabaseOptions getDatabaseOptions() {
		return dop;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	/**
	 * Calls the event
	 */
	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
