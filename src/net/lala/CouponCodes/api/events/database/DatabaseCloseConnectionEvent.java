package net.lala.CouponCodes.api.events.database;

import java.sql.Connection;

import net.lala.CouponCodes.sql.DatabaseOptions;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

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
	
	protected void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
