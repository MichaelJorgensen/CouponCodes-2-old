package net.lala.CouponCodes.api;

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
}
