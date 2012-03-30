package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import net.lala.CouponCodes.CouponCodes;
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
	private long m_ts = 0;
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
		m_ts = rs.getLong("expire");
		m_active = rs.getInt("active");
	}
	
	public boolean addToDatabase() throws SQLException {
		m_id = CouponCodes.getCouponManager().addCouponToDatabase(this);
		return (m_id > 0) ? true : false;
	}
	
	public boolean removeFromDatabase() throws SQLException {
		return CouponCodes.getCouponManager().removeCouponFromDatabase(this);
	}
	
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
	
	public long getExpireTimestamp() { return m_ts; }
	
	public void setExpireTimestamp(long time) { m_ts = time; }
	
	public int getActive() { return m_active; }
	
	public void setActive(int active) { m_active = active; }
	
	public static Boolean createTables(SQL sql) {
		try {
			sql.createTable("CREATE TABLE IF NOT EXISTS codes (id INTEGER PRIMARY KEY AUTO_INCREMENT, code VARCHAR(24), effect INT, value INT, totaluses INT, expire BIGINT, active INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(16))");
			sql.createTable("CREATE TABLE IF NOT EXISTS uses (id INTEGER PRIMARY KEY AUTO_INCREMENT, user_id INT, code_id INT, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			sql.createTable("CREATE TABLE IF NOT EXISTS multi (id INTEGER PRIMARY KEY AUTO_INCREMENT, trigger_code_id INT, effect_code_id INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTO_INCREMENT, coupon_id INT, item_id INT, amount INT, damage INT, enchantment INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS ranks (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(16))");
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
	
	public static ArrayList<Coupon> getAllCoupons(SQL sql) throws SQLException {
		return getAllCoupons(sql, true, true, null);
	}
	
	public static ArrayList<Coupon> getAllCoupons(SQL sql, boolean showactive, boolean showinactive, String prefix) throws SQLException {
		ArrayList<Coupon> alc = new ArrayList<Coupon>();
		String s = "SELECT * FROM codes WHERE 1=1 ";
		if(showactive == true && showinactive == false)
			s += "AND active=1 ";
		if(showactive == false && showinactive == true)
			s += "AND active=0 ";
		if(prefix != null)
			s += "AND SUBSTR(code,1," + prefix.length() + ")='" + prefix + "'";
		ResultSet rs = sql.query(s);
		while(rs.next()) {
			Coupon c = new Coupon(rs);
			alc.add(c);
		}
		return alc;
	}

	public static void deleteAllCoupons(SQL sql) throws SQLException {
		sql.query("DELETE FROM codes");
		sql.query("DELETE FROM users");
		sql.query("DELETE FROM uses");
		sql.query("DELETE FROM multi");
		sql.query("DELETE FROM items");
		sql.query("DELETE FROM ranks");
	}
	
	public static void deleteCoupon(SQL sql, Coupon coupon) throws SQLException {
		// FIXME this should delete unused rows that it references in other tables 
		sql.query("DELETE FROM codes WHERE id=" + coupon.getID());
	}
	
	public static boolean isExpired(SQL sql, Coupon coupon) {
		long ctime = coupon.getExpireTimestamp();
		if(ctime == 0)
			return false;
		Date ndate = new Date();
		long ntime = ndate.getTime();
//		m_log.info("ctime: " + ctime + " ntime: " + ntime);       
		return (ctime <= ntime) ? true : false; 
	}
	
	public static boolean couponExists(SQL sql, Coupon coupon) throws SQLException {
		ResultSet rs = sql.query("SELECT COUNT(id) FROM codes WHERE id=" + coupon.getID());
		rs.next();
		int rc = rs.getInt(1);
		return (rc > 0) ? true : false;
	}
	
	public static Coupon findCoupon(SQL sql, String code) throws SQLException {
		Coupon c = null;
		String q = "SELECT * FROM codes WHERE code='" + code + "'";
//		m_log.info("findCoupon: " + q);
		ResultSet rs = sql.query(q);
		while(rs.next()) {
			c = new Coupon(rs);
//			String s = (c.getActive() == 1) ? "true" : "false";
//			m_log.info("cup: " + c.getID() + " act: " + s + " code: " + c.getCode());
		}
		return c;
	}
	
	public static int getTimesUsed(SQL sql, Coupon coupon) throws SQLException {
		ResultSet rs = sql.query("SELECT COUNT(id) FROM uses WHERE code_id=" + coupon.getID());
		rs.next();
		int count = rs.getInt(1);
		return count;
	}
	
	public static boolean alreadyUsed(SQL sql, String playername, Coupon coupon) throws SQLException {
		ResultSet rs = sql.query("SELECT COUNT(uses.id) FROM uses JOIN users ON uses.user_id=users.id WHERE users.name='" + playername + "' AND uses.code_id=" + coupon.getID() + "");
		rs.next();
		int rc = rs.getInt(1);
		return (rc > 0) ? true : false;
	}
}
