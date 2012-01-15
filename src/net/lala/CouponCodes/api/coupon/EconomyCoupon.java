package net.lala.CouponCodes.api.coupon;

import java.util.HashMap;

/**
 * EconomyCoupon.java - Provides an extension of a coupon for economy use
 * @author mike101102
 */
public class EconomyCoupon extends Coupon {

	private Integer money;
	
	public EconomyCoupon(String name, Integer usetimes, Integer time, HashMap<String, Boolean> usedplayers, Integer money) {
		super(name, usetimes, time, usedplayers);
		this.money = money;
	}
	
	public Integer getMoney() {
		return money;
	}
	
	public void setMoney(Integer newmoney) {
		this.money = newmoney;
	}
}
