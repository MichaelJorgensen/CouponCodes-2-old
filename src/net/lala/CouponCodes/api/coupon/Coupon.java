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
	private int time;
	private boolean expired;
	private HashMap<String, Boolean> usedplayers = null;
	
	public Coupon(String name, int usetimes, int time, HashMap<String, Boolean> usedplayers) {
		this.name = name;
		this.usetimes = usetimes;
		this.time = time;
		this.usedplayers = usedplayers;
		this.expired = (usetimes <= 0 || time == 0);
		EventHandle.callCouponCreateEvent(this);
	}
	
	public boolean addToDatabase() throws SQLException {
		return CouponCodes.getCouponManager().addCouponToDatabase(this);
	}
	
	public boolean removeFromDatabase() throws SQLException {
		return CouponCodes.getCouponManager().removeCouponFromDatabase(this);
	}
	
	public boolean isInDatabase() throws SQLException {
		return CouponCodes.getCouponManager().couponExists(this);
	}
	
	public void updateWithDatabase() throws SQLException {
		CouponCodes.getCouponManager().updateCoupon(this);
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
		if (this.usetimes <= 0)
			this.setExpired(true);
	}
	
	public Integer getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
		if (this.time == 0)
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
	
	public boolean isExpired() {
		return expired;
	}
	
	public void setExpired(boolean expired) {
		this.expired = expired;
		if (expired) new CouponExpireEvent(this).call();
	}
}
