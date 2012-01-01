package net.lala.CouponCodes.api.coupon;

import java.sql.SQLException;
import java.util.HashMap;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.api.events.coupon.CouponExpireEvent;

/**
 * Coupon.java - Stores generic coupon information
 * @author mike101102
 */
public abstract class Coupon {
	
	private String name;
	private int usetimes;
	private boolean expired;
	private HashMap<String, Boolean> usedplayers = null;
	
	public Coupon(String name, int usetimes, HashMap<String, Boolean> usedplayers) {
		this.name = name;
		this.usetimes = usetimes;
		this.usedplayers = usedplayers;
		this.expired = false;
		EventHandle.callCouponCreateEvent(this);
	}
	
	public boolean addToDatabase() throws SQLException {
		return CouponCodes.getCouponAPI().addCouponToDatabase(this);
	}
	
	public boolean removeFromDatabase() throws SQLException {
		return CouponCodes.getCouponAPI().removeCouponFromDatabase(this);
	}
	
	public boolean isInDatabase() throws SQLException {
		return CouponCodes.getCouponAPI().couponExists(this);
	}
	
	public void updateWithDatabase() throws SQLException {
		CouponCodes.getCouponAPI().updateCoupon(this);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getUseTimes() {
		return usetimes;
	}
	
	public void setUseTimes(int usetimes) {
		this.usetimes = usetimes;
		if (usetimes <= 0)
			this.setExpired(true);
	}
	
	public HashMap<String, Boolean> getUsedPlayers() {
		return usedplayers;
	}
	
	public void setUsedPlayers(HashMap<String, Boolean> usedplayers) {
		this.usedplayers = usedplayers;
	}
	
	public String getType() {
		if (this instanceof ItemCoupon) return "Item";
		if (this instanceof EconomyCoupon) return "Economy";
		if (this instanceof RankCoupon) return "Rank";
		else
			return null;
	}
	
	public Boolean isExpired() {
		return expired;
	}
	
	public void setExpired(Boolean expired) {
		this.expired = expired;
		new CouponExpireEvent(this).call();
	}
}
