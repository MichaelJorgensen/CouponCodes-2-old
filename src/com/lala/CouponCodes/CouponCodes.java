package com.lala.CouponCodes;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class CouponCodes extends JavaPlugin{

	public boolean iconomy;
	public boolean bosecon;
	public Plugin boseconomy;
	
	public File directory;
	public Server server;
	
	public SQLite sql;
	
	private Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable(){		
		try {
			this.setupMySQLDatabase();
		} catch (SQLException e) {
			send("========================================");
			send("Error while initializing SQLite Database", 3);
			send("========================================");
			e.printStackTrace();
		}
		
		directory = this.getDataFolder();
		server = this.getServer();
		send("is now Enabled!");
	}
	
	public void onDisable(){
		send("is now Disabled!");
	}
	
	public void setupMySQLDatabase() throws SQLException{
		this.sql = new SQLite(this.log, "[CouponCodes]", "coupondata", this.getDataFolder().getAbsolutePath());
	}
	
	public Configuration getBukkitConfiguration(){
		try {
			Configuration cfg = new Configuration(new File("bukkit.yml"));
			cfg.load();
			return cfg;
		}catch (Exception e){
			return null;
		}
	}
	
	public Configuration getConfig(){
		return this.getConfiguration();
	}
	
	public void send(String message){
		System.out.println("[CouponCodes] "+message);
	}
	
	public void send(String message, int times){
		for (int i = times; i > 0; i--){
			send(message);
		}
	}
}