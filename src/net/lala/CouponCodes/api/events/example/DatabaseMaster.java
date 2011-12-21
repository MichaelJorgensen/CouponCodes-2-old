package net.lala.CouponCodes.api.events.example;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.database.DatabaseCloseConnectionEvent;
import net.lala.CouponCodes.api.events.database.DatabaseListener;
import net.lala.CouponCodes.api.events.database.DatabaseOpenConnectionEvent;
import net.lala.CouponCodes.api.events.database.DatabaseQueryEvent;

public class DatabaseMaster extends DatabaseListener {

	private CouponCodes plugin;
	
	public DatabaseMaster(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onDatabaseQuery(DatabaseQueryEvent event) {
		plugin.debug("Query sent to database: "+event.getQuery());
	}
	
	@Override
	public void onDatabaseOpenConnection(DatabaseOpenConnectionEvent event) {
		plugin.debug("Connection opened to database");
	}
	
	@Override
	public void onDatabaseCloseConnection(DatabaseCloseConnectionEvent event) {
		plugin.debug("Connection closed from database");
	}
}
