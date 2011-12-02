package net.lala.CouponCodes.config;

import net.lala.CouponCodes.misc.SQLType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Config.java - Custom config handling
 * @author LaLa
 */
public class Config {
	
	private FileConfiguration config;
	
	public Config(Plugin plugin) {
		this.config = plugin.getConfig();
		copyDefaults(true);
	}
	
	public void copyDefaults(boolean copy) {
		config.options().copyDefaults(copy);
	}
	
	public SQLType getSQLType() {
		String type = getSQLValue();
		
		if (type.equalsIgnoreCase("MySQL")) {
			return SQLType.MySQL;
		}
		else if (type.equalsIgnoreCase("SQLite")) {
			return SQLType.SQLite;
		} else {
			return SQLType.Unknown;
		}
	}
	
	public String getSQLValue() {
		return config.getString("sql-type");
	}
	
	public String getHostname() {
		return config.getString("MySQL-options.hostname");
	}
	
	public String getPort() {
		return config.getString("MySQL-options.port");
	}
	
	public String getDatabase() {
		return config.getString("MySQL-options.database");
	}
	
	public String getUsername() {
		return config.getString("MySQL-options.username");
	}
	
	public String getPassword() {
		return config.getString("MySQL-options.password");
	}
}