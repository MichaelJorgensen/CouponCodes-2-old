package net.lala.CouponCodes.api.events.coupon;

import net.lala.CouponCodes.api.coupon.Coupon;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("serial")
public class CouponRemoveFromDatabaseEvent extends Event {

	private static final HandlerList h = new HandlerList();
	
	private Coupon coupon;
	
	public CouponRemoveFromDatabaseEvent(Coupon coupon) {
		this.coupon = coupon;
	}
	
	public Coupon getCoupon() {
		return coupon;
	}
	
	public HandlerList getHandlers() {
		return h;
	}
	
	public static HandlerList getHandlerList() {
		return h;
	}
	
	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
