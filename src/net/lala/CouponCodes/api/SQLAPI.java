package net.lala.CouponCodes.api;

import net.lala.CouponCodes.CouponCodes;

/**
 * SQLAPI.java - Provides abstract handling for subclasses
 * @author Owner
 *
 */
public abstract class SQLAPI {

	private CouponCodes plugin;
	
	public SQLAPI(CouponCodes plugin){
		this.plugin = plugin;
	}
	
	public void send(String message){
		plugin.send(message);
	}
	
	public void sendErr(String message){
		plugin.sendErr(message);
	}
}
