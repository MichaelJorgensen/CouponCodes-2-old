package net.lala.CouponCodes.api.events.database;

import java.sql.ResultSet;

import net.lala.CouponCodes.sql.options.DatabaseOptions;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * DatabaseQueryEvent.java - Extension of event used when the database is sent a query
 * @author mike101102
 */
@SuppressWarnings("serial")
public class DatabaseQueryEvent extends Event {

	private DatabaseOptions dop;
	private String query;
	private ResultSet rs;
	
	public DatabaseQueryEvent(DatabaseOptions dop, String query, ResultSet rs) {
		super("DatabaseQueryEvent");
		this.dop = dop;
		this.query = query;
		this.rs = rs;
	}
	
	public String getQuery() {
		return query;
	}
	
	public DatabaseOptions getDatabaseOptions() {
		return dop;
	}
	
	public ResultSet getResultSet() {
		return rs;
	}
	
	/**
	 * Calls the event
	 */
	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
