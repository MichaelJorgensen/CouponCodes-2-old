package net.lala.CouponCodes.api.couponapi;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.SQLAPI;

/**
 * CouponManager.java - Allows other plugins to interact with coupons
 * @author LaLa
 */
public class CouponManager implements CouponAPI{
	
	private SQLAPI sql;
	
	public CouponManager(CouponCodes plugin){
		this.sql = plugin.getSQLAPI();
	}
	
	public CouponManager(SQLAPI sql){
		this.sql = sql;
	}
	
	@Override
	public boolean addCouponToDatabase(Coupon coupon) throws SQLException {		
		Connection con = sql.getConnection();
		con.createStatement().executeUpdate("INSERT INTO couponcodes VALUES('"+coupon.getName()+"', '"+coupon.getCouponType().value()+"', "+coupon.getUseTimes()+", "+coupon.getUsedPlayers()+", "+coupon.getMoney());
		return true;
	}
	
	@Override
	public boolean removeCouponFromDatabase(Coupon coupon) throws SQLException {
		if (!couponExists(coupon)) return false;		
		sql.query("DELETE FROM couponcodes WHERE name='"+coupon.getName()+"'");
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
		ResultSet rs = sql.query("SELECT name FROM couponcodes");
		if (rs.equals(null)) return null;
		ArrayList<String> c = new ArrayList<String>();
		while (rs.next())
			c.add(rs.getString(1));
		return c;
	}
	
	@Override
	public Coupon createNewItemCoupon(String name, int usetimes, Array ids, Array usedplayers){
		return new Coupon(name, usetimes, ids, usedplayers);
	}
	
	@Override
	public Coupon createNewEconomyCoupon(String name, int usetimes, Array usedplayers, int money){
		return new Coupon(name, usetimes, usedplayers, money);
	}
}
