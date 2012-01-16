package net.lala.CouponCodes.runnable;

import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.api.events.coupon.CouponTimeChangeEvent;

public class CouponTimer implements Runnable {
	
	private CouponManager cm;
	
	public CouponTimer() {
		cm = CouponCodes.getCouponManager();
	}
	
	@Override
	public void run() {
		try {
			ArrayList<String> cl = cm.getCoupons();
			
			for (String name : cl) {
				Coupon c = cm.getCoupon(name);
				if (c.isExpired() || c.getTime() == -1) continue;
				CouponTimeChangeEvent ev = EventHandle.callCouponTimeChangeEvent(c);
				if (ev.isCancelled()) return;
				
				if (c.getTime()-2 < 0) c.setTime(0);
				else
					c.setTime(c.getTime()-2);
				cm.updateCoupon(c);
			}
		} catch (SQLException e) {}
	}
}
