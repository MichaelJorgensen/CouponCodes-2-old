package net.lala.CouponCodes.sql;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.Coupon;

public class SQL {

	private CouponCodes plugin;
	
	public SQL(CouponCodes plugin){
		this.plugin = plugin;
	}
	
	public void send(String message){
		plugin.send(message);
	}
	
	public boolean addCouponToDatabase(Coupon coupon){
		return false;
	}
}
