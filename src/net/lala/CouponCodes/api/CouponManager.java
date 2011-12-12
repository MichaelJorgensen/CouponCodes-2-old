package net.lala.CouponCodes.api;

import net.lala.CouponCodes.CouponCodes;

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
