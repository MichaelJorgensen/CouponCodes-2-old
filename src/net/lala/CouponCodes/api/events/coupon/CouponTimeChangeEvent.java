package net.lala.CouponCodes.api.events.coupon;

import net.lala.CouponCodes.api.coupon.Coupon;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * CouponTimeChangeEvent.java - Extension of event used when a coupon's time is changed
 * @author mike101102
 */
@SuppressWarnings("serial")
public class CouponTimeChangeEvent extends Event implements Cancellable {

	private Coupon coupon;
	private boolean cancel;
	
	public CouponTimeChangeEvent(Coupon coupon) {
		super("CouponTimeChangeEvent");
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

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancel = arg0;
	}
}
