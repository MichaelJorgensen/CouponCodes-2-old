package net.lala.CouponCodes.api;

public interface CouponAPI {

	/**
	 * Add the coupon to the database, returning its success
	 * @param coupon
	 * @return true if coupon has been added
	 */
	public boolean addCouponToDatabase(Coupon coupon);
}
