package net.lala.CouponCodes.misc;

/**
 * CouponType.java - Enum for identifying coupon types.
 * @author LaLa
 */
public enum CouponType {
	Item, Economy, Unknown;
	
	public String value() {
		switch (this){
		case Item: return "Item";
		case Economy: return "Economy";
		case Unknown: return "Unknown";
		default:
			return null;
		}
	}
}
