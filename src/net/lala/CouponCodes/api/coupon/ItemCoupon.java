package net.lala.CouponCodes.api.coupon;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ItemCoupon.java - Provides an extension of a coupon for item use
 * @author mike101102
 */
public class ItemCoupon extends Coupon {

	private HashMap<Integer, Integer> ids;
	
	public ItemCoupon(String name, Integer usetimes, ArrayList<String> usedplayers, HashMap<Integer, Integer> ids) {
		super(name, usetimes, usedplayers);
		this.ids = ids;
	}
	
	/**
	 * Returns the list of item IDs and their amount
	 * Use 
	 * @return HashMap<Integer, Integer> the id list with their amount
	 */
	public HashMap<Integer, Integer> getIDs() {
		return ids;
	}
}
