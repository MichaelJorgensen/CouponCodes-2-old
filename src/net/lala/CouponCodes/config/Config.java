package net.lala.CouponCodes.config;

import net.lala.CouponCodes.misc.SQLType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {

	private Plugin plugin;
	private FileConfiguration config;
	
	public Config(Plugin plugin){
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}
	
	public SQLType getSQLType(){
		String type = config.getString("sql-type");
		
		if (type.equalsIgnoreCase("MySQL"))
			return SQLType.MySQL;
		else if (type.equalsIgnoreCase("SQLite"))
			return SQLType.SQLite;
		else
			return SQLType.Unknown;
	}
	
	public String getHostname(){
		return config.getString("MySQL-options.hostname");
	}
	
	public String getPort(){
		return config.getString("MySQL-options.port");
	}
	
	public String getDatabase(){
		return config.getString("MySQL-options.database");
	}
	
	public String getUsername(){
		return config.getString("MySQL-options.username");
	}
	
	public String getPassword(){
		return config.getString("MySQL-options.password");
	}
}