package net.lala.CouponCodes.api.events.coupon;

import net.lala.CouponCodes.api.Coupon;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@SuppressWarnings("serial")
public class CouponRemoveFromDatabaseEvent extends Event {

	private Coupon coupon;
	
	public CouponRemoveFromDatabaseEvent(Coupon coupon) {
		super("CouponRemoveFromDatabaseEvent");
		this.coupon = coupon;
	}
	
	public Coupon getCoupon() {
		return coupon;
	}
	
	protected void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
