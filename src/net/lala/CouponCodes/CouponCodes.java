package net.lala.CouponCodes;

import java.io.File;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.config.Config;
import net.lala.CouponCodes.sql.DatabaseOptions;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CouponCodes extends JavaPlugin {
	
	private static CouponManager cm = null;
	
	private DatabaseOptions dataop = null;
	private Config config = null;
	private boolean ec = false;
	
	public Server server = null;
	public Economy econ = null;
	
	public void onEnable(){
		if (cm == null)
			cm = new CouponManager(this);
		
		if (server == null)
			server = getServer();
		
		if (!setupEcon()){
			send("Economy support is disabled.");
			ec = false;
		}else{
			ec = true;
		}
		
		config = new Config(this);
		
		switch (config.getSQLType()){
		case MySQL: dataop = new DatabaseOptions(config.getHostname(),
				config.getPort(),
				config.getDatabase(),
				config.getUsername(),
				config.getPassword());
		case SQLite: dataop = new DatabaseOptions(new File(this.getDataFolder()+"/coupon_data.db"));
		default:
			sendErr("SQLType not found. Please check the config. CouponCodes will now disable.");
			this.setEnabled(false);
			return;
		}
	}
	
	public void onDisable(){
		send("is now disabled.");
	}
	
	private boolean setupEcon(){
		RegisteredServiceProvider<Economy> ep = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (ep == null)
			return false;
		else
			econ = ep.getProvider();
			return true;
	}
	
	public void send(String message){
		System.out.println("[CouponCodes] "+message);
	}
	
	public void sendErr(String message){
		System.err.println("[CouponCodes] "+message);
	}
	
	public static CouponManager getCouponManager(){
		return cm;
	}
	
	public DatabaseOptions getDatabaseOptions(){
		return dataop;
	}
	
	public boolean isEconomyEnabled(){
		return ec;
	}
}
