package net.lala.CouponCodes.api;

import net.lala.CouponCodes.CouponCodes;

/**
 * CouponManager.java - Allows other plugins to interact with coupons
 * @author LaLa
 */
public class CouponManager implements CouponAPI{

	private CouponCodes plugin;
	
	public CouponManager(CouponCodes plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean addCouponToDatabase(Coupon coupon) {
		return false;
	}
}
