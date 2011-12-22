package net.lala.CouponCodes.api.events.coupon;

import net.lala.CouponCodes.api.coupon.Coupon;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * CouponAddToDatabaseEvent.java - Extension of event used when a coupon is added to the database
 * @author mike101102
 */
@SuppressWarnings("serial")
public class CouponAddToDatabaseEvent extends Event {

	private Coupon coupon;
	
	public CouponAddToDatabaseEvent(Coupon coupon) {
		super("CouponAddToDatabaseEvent");
		this.coupon = coupon;
	}
	
	public Coupon getCoupon() {
		return coupon;
	}
	
	/**
	 * Calls the event
	 */
	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
