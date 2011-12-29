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

/**
 * CouponManager.java - Allows other plugins to interact with coupons
 * @author mike101102
 */
public class CouponManager implements CouponAPI {
	
	private CouponCodes plugin;
	private SQLAPI sql;
	
	public CouponManager(CouponCodes plugin, SQLAPI sql) {
		this.plugin = plugin;
		this.sql = sql;
	}
	
	public CouponManager(SQLAPI sql) {
		this.sql = sql;
	}
	
	@Override
	public boolean addCouponToDatabase(Coupon coupon) throws SQLException {		
		Connection con = sql.getConnection();
		if (coupon instanceof ItemCoupon) {
			ItemCoupon c = (ItemCoupon) coupon;
			PreparedStatement p = con.prepareStatement("INSERT INTO couponcodes VALUES(?, ?, ?, ?, ?, ?)");
			p.setString(1, c.getName());
			p.setString(2, c.getType());
			p.setInt(3, c.getUseTimes());
			p.setString(4, plugin.convertHashToString2(c.getUsedPlayers()));
			p.setString(5, plugin.convertHashToString(c.getIDs()));
			p.setInt(6, 0);
			p.setString(7, "");
			p.addBatch();
			con.setAutoCommit(false);
			p.executeBatch();
			con.setAutoCommit(true);
		}
		else if (coupon instanceof EconomyCoupon) {
			EconomyCoupon c = (EconomyCoupon) coupon;
			PreparedStatement p = con.prepareStatement("INSERT INTO couponcodes VALUES(?, ?, ?, ?, ?, ?)");
			p.setString(1, c.getName());
			p.setString(2, c.getType());
			p.setInt(3, c.getUseTimes());
			p.setString(4, plugin.convertHashToString2(c.getUsedPlayers()));
			p.setString(5, "");
			p.setInt(6, c.getMoney());
			p.setString(7, "");
			p.addBatch();
			con.setAutoCommit(false);
			p.executeBatch();
			con.setAutoCommit(true);
		}
		else if (coupon instanceof RankCoupon) {
			RankCoupon c = (RankCoupon) coupon;
			PreparedStatement p = con.prepareStatement("INSERT INTO couponcodes VALUES(?, ?, ?, ?, ?, ?)");
			p.setString(1, c.getName());
			p.setString(2, c.getType());
			p.setInt(3, c.getUseTimes());
			p.setString(4, plugin.convertHashToString2(c.getUsedPlayers()));
			p.setString(5, "");
			p.setInt(6, 0);
			p.setString(7, c.getGroup());
			p.addBatch();
			con.setAutoCommit(false);
			p.executeBatch();
			con.setAutoCommit(true);
		}
		EventHandle.callCouponAddToDatabaseEvent(coupon);
		return true;
	}
	
	@Override
	public boolean removeCouponFromDatabase(Coupon coupon) throws SQLException {
		if (!couponExists(coupon)) return false;		
		sql.query("DELETE FROM couponcodes WHERE name='"+coupon.getName()+"'");
		EventHandle.callCouponRemoveFromDatabaseEvent(coupon);
		return true;
	}
	
	@Override
	public boolean couponExists(Coupon coupon) throws SQLException {
		return getCoupons().contains(coupon.getName());
	}
	
	@Override
	public boolean couponExists(String name) throws SQLException {
		return getCoupons().contains(name);
	}
	
	@Override
	public ArrayList<String> getCoupons() throws SQLException {
		ArrayList<String> c = new ArrayList<String>();
		try {
			ResultSet rs = sql.query("SELECT name FROM couponcodes");
			if (rs.equals(null)) return null;
			while (rs.next())
				c.add(rs.getString(1));
			return c;
		} catch (NullPointerException e) {
			return c;
		}
	}
	
	@Override
	public void updateCoupon(Coupon coupon) throws SQLException {
		if (coupon instanceof ItemCoupon) {
			ItemCoupon c = (ItemCoupon) coupon;
			sql.query("UPDATE couponcodes SET ctype='"+c.getType()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usetimes='"+c.getUseTimes()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usedplayers='"+plugin.convertHashToString2(c.getUsedPlayers())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET ids='"+plugin.convertHashToString(c.getIDs())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET money='"+0+"' WHERE name='"+c.getName()+"'");
		}
		else if (coupon instanceof EconomyCoupon) {
			EconomyCoupon c = (EconomyCoupon) coupon;
			sql.query("UPDATE couponcodes SET ctype='"+c.getType()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usetimes='"+c.getUseTimes()+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET usedplayers='"+plugin.convertHashToString2(c.getUsedPlayers())+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET ids='"+"' WHERE name='"+c.getName()+"'");
			sql.query("UPDATE couponcodes SET money='"+c.getMoney()+"' WHERE name='"+c.getName()+"'");
		}
	}
	
	@Override
	public Coupon getCoupon(String coupon) throws SQLException {
		if (!couponExists(coupon)) return null;
		int usetimes = sql.query("SELECT usetimes FROM couponcodes WHERE name='"+coupon+"'").getInt(1);
		HashMap<String, Boolean> usedplayers = plugin.convertStringToHash2(sql.query("SELECT usedplayers FROM couponcodes WHERE name='"+coupon+"'").getString(1));
		ResultSet rs = sql.query("SELECT ctype FROM couponcodes WHERE name='"+coupon+"'");
		
		if (rs.getString(1).equalsIgnoreCase("Item")) {
			return createNewItemCoupon(coupon, usetimes, plugin.convertStringToHash(sql.query("SELECT ids FROM couponcodes WHERE name='"+coupon+"'").getString(1)), usedplayers);
		}
		
		else if (rs.getString(1).equalsIgnoreCase("Economy")) {
			return createNewEconomyCoupon(coupon, usetimes, usedplayers, sql.query("SELECT money FROM couponcodes WHERE name='"+coupon+"'").getInt(1));
		} else {
			return null;
		}
	}
	
	@Override
	public ItemCoupon createNewItemCoupon(String name, int usetimes, HashMap<Integer, Integer> ids, HashMap<String, Boolean> usedplayers) {
		return new ItemCoupon(name, usetimes, usedplayers, ids);
	}
	
	@Override
	public EconomyCoupon createNewEconomyCoupon(String name, int usetimes, HashMap<String, Boolean> usedplayers, int money) {
		return new EconomyCoupon(name, usetimes, usedplayers, money);
	}
	
	@Override
	public RankCoupon createNewRankCoupon(String name, String group, int usetimes, HashMap<String, Boolean> usedplayers) {
		return new RankCoupon(name, group, usetimes, usedplayers);
	}
}
