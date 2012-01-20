package net.lala.CouponCodes.runnable;

import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.EventHandle;

public class CouponTimer implements Runnable {
	
	private CouponManager cm;
	
	public CouponTimer() {
		cm = CouponCodes.getCouponManager();
	}
	
	@Override
	public void run() {
		try {
			ArrayList<String> cl = cm.getCoupons();
			if (cl == null) return;
			
			for (String name : cl) {
				Coupon c = cm.getBasicCoupon(name);
				if (c == null) continue;
				if (c.isExpired() || c.getTime() == -1) continue;
				
				if (c.getTime()-2 < 0) c.setTime(0);
				else
					c.setTime(c.getTime()-2);
				cm.updateCouponTime(c);
				EventHandle.callCouponTimeChangeEvent(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
