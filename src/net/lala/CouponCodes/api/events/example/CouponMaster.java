package net.lala.CouponCodes.api.events.example;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.coupon.CouponAddToDatabaseEvent;
import net.lala.CouponCodes.api.events.coupon.CouponCreateEvent;
import net.lala.CouponCodes.api.events.coupon.CouponExpireEvent;
import net.lala.CouponCodes.api.events.coupon.CouponListener;
import net.lala.CouponCodes.api.events.coupon.CouponRemoveFromDatabaseEvent;
import net.lala.CouponCodes.api.events.coupon.CouponTimeChangeEvent;

/**
 * CouponMaster.java - Example class of using couponcode's custom events
 * @author mike101102
 */
public class CouponMaster extends CouponListener {

	private CouponCodes plugin;
	
	public CouponMaster(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCouponCreate(CouponCreateEvent event) {
		plugin.debug("Coupon created: "+event.getCoupon().getName()+" Type: "+event.getCoupon().getType());
	}
	
	@Override
	public void onCouponAddToDatabase(CouponAddToDatabaseEvent event) {
		plugin.debug("Coupon added to database: "+event.getCoupon().getName()+" Type: "+event.getCoupon().getType());
	}
	
	@Override
	public void onCouponRemoveFromDatabase(CouponRemoveFromDatabaseEvent event) {
		plugin.debug("Coupon removed from database: "+event.getCoupon().getName()+" Type: "+event.getCoupon().getType());
	}
	
	@Override
	public void onCouponExpire(CouponExpireEvent event) {
		plugin.debug("Coupon "+event.getCoupon().getName()+" has expired! Type: "+event.getCoupon().getType());
	}
	
	@Override
	public void onCouponTimeChange(CouponTimeChangeEvent event) {
		plugin.debug("Coupon "+event.getCoupon().getName()+"'s time has been changed! It is now "+event.getCoupon().getTime()+"! Expired: "+event.getCoupon().isExpired()+" Type: "+event.getCoupon().getType());
	}
}
