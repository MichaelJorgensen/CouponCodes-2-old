package net.lala.CouponCodes.api.coupon;

import java.sql.Array;

public class ItemCoupon extends Coupon {

	private Array ids;
	
	public ItemCoupon(String name, Integer usetimes, Array usedplayers, Array ids) {
		super(name, usetimes, usedplayers);
		this.ids = ids;
	}
	
	public Array getIDs() {
		return ids;
	}
}
