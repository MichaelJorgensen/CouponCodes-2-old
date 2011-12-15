package net.lala.CouponCodes.api;

import net.lala.CouponCodes.sql.DatabaseOptions;

/**
 * CouponCodesInterface.java - Provides an interface for the main class
 * @author LaLa
 */
public interface CouponCodesInterface {

	/**
	 * Prints out the message to the console in the format:
	 * [CouponCodes] +message
	 * @param message
	 */
	public void send(String message);
	
	/**
	 * Prints out the error message to the console in the format:
	 * [CouponCodes] [Error] +message
	 * @param message
	 */
	public void sendErr(String message);
	
	/**
	 * Returns the main SQLAPI Variable that this plugin uses
	 * @return SQL
	 */
	public SQLAPI getSQLAPI();
	
	/**
	 * Returns the database options that this plugin uses
	 * @return DatabaseOptions
	 */
	public DatabaseOptions getDatabaseOptions();
	
	/**
	 * Checks if Economy support is enabled
	 * @return true if economy support is valid
	 */
	public boolean isEconomyEnabled();
}
