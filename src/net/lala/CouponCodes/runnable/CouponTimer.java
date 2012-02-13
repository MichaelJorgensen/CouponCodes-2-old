package net.lala.CouponCodes.runnable;

import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.EventHandle;

public class CouponTimer implements Runnable {
	
	private CouponManager cm;
	
	private ArrayList<String> cl;
	private Coupon c;
	
	public CouponTimer() {
		cm = CouponCodes.getCouponManager();
		cl = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		try {
			cl = cm.getCoupons();
			if (cl == null) return;
			
			for (String name : cl) {
				if (cm.getSQL().getConnection().isClosed()) return;
				c = cm.getBasicCoupon(name);
				if (c == null) continue;
				if (c.isExpired() || c.getTime() == -1) continue;
				
				if (c.getTime()-5 < 0) c.setTime(0);
				else
					c.setTime(c.getTime()-5);
				cm.updateCouponTime(c);
				EventHandle.callCouponTimeChangeEvent(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			
		}
	}
}
