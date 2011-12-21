package net.lala.CouponCodes.api.events.database;

import java.sql.ResultSet;

import net.lala.CouponCodes.sql.DatabaseOptions;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

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
	
	protected void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
