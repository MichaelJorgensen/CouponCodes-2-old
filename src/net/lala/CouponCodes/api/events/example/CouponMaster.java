package net.lala.CouponCodes.api.events.example;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.coupon.CouponAddToDatabaseEvent;
import net.lala.CouponCodes.api.events.coupon.CouponCreateEvent;
import net.lala.CouponCodes.api.events.coupon.CouponListener;
import net.lala.CouponCodes.api.events.coupon.CouponRemoveFromDatabaseEvent;

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
}
