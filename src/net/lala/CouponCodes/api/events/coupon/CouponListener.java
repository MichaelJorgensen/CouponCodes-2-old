package net.lala.CouponCodes.api.events.coupon;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

/**
 * CouponListener.java - Handles couponcode's custom events. Other classes can extend these events
 * @author mike101102
 */
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
		else if (event instanceof CouponExpireEvent) {
			this.onCouponExpire((CouponExpireEvent) event);
		}
		else if (event instanceof CouponTimeChangeEvent) {
			this.onCouponTimeChange((CouponTimeChangeEvent) event);
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
	
	/**
	 * Called after a coupon expires
	 * @param event
	 */
	public void onCouponExpire(CouponExpireEvent event) {}
	
	/**
	 * Called after the time is changed on a coupon
	 * @param event
	 */
	public void onCouponTimeChange(CouponTimeChangeEvent event) {}
}
