package net.lala.CouponCodes.api.couponapi;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;


public interface CouponAPI {

	/**
	 * Add the coupon to the database, returning its success
	 * @param coupon
	 * @return true if coupon has been added
	 */
	public boolean addCouponToDatabase(Coupon coupon) throws SQLException;
	
	/**
	 * Removes the give coupon from the database, returning its success
	 * @param coupon
	 * @return true if coupon has been deleted
	 */
	public boolean removeCouponFromDatabase(Coupon coupon) throws SQLException;
	
	/**
	 * Creates a new item coupon, meant for item redeeming. Note: Uses SQL Array
	 * @param name
	 * @param usetimes
	 * @param ids
	 * @param usedplayers
	 * @return Coupon
	 */
	public Coupon createNewItemCoupon(String name, int usetimes, Array ids, Array usedplayers);
	
	/**
	 * Creates a new economy coupon, meant for redeeming money for economy plugins
	 * @param name
	 * @param usetimes
	 * @param usedplayers
	 * @param money
	 * @return Coupon
	 */
	public Coupon createNewEconomyCoupon(String name, int usetimes, Array usedplayers, int money);
	
	/**
	 * Checks if the coupon exists in the database
	 * @param coupon
	 * @return true if the coupon exists
	 */
	public boolean couponExists(Coupon coupon) throws SQLException;
	
	/**
	 * Returns all the coupon names in the database
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getCoupons() throws SQLException;
}
