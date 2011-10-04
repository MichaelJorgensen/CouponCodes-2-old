package com.lala.CouponCodes.Configuration;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.ChatColor;

import com.lala.CouponCodes.Coupon;
import com.lala.CouponCodes.CouponCodes;

public class SQLiteCfg {

	private CouponCodes plugin;
	private Coupon c;
	private SQLite sql = plugin.sql;
	public SQLiteCfg(CouponCodes plugin){
		this.plugin = plugin;
	}
	
	public String addCoupon(Coupon coupon){
		ResultSet rs = sql.query("select * from couponcodes;");
		ArrayList<String> couponnames = new ArrayList<String>();
		
		try{
			while (rs.next()){
				couponnames.add(rs.getString(1));
			}
		} catch (SQLException e){
			plugin.send("SQLException while adding a coupon");
			e.printStackTrace();
		}
		
		if (couponnames.contains(coupon.getName())){
			return String.valueOf(ChatColor.RED + "Coupon already exists!");
		}
		
		try {
			PreparedStatement ps = sql.getConnection().prepareStatement("insert into couponcodes values (?, ?)");			
			ps.setString(1, coupon.getName());
			ps.setString(2, "no");
			ps.addBatch();
			sql.getConnection().setAutoCommit(false);
			ps.executeBatch();
			sql.getConnection().setAutoCommit(true);
		} catch (SQLException e){
			plugin.send("Error while using SQLite database to add coupon");
			return String.valueOf(ChatColor.RED + "Error while using SQLite database to add coupon, coupon creation failed");
		}
		
		try{
			rs.close();
			sql.getConnection().close();
			sql.close();
		} catch (SQLException e){
			plugin.send("Error while closing SQLite connections");
			return String.valueOf(ChatColor.RED + "Error while closing SQLite connections, coupon creation failed");
		}
		return String.valueOf(ChatColor.GREEN + "Coupon "+ChatColor.LIGHT_PURPLE+coupon.getName()+ChatColor.GREEN+" has been created!");
	}
}