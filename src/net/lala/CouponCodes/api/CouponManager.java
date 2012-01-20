package net.lala.CouponCodes.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.coupon.EconomyCoupon;
import net.lala.CouponCodes.api.coupon.ItemCoupon;
import net.lala.CouponCodes.api.coupon.RankCoupon;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.sql.SQL;
import net.lala.CouponCodes.sql.options.MySQLOptions;

public class CouponManager {
	
	private CouponCodes plugin;
	private SQL sql;
	
	public CouponManager(CouponCodes plugin, SQL sql) {
		this.plugin = plugin;
		this.sql = sql;
	}
	
	public boolean addCouponToDatabase(Coupon coupon) throws SQLException {		
		Connection con = sql.getConnection();
		if (coupon instanceof ItemCoupon) {
			ItemCoupon c = (ItemCoupon) coupon;
			PreparedStatement p = con.prepareStatement("INSERT INTO couponcodes VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			p.setString(1, c.getName());
			p.setString(2, c.getType());
			p.setInt(3, c.getUseTimes());
			p.setString(4, plugin.convertHashToString2(c.getUsedPlayers()));
			p.setString(5, plugin.convertHashToString(c.getIDs()));
			p.setInt(6, 0);
			p.setString(7, "");
			p.setInt(8, c.getTime());
			p.addBatch();
			con.setAutoCommit(false);
			p.executeBatch();
			con.setAutoCommit(true);
		}
		else if (coupon instanceof EconomyCoupon) {
			EconomyCoupon c = (EconomyCoupon) coupon;
			PreparedStatement p = con.prepareStatement("INSERT INTO couponcodes VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			p.setString(1, c.getName());
			p.setString(2, c.getType());
			p.setInt(3, c.getUseTimes());
			p.setString(4, plugin.convertHashToString2(c.getUsedPlayers()));
			p.setString(5, "");
			p.setInt(6, c.getMoney());
			p.setString(7, "");
			p.setInt(8, c.getTime());
			p.addBatch();
			con.setAutoCommit(false);
			p.executeBatch();
			con.setAutoCommit(true);
		}
		else if (coupon instanceof RankCoupon) {
			RankCoupon c = (RankCoupon) coupon;
			PreparedStatement p = con.prepareStatement("INSERT INTO couponcodes VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			p.setString(1, c.getName());
			p.setString(2, c.getType());
			p.setInt(3, c.getUseTimes());
			p.setString(4, plugin.convertHashToString2(c.getUsedPlayers()));
			p.setString(5, "");
			p.setInt(6, 0);
			p.setString(7, c.getGroup());
			p.setInt(8, c.getTime());
			p.addBatch();
			con.setAutoCommit(false);
			p.executeBatch();
			con.setAutoCommit(true);
		}
		EventHandle.callCouponAddToDatabaseEvent(coupon);
		return true;
	}
	
	public boolean removeCouponFromDatabase(Coupon coupon) throws SQLException {
		if (!couponExists(coupon)) return false;		
		sql.query("DELETE FROM couponcodes WHERE name='"+coupon.getName()+"'");
		EventHandle.callCouponRemoveFromDatabaseEvent(coupon);
		return true;
	}
	
	public boolean couponExists(Coupon coupon) throws SQLException {
		return getCoupons().contains(coupon.getName());
	}
	
	public boolean couponExists(String name) throws SQLException {
		return getCoupons().contains(name);
	}
	
	public ArrayList<String> getCoupons() throws SQLException {
		ArrayList<String> c = new ArrayList<String>();
		try {
			ResultSet rs = sql.query("SELECT name FROM couponcodes");
			if (rs == null) return null;
			while (rs.next())
				c.add(rs.getString(1));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public void updateCoupon(Coupon coupon) throws SQLException {
		if (coupon instanceof ItemCoupon) {
			ItemCoupon c = (ItemCoupon) coupon;
			sql.query("UPDATE couponcodes SET ctype='"+c.getType()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usetimes='"+c.getUseTimes()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usedplayers='"+plugin.convertHashToString2(c.getUsedPlayers())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET ids='"+plugin.convertHashToString(c.getIDs())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET timeuse='"+c.getTime()+"' WHERE name='"+c.getName()+"'");
		}
		else if (coupon instanceof EconomyCoupon) {
			EconomyCoupon c = (EconomyCoupon) coupon;
			sql.query("UPDATE couponcodes SET ctype='"+c.getType()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usetimes='"+c.getUseTimes()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usedplayers='"+plugin.convertHashToString2(c.getUsedPlayers())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET money='"+c.getMoney()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET timeuse='"+c.getTime()+"' WHERE name='"+c.getName()+"'");
		}
		else if (coupon instanceof RankCoupon) {
			RankCoupon c = (RankCoupon) coupon;
			sql.query("UPDATE couponcodes SET ctype='"+c.getType()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usetimes='"+c.getUseTimes()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usedplayers='"+plugin.convertHashToString2(c.getUsedPlayers())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET groupname='"+c.getGroup()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET timeuse='"+c.getTime()+"' WHERE name='"+c.getName()+"'");
		}
	}
	
	public void updateCouponTime(Coupon c) throws SQLException {
		sql.query("UPDATE couponcodes SET timeuse='"+c.getTime()+"' WHERE name='"+c.getName()+"'");
	}
	
	public Coupon getCoupon(String coupon) throws SQLException {
		if (!couponExists(coupon)) return null;
		ResultSet rs = sql.query("SELECT * FROM couponcodes WHERE name='"+coupon+"'");
		if (sql.getDatabaseOptions() instanceof MySQLOptions) rs.first();
		int usetimes = rs.getInt("usetimes");
		int time = rs.getInt("timeuse");
		HashMap<String, Boolean> usedplayers = plugin.convertStringToHash2(rs.getString("usedplayers"));
		
		if (rs.getString("ctype").equalsIgnoreCase("Item")) {
			return createNewItemCoupon(coupon, usetimes, time, plugin.convertStringToHash(rs.getString("ids")), usedplayers);
		}
		else if (rs.getString("ctype").equalsIgnoreCase("Economy")) {
			return createNewEconomyCoupon(coupon, usetimes, time, usedplayers, rs.getInt("money"));
		}
		else if (rs.getString("ctype").equalsIgnoreCase("Rank")) {
			return createNewRankCoupon(coupon, rs.getString("groupname"), usetimes, time, usedplayers);
		} else {
			return null;
		}
	}
	
	public Coupon getBasicCoupon(String coupon) throws SQLException {
		ResultSet rs = sql.query("SELECT * FROM couponcodes WHERE name='"+coupon+"'");
		if (rs == null) return null;
		if (sql.getDatabaseOptions() instanceof MySQLOptions) rs.first();
		int usetimes = rs.getInt("usetimes");
		int time = rs.getInt("timeuse");
		String type = rs.getString("ctype");
		
		if (type.equalsIgnoreCase("Item"))
			return createNewItemCoupon(coupon, usetimes, time, null, null);
		else if (type.equalsIgnoreCase("Economy"))
			return createNewEconomyCoupon(coupon, usetimes, time, null, 0);
		else if (type.equalsIgnoreCase("Rank"))
			return createNewRankCoupon(coupon, null, usetimes, time, null);
		else
			return null;
	}
	
	public ItemCoupon createNewItemCoupon(String name, int usetimes, int time, HashMap<Integer, Integer> ids, HashMap<String, Boolean> usedplayers) {
		return new ItemCoupon(name, usetimes, time, usedplayers, ids);
	}
	
	public EconomyCoupon createNewEconomyCoupon(String name, int usetimes, int time, HashMap<String, Boolean> usedplayers, int money) {
		return new EconomyCoupon(name, usetimes, time, usedplayers, money);
	}
	
	public RankCoupon createNewRankCoupon(String name, String group, int usetimes, int time, HashMap<String, Boolean> usedplayers) {
		return new RankCoupon(name, group, usetimes, time, usedplayers);
	}
}
