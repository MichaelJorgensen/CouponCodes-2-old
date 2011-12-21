package net.lala.CouponCodes.api.events.coupon;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import net.lala.CouponCodes.api.coupon.Coupon;

@SuppressWarnings("serial")
public class CouponCreateEvent extends Event {

	private Coupon coupon;
	
	public CouponCreateEvent(Coupon coupon) {
		super("CouponCreateEvent");
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