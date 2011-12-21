package net.lala.CouponCodes.api;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;



public interface CouponAPI {

	/**
	 * Add the coupon to the database, returning its success
	 * @param coupon
	 * @return true if coupon has been added
	 * @throws SQLException
	 */
	public boolean addCouponToDatabase(Coupon coupon) throws SQLException;
	
	/**
	 * Removes the give coupon from the database, returning its success
	 * @param coupon
	 * @return true if coupon has been deleted
	 * @throws SQLException
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
	 * @throws SQLException
	 */
	public boolean couponExists(Coupon coupon) throws SQLException;
	
	/**
	 * Checks if the coupon name exists in the database
	 * @param name
	 * @return true if coupon exists
	 * @throws SQLException
	 */
	public boolean couponExists(String name) throws SQLException;
	/**
	 * Returns all the coupon names in the database
	 * @return ArrayList<String>
	 * @throws SQLException
	 */
	public ArrayList<String> getCoupons() throws SQLException;
}
