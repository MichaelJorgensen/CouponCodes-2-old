package net.lala.CouponCodes.api.events.coupon;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;


public class CouponListener extends CustomEventListener {

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof CouponCreateEvent) {
			this.onCouponCreate((CouponCreateEvent) event);
		}
		else if (event instanceof CouponAddToDatabaseEvent) {
			this.onCouponAddToDatabase((CouponAddToDatabaseEvent) event);
		}
		else if (event instanceof CouponRemoveFromDatabaseEvent) {
			this.onCouponRemoveFromDatabase((CouponRemoveFromDatabaseEvent) event);
		}
	}
	
	/**
	 * Called after a coupon is created
	 * This does not necessarily mean it's in the database
	 * @param event
	 */
	public void onCouponCreate(CouponCreateEvent event) {}
	
	/**
	 * Called after a coupon is added to the database
	 * @param event
	 */
	public void onCouponAddToDatabase(CouponAddToDatabaseEvent event) {}
	
	
	/**
	 * Called after a coupon is removed from the database
	 * @param event
	 */
	public void onCouponRemoveFromDatabase(CouponRemoveFromDatabaseEvent event) {}
}
