package net.lala.CouponCodes.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.SQLAPI;
import net.lala.CouponCodes.api.events.database.DatabaseCloseConnectionEvent;
import net.lala.CouponCodes.api.events.database.DatabaseOpenConnectionEvent;
import net.lala.CouponCodes.api.events.database.DatabaseQueryEvent;
import net.lala.CouponCodes.misc.SQLType;

import org.bukkit.Bukkit;

/**
 * SQL.java - MySQL, SQLite handling
 * @author LaLa
 */
public class SQL extends SQLAPI {
	
	private DatabaseOptions dop;
	
	private SQLType sqltype = SQLType.Unknown;
	private Connection con = null;
	
	public SQL(CouponCodes plugin, DatabaseOptions dop) {
		super(plugin);
		this.dop = dop;
		this.sqltype = plugin.getSQLType();
	}
	
	@Override
	public DatabaseOptions getDatabaseOptions() {
		return dop;
	}
	
	@Override
	public Connection getConnection() {
		return con;
	}
	
	@Override
	public boolean open() throws SQLException {
		if (sqltype.equals(SQLType.MySQL)){
			con = DriverManager.getConnection("jdbc:mysql://"+dop.getHostname()+":"+dop.getPort()+"/"+dop.getDatabase(), dop.getUsername(), dop.getPassword());
			DatabaseOpenConnectionEvent ev = new DatabaseOpenConnectionEvent(con, dop, true);
			Bukkit.getServer().getPluginManager().callEvent(ev);
			return true;
		}
		else if (sqltype.equals(SQLType.SQLite)) {
			con = DriverManager.getConnection("jdbc:sqlite:"+dop.getSQLFile().getAbsolutePath());
			DatabaseOpenConnectionEvent ev = new DatabaseOpenConnectionEvent(con, dop, true);
			Bukkit.getServer().getPluginManager().callEvent(ev);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void close() throws SQLException {
		con.close();
		DatabaseCloseConnectionEvent ev = new DatabaseCloseConnectionEvent(con, dop);
		Bukkit.getServer().getPluginManager().callEvent(ev);
	}
	
	@Override
	public boolean reload() throws SQLException {
		con.close();
		return open();
	}
	
	@Override
	public ResultSet query(String query) throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		
		st = con.createStatement();
		if (query.toLowerCase().contains("delete")) {
			st.executeUpdate(query);
			DatabaseQueryEvent ev = new DatabaseQueryEvent(dop, query, rs);
			Bukkit.getServer().getPluginManager().callEvent(ev);
			return rs;
		} else {
			rs = st.executeQuery(query);
			DatabaseQueryEvent ev = new DatabaseQueryEvent(dop, query, rs);
			Bukkit.getServer().getPluginManager().callEvent(ev);
			return rs;
		}
	}
	
	@Override
	public boolean createTable(String table) throws SQLException {
		Statement st = con.createStatement();
		return st.execute(table);
	}
}
