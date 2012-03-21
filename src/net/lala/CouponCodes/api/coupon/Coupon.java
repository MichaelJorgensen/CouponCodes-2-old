package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.events.coupon.CouponExpireEvent;
import net.lala.CouponCodes.sql.SQL;

public class Coupon {
	
	public static final int ECONOMY	= 1;
	public static final int ITEMS	= 2;
	public static final int RANK	= 3;
	public static final int XP		= 4;
	public static final int MULTI	= 5;
	
	private int m_id = 0;
	private String m_code = "";
	private int m_effect = 0; 
	private int m_value = 0;
	private int m_totaluses = 0;
	private Timestamp m_ts = null;
	private int m_active = 1;

	public Coupon() {
	}
	
	public Coupon(ResultSet rs) throws SQLException {
		loadFrom(rs);
	}
	
	public void loadFrom(ResultSet rs) throws SQLException {
		m_id = rs.getInt("id");
		m_code = rs.getString("code");
		m_effect = rs.getInt("effect");
		m_value = rs.getInt("value");
		m_totaluses = rs.getInt("totaluses");
		m_ts = rs.getTimestamp("expire");
		m_active = rs.getInt("active");
	}
	
	public boolean addToDatabase() throws SQLException {
		m_id = CouponCodes.getCouponManager().addCouponToDatabase(this);
		return (m_id > 0) ? true : false;
	}
	
	public boolean removeFromDatabase() throws SQLException {
		return CouponCodes.getCouponManager().removeCouponFromDatabase(this);
	}
	
	public boolean isInDatabase() throws SQLException {
		return CouponCodes.getCouponManager().couponExists(this);
	}
	
/*	public void updateWithDatabase() throws SQLException {
		CouponCodes.getCouponManager().updateCoupon(this);
	}
	
	public void updateTimeWithDatabase() throws SQLException {
		CouponCodes.getCouponManager().updateCouponTime(this);
	}*/
	
	public int getID() { return m_id; }
	
	public void setID(int id) { m_id = id; }
	
	public String getCode() { return m_code; }
	
	public void setCode(String code) { m_code = code; }
	
	public int getEffect() { return m_effect;	}
	
	public void setEffect(int effect) { m_effect = effect; }
	
	public int getValue() { return m_value; }
	
	public void setValue(int value) { m_value = value; }
	
	public int getTotalUses() { return m_totaluses; }
	
	public void setUseTimes(int usetimes) { m_totaluses = usetimes; }
	
	public Timestamp getExpireTimestamp() { return m_ts; }
	
	public void setExpireTimestamp(Timestamp time) { m_ts = time; }
	
	public int getActive() { return m_active; }
	
	public void setActive(int active) { m_active = active; }
	
	public static Boolean createTables(SQL sql) {
		try {
			sql.createTable("CREATE TABLE IF NOT EXISTS codes (id INTEGER PRIMARY KEY AUTOINCREMENT, code VARCHAR(24), effect INT, value INT, totaluses INT, expire TIMESTAMP, active INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(16))");
			sql.createTable("CREATE TABLE IF NOT EXISTS uses (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INT, code_id INT, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			sql.createTable("CREATE TABLE IF NOT EXISTS multi (id INTEGER PRIMARY KEY AUTOINCREMENT, trigger_code_id INT, effect_code_id INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTOINCREMENT, coupon_id INT, item_id INT, amount INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS ranks (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(16))");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String effectText(int effect) {
		switch(effect) {
		case ECONOMY:
			return "Economy";
		case ITEMS:
			return "Items";
		case RANK:
			return "Rank";
		case XP:
			return "XP";
		case MULTI:
			return "Multi";
		default:
			return "";
		}
	}
}
