package net.lala.CouponCodes.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.sql.SQL;

public class CouponManager {
	
	private CouponCodes plugin;
	private SQL sql;
	private Logger m_log;
	
	public CouponManager(CouponCodes plugin, SQL sql) {
		this.plugin = plugin;
		m_log = plugin.getLogger();
		this.sql = sql;
	}
	
	public void addUse(Player player, Coupon coupon) throws SQLException {
		int playerid = getUserID(player.getName());
		String s = "INSERT INTO uses (user_id, code_id) VALUES (?, ?)";
		PreparedStatement ps = sql.getConnection().prepareStatement(s);
		ps.setInt(1, playerid);
		ps.setInt(2, coupon.getID());
	}
	
	public void addAttempt(Player player, String code) throws SQLException {
		int playerid = getUserID(player.getName());
		String s ="INSERT INTO attempt (user_id, code) VALUES (?, ?)";
		PreparedStatement ps = sql.getConnection().prepareStatement(s);
		ps.setInt(1, playerid);
		ps.setString(2, code);
		ps.executeUpdate();
	}
	
	public int getUserID(String username) throws SQLException {
		ResultSet rs = sql.query("SELECT id FROM users WHERE name='" + username + "'");
		if(!rs.next()) {
			sql.query("INSERT INTO users (name) VALUES ('" + username + "')");
			rs = sql.query("SELECT id FROM users WHERE name='" + username + "'");
			rs.next();
		}
		return rs.getInt(1);
	}
	
	public int getRankID(String rankname) throws SQLException {
		ResultSet rs = sql.query("SELECT id FROM ranks WHERE name='" + rankname + "'");
		if(!rs.next())
			sql.query("INSERT INTO ranks (name) VALUES ('" + rankname + "')");
		rs = sql.query("SELECT id FROM ranks WHERE name='" + rankname + "'");
		rs.next();
		return rs.getInt("id");
	}
	
	public int getWarpID(Double x, Double y, Double z) throws SQLException {
		String s = "SELECT id FROM warp WHERE ";
		s += (x == null) ? "x IS NULL" : "x=" + x;
		s += " AND ";
		s += (y == null) ? "y IS NULL" : "y=" + y;
		s += " AND ";
		s += (z == null) ? "z IS NULL" : "z=" + z;
		ResultSet rs = sql.query(s);
		if(!rs.next()) {
			String i = "INSERT INTO warp (x, y, z) VALUES (?, ?, ?)";
			PreparedStatement ps = sql.getConnection().prepareStatement(i);
			if(x == null)
				ps.setNull(1, java.sql.Types.DOUBLE);
			else
				ps.setDouble(1, x);
			if(y == null)
				ps.setNull(2, java.sql.Types.DOUBLE);
			else
				ps.setDouble(2, y);
			if(z == null)
				ps.setNull(3, java.sql.Types.DOUBLE);
			else
				ps.setDouble(3, z);
			int rc = ps.executeUpdate();
		} else
			return rs.getInt("id");
		rs = sql.query(s);
		rs.next();
		return rs.getInt("id");
	}
	
	public int getGroupID(String group) throws SQLException {
		ResultSet rs = sql.query("SELECT id FROM multigroup WHERE name='" + group + "'");
		if(!rs.next()) {
			sql.query("INSERT INTO multigroup (name) VALUES ('" + group + "')");
			rs = sql.query("SELECT id FROM multigroup WHERE name='" + group + "'");
			rs.next();
		}
		return rs.getInt(1);
	}
	
	public SQL getSQL() {
		return sql;
	}
	
	public Logger getLogger() {
		return m_log;
	}
}
