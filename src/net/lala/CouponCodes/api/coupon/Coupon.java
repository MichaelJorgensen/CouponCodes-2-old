package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.sql.SQL;

abstract public class Coupon {
	
	public static final int ECONOMY	= 1;
	public static final int ITEMS	= 2;
	public static final int RANK	= 3;
	public static final int XP		= 4;
	public static final int MULTI	= 5;
	public static final int WARP	= 6;
	public static final int BAD		= 7;
	
	protected CouponCodes m_plugin;
	protected SQL m_sql;

	private int m_id = 0;
	private String m_code = "";
	private int m_effect = 0; 
	private int m_value = 0;
	private int m_totaluses = 0;
	private long m_expire = 0;
	private int m_active = 1;

	protected Coupon(int id, String code, int effect, int value, int active, int totaluses, long expire) {
		this();
		m_id = id;
		m_code = code;
		m_effect = effect;
		m_value = value;
		m_totaluses = totaluses;
		m_expire = expire;
		m_active = active;
	}
	
	protected Coupon() {
		m_plugin = CouponCodes.getInstance();
		m_sql = m_plugin.getSQLAPI();
	}
	
	protected Coupon(ResultSet rs) throws SQLException {
		this();
		m_id = rs.getInt("id");
		m_code = rs.getString("code");
		m_effect = rs.getInt("effect");
		m_value = rs.getInt("value");
		m_totaluses = rs.getInt("totaluses");
		m_expire = rs.getLong("expire");
		m_active = rs.getInt("active");
	}
	
	abstract public String effectText();

	abstract public void doEffect(Player player);

	public int dbAdd() throws SQLException {
		SQL sql = m_plugin.getSQLAPI();
		if(couponExists(sql, getCode()) || getID() > 0)
			return 0;
		String q = "INSERT INTO codes (code, effect, value, totaluses, expire, active) VALUES ('" +
				getCode() + "', " + getEffect() + ", " + getValue() + ", " + getTotalUses() + ", " +
				getExpire() + ", " + getActive() + ")";
		sql.query(q);
		ResultSet rs = sql.query("SELECT LAST_INSERT_ID()");
		rs.next();
		int newid = rs.getInt(1);
		EventHandle.callCouponAddToDatabaseEvent(this);
		return newid;
	}

	abstract public void dbUpdate();

	abstract public void dbRemove();
	
	static public void parseAddArgs(CouponManager api, CommandSender sender, String[] args) {
		if(args.length > 2) {
			String e = args[1];
			if(e.equalsIgnoreCase("econ"))
				EconomyCoupon.parseAddArgs(api, sender, args);
			if(e.equalsIgnoreCase("item"))
				ItemCoupon.parseAddArgs(api, sender, args);
			if(e.equalsIgnoreCase("rank"))
				RankCoupon.parseAddArgs(api, sender, args);
			if(e.equalsIgnoreCase("xp"))
				XPCoupon.parseAddArgs(api, sender, args);
			if(e.equalsIgnoreCase("multi"))
				MultiCoupon.parseAddArgs(api, sender, args);
			if(e.equalsIgnoreCase("warp"))
				WarpCoupon.parseAddArgs(api, sender, args);
			if(e.equalsIgnoreCase("bad"))
				BadCoupon.parseAddArgs(api, sender, args);
		} else
			CommandUsage.sendHelp(sender);
	}

	static public Coupon loadFrom(ResultSet rs) throws SQLException {
		Coupon c = null;
		int effect = rs.getInt("effect");
		switch(effect) {
		case MULTI:
			c = (Coupon) new MultiCoupon(rs); break;
		case RANK:
			c = (Coupon) new RankCoupon(rs); break;
		case XP:
			c = (Coupon) new XPCoupon(rs); break;
		case ECONOMY:
			c = (Coupon) new EconomyCoupon(rs); break;
		case ITEMS:
			c = (Coupon) new ItemCoupon(rs); break;
		case WARP:
			c = (Coupon) new WarpCoupon(rs); break;
		case BAD:
			c = (Coupon) new BadCoupon(rs); break;
		}

		return c;
	}
	
	public int		getID()			{ return m_id; }
	public String	getCode()		{ return m_code; }
	public int		getEffect()		{ return m_effect;	}
	public int		getValue()		{ return m_value; }
	public int		getTotalUses()	{ return m_totaluses; }
	public long		getExpire()		{ return m_expire; }
	public int		getActive()		{ return m_active; }
	
	public static Boolean createTables(SQL sql) {
		try {
			sql.createTable("CREATE TABLE IF NOT EXISTS codes (id INTEGER PRIMARY KEY AUTO_INCREMENT, code VARCHAR(24), effect INT, value INT, totaluses INT, expire BIGINT, active INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(16))");
			sql.createTable("CREATE TABLE IF NOT EXISTS uses (id INTEGER PRIMARY KEY AUTO_INCREMENT, user_id INT, code_id INT, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			sql.createTable("CREATE TABLE IF NOT EXISTS multi (id INTEGER PRIMARY KEY AUTO_INCREMENT, trigger_code_id INT, effect_code_id INT, multigroup_id INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTO_INCREMENT, coupon_id INT, item_id INT, amount INT, damage INT, enchantment INT)");
			sql.createTable("CREATE TABLE IF NOT EXISTS ranks (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(16))");
			sql.createTable("CREATE TABLE IF NOT EXISTS warp (id INTEGER PRIMARY KEY AUTO_INCREMENT, x DOUBLE, y DOUBLE, z DOUBLE)");
			sql.createTable("CREATE TABLE IF NOT EXISTS attempt (id INTEGER PRIMARY KEY AUTO_INCREMENT, users_id INT, code VARCHAR(24), ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			sql.createTable("CREATE TABLE IF NOT EXISTS multigroup (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(16))");
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static int getCount(SQL sql) throws SQLException {
		return getCount(sql, 0);
	}
	public static int getCount(SQL sql, int effect) throws SQLException {
		int i = 0;
		String q = "SELECT COUNT(*) FROM codes";
		q += (effect > 0) ? " WHERE effect=" + effect : "";
		ResultSet rs = sql.query(q);
		if(rs.next())
			i = rs.getInt(1);
		return i;
	}
	
	public static ArrayList<Coupon> getAllCoupons(SQL sql) throws SQLException {
		return getAllCoupons(sql, true, true, null);
	}
	
	public static ArrayList<Coupon> getAllCoupons(SQL sql, int effect) throws SQLException {
		ArrayList<Coupon> alc = new ArrayList<Coupon>();
		String s = "SELECT * FROM codes WHERE effect=" + effect;
		ResultSet rs = sql.query(s);
		while(rs.next()) {
			Coupon c = Coupon.loadFrom(rs);
			alc.add(c);
		}
		return alc;
	}
	
	public static ArrayList<Coupon> getAllCoupons(SQL sql, boolean showactive, boolean showinactive, String prefix) throws SQLException {
		ArrayList<Coupon> alc = new ArrayList<Coupon>();
		String s = "SELECT * FROM codes WHERE 1=1 ";
		if(showactive == true && showinactive == false)
			s += "AND active=1 ";
		if(showactive == false && showinactive == true)
			s += "AND active=0 ";
		if(prefix != null)
			s += "AND SUBSTR(code, 1, " + prefix.length() + ")='" + prefix + "'";
		ResultSet rs = sql.query(s);
		while(rs.next()) {
			Coupon c = Coupon.loadFrom(rs);
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
	
	static public void deleteCoupon(SQL sql, Coupon coupon) throws SQLException {
		// FIXME this should delete unused rows that it references in other tables 
		sql.query("DELETE FROM codes WHERE id=" + coupon.getID());
	}
	
	static public boolean isExpired(SQL sql, Coupon coupon) {
		long ctime = coupon.getExpire();
		if(ctime == 0)
			return false;
		Date ndate = new Date();
		long ntime = ndate.getTime();
//		m_log.info("ctime: " + ctime + " ntime: " + ntime);       
		return (ctime <= ntime) ? true : false; 
	}
	
	static public boolean couponExists(SQL sql, String code) throws SQLException {
		ResultSet rs = sql.query("SELECT COUNT(id) FROM codes WHERE code='" + code + "'");
		rs.next();
		int rc = rs.getInt(1);
		return (rc > 0) ? true : false;
	}
	
	static public Coupon findCoupon(String code) {
		Coupon c = null;
		SQL sql = CouponCodes.getInstance().getSQLAPI();
		String q = "SELECT * FROM codes WHERE code='" + code + "'";
		try {
			ResultSet rs = sql.query(q);
			if(rs.next()) {
				c = Coupon.loadFrom(rs);
			}
		} catch(SQLException e) {
			return null;
		}
		return c;
	}
	
	static public Coupon findCoupon(int id) {
		Coupon c = null;
		SQL sql = CouponCodes.getInstance().getSQLAPI();
		String q = "SELECT * FROM codes WHERE id=" + id;
		try {
			ResultSet rs = sql.query(q);
			if(rs.next()) {
				c = Coupon.loadFrom(rs);
			}
		} catch(SQLException e) {
			return null;
		}
		return c;
	}
	
	static public int getTimesUsed(SQL sql, Coupon coupon) throws SQLException {
		ResultSet rs = sql.query("SELECT COUNT(id) FROM uses WHERE code_id=" + coupon.getID());
		rs.next();
		int count = rs.getInt(1);
		return count;
	}
	
	public boolean alreadyUsed(SQL sql, String playername) throws SQLException {
		ResultSet rs = sql.query("SELECT COUNT(uses.id) FROM uses JOIN users ON uses.user_id=users.id WHERE users.name='" + playername + "' AND uses.code_id=" + getID());
		rs.next();
		int rc = rs.getInt(1);
		if(getTotalUses() == 0)
			return false;
		return (rc > 0) ? true : false;
	}
	
	static public long parseExpire(String expire) {
		long time = 0;
		try {
			if(expire.charAt(0) == '+') {
				String val = expire.substring(1, expire.length());
				String[] vals = val.split(":");
				int amt = Integer.parseInt(vals[0]);
				String unit = vals[1];
				long now = (new Date()).getTime();
				long exptime = 0;
				if(unit.toLowerCase().contains("min") == true)
					exptime = now + (amt * 1000 * 60);
				else if(unit.toLowerCase().contains("hour") == true)
					exptime = now + (amt * 1000 * 60 * 60);
				else if(unit.toLowerCase().contains("day") == true)
					exptime = now + (amt * 1000 * 60 * 60 * 24);
				else if(unit.toLowerCase().contains("week") == true)
					exptime = now + (amt * 1000 * 60 * 60 * 24 * 7);
				time = exptime;
			} else
				time = Integer.parseInt(expire);
		} catch(NumberFormatException e) {
			return 0;
		}
		return time;
	}

}
