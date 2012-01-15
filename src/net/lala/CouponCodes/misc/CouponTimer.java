package net.lala.CouponCodes.misc;

import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.api.events.coupon.CouponTimeChangeEvent;

public class CouponTimer implements Runnable {

	private CouponCodes plugin;
	private int z = 0;
	
	public CouponTimer(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		try {
			z = z+3;
			if (z == 30) {
				plugin.debug("CouponTimer.run() executed (this message appears every 30 seconds, thread is ran every 3 seconds!)");
				z = 0;
			}
			CouponManager cm = CouponCodes.getCouponManager();
			ArrayList<String> cl = cm.getCoupons();
			
			for (int i = 0; i < cl.size(); i++) {
				Coupon c = cm.getCoupon(cl.get(i));
				if (c.isExpired()) continue;
				CouponTimeChangeEvent ev = EventHandle.callCouponTimeChangeEvent(c);
				if (ev.isCancelled()) return;
				c.setTime(c.getTime()-1);
				cm.updateCoupon(c);
			}
		} catch (SQLException e) {}
	}
}
