package net.lala.CouponCodes.api.events.example;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.plugin.CouponCodesCommandEvent;
import net.lala.CouponCodes.api.events.plugin.CouponCodesListener;

public class CouponCodesMaster extends CouponCodesListener {

	private CouponCodes plugin;
	
	public CouponCodesMaster(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCouponCodesCommand(CouponCodesCommandEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append("Command sent to coupon codes: "+event.getCommand().getName());
		for (int i = 0; i < event.getArgs().length; i++) {
			sb.append(" "+event.getArgs()[i]);
		}
		plugin.debug(sb.toString());
	}
}
