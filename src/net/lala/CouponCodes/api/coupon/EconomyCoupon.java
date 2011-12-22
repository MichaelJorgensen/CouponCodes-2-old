package net.lala.CouponCodes.api.coupon;

import java.util.ArrayList;

public class EconomyCoupon extends Coupon {

	private Integer money;
	
	public EconomyCoupon(String name, Integer usetimes, ArrayList<String> usedplayers, Integer money) {
		super(name, usetimes, usedplayers);
		this.money = money;
	}
	
	public Integer getMoney() {
		return money;
	}
	
	public void setMoney(Integer newmoney) {
		this.money = newmoney;
	}
}
