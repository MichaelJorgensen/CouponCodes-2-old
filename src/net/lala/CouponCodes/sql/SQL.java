package net.lala.CouponCodes.sql;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.Coupon;

public class SQL {

	private CouponCodes plugin;
	private DatabaseOptions dop;
	
	public SQL(CouponCodes plugin, DatabaseOptions dop){
		this.plugin = plugin;
	}
	
	public void send(String message){
		plugin.send(message);
	}
	
	public DatabaseOptions getDatabaseOptions(){
		return dop;
	}
	
	public boolean addCouponToDatabase(Coupon coupon){
		return false;
	}
}
