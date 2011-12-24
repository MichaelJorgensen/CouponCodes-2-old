package net.lala.CouponCodes.api.events.plugin;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;

/**
 * CouponCodesListener.java - Handles couponcode's custom events. Other classes can extend these events
 * @author mike101102
 */
public class CouponCodesListener extends CustomEventListener {

	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof CouponCodesCommandEvent) {
			this.onCouponCodesCommand((CouponCodesCommandEvent) event);
		}
	}
	
	/**
	 * Called when a command is passed through coupon codes, allowing for custom /c commands
	 * @param event
	 */
	public void onCouponCodesCommand(CouponCodesCommandEvent event) {}
}
