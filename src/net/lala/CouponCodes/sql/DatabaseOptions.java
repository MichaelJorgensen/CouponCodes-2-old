package net.lala.CouponCodes.sql;

import java.io.File;

import net.lala.CouponCodes.misc.SQLType;

/**
 * DatabaseOptions.java - Provides an easy way of sharing a database's options among classes
 * @author LaLa
 */
public class DatabaseOptions {

	//General Variables
	private SQLType sqltype = SQLType.Unknown;
	
	//MySQL variables
	private String hostname = null;
	private String pn = null;
	private String database = null;
	private String username = null;
	private String password = null;
	
	//SQLite variables
	File sqlFile = null;
	
	/**
	 * For setting up MySQL
	 * @param hostname
	 * @param portnumber
	 * @param database
	 * @param username
	 * @param password
	 */
	public DatabaseOptions(String hostname, String portnumber, String database, String username, String password) {
		this.sqltype = SQLType.MySQL;
		this.hostname = hostname;
		this.pn = portnumber;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * For setting up SQLite
	 * @param sqlFile
	 */
	public DatabaseOptions(File sqlFile) {
		this.sqltype = SQLType.SQLite;
		this.sqlFile = sqlFile;
	}
	
	public SQLType getSQLType() {
		return sqltype;
	}
	
	public File getSQLFile() {
		return sqlFile;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getPort() {
		return pn;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
