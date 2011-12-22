package net.lala.CouponCodes.api.events.database;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

/**
 * DatabaseListener.java - Handles couponcode's custom events. Other classes can extend these events
 * @author mike101102
 */
public class DatabaseListener extends CustomEventListener {

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof DatabaseQueryEvent) {
			this.onDatabaseQuery((DatabaseQueryEvent) event);
		}
		else if (event instanceof DatabaseOpenConnectionEvent) {
			this.onDatabaseOpenConnection((DatabaseOpenConnectionEvent) event);
		}
		else if (event instanceof DatabaseCloseConnectionEvent) {
			this.onDatabaseCloseConnection((DatabaseCloseConnectionEvent) event);
		}
	}
	
	/**
	 * Called after a query is sent to the database
	 * @param event
	 */
	public void onDatabaseQuery(DatabaseQueryEvent event) {}
	
	/**
	 * Called after the connection is made to the database
	 * @param event
	 */
	public void onDatabaseOpenConnection(DatabaseOpenConnectionEvent event) {}
	
	/**
	 * Called after the connection is closed from the database
	 * @param event
	 */
	public void onDatabaseCloseConnection(DatabaseCloseConnectionEvent event) {}
}
