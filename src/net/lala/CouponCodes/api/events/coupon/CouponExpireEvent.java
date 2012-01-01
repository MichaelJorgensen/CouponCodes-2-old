package net.lala.CouponCodes.api.events.coupon;

import net.lala.CouponCodes.api.coupon.Coupon;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * CouponExpireEvent.java - Extension of event used when a coupon expires
 * @author mike101102
 */
@SuppressWarnings("serial")
public class CouponExpireEvent extends Event {

	private Coupon coupon;
	
	public CouponExpireEvent(Coupon coupon) {
		super("CouponExpireEvent");
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
