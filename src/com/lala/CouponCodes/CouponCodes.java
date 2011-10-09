package com.lala.CouponCodes;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.lala.CouponCodes.Configuration.SQLiteCfg;

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
		if (!sql.checkTable("couponcodes")){
			sql.query("create table couponcodes (name, usetimes, itemsids, usedplayers)");
		}
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
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		if (command.getName().equalsIgnoreCase("coupon")){
			SQLiteCfg cfg = new SQLiteCfg(this);
			ArrayList<Integer> ints = new ArrayList<Integer>();
			ints.add(46);
			ints.add(50);
			ArrayList<String> up = new ArrayList<String>();
			up.add("hi");
			
			cfg.addCoupon(new Coupon("test", 1, ints, up, 765));
		}
		return true;
	}
}