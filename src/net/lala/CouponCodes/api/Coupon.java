package net.lala.CouponCodes.api;

import java.sql.Array;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.coupon.CouponCreateEvent;
import net.lala.CouponCodes.misc.CouponType;

/**
 * Coupon.java - Stores all of the coupon's information
 * @author LaLa
 */
public class Coupon {
	
	// General variables
	private CouponType ct = CouponType.Unknown;
	private String name;
	private int usetimes;
	private Array usedplayers = null;
	
	// Item specific variables
	private Array ids = null;
	
	// Economy specific variables
	private int money = 0;
	
	/**
	 * Used for creating a coupon for ID redeeming
	 * @param name
	 * @param usetimes
	 * @param ids
	 * @param usedplayers
	 */
	public Coupon(String name, int usetimes, Array ids, Array usedplayers) {
		this.ct = CouponType.Item;
		this.name = name;
		this.usetimes = usetimes;
		this.ids = ids;
		this.usedplayers = usedplayers;
		CouponCreateEvent ev = new CouponCreateEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(ev);
	}
	
	/**
	 * Used for creating a coupon for economy use
	 * @param name
	 * @param usetimes
	 * @param usedplayers
	 * @param money
	 */
	public Coupon(String name, int usetimes, Array usedplayers, int money) {
		this.ct = CouponType.Economy;
		this.name = name;
		this.usetimes = usetimes;
		CouponCreateEvent ev = new CouponCreateEvent(this);
		Bukkit.getServer().getPluginManager().callEvent(ev);
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
	
	public CouponType getType() {
		return ct;
	}
	
	public void setCouponType(CouponType coupontype) {
		this.ct = coupontype;
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
	}
	
	public Array getIDs() {
		return ids;
	}
	
	public void setIDs(Array ids) {
		this.ids = ids;
	}
	
	public Array getUsedPlayers() {
		return usedplayers;
	}
	
	public void setUsedPlayers(Array usedplayers) {
		this.usedplayers = usedplayers;
	}
	
	public Integer getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
}
