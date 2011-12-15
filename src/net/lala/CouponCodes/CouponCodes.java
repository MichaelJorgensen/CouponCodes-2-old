package net.lala.CouponCodes;

import java.io.File;
import java.sql.SQLException;

import net.lala.CouponCodes.api.CouponCodesInterface;
import net.lala.CouponCodes.api.SQLAPI;
import net.lala.CouponCodes.api.couponapi.CouponAPI;
import net.lala.CouponCodes.api.couponapi.CouponManager;
import net.lala.CouponCodes.config.Config;
import net.lala.CouponCodes.misc.SQLType;
import net.lala.CouponCodes.sql.DatabaseOptions;
import net.lala.CouponCodes.sql.SQL;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * CouponCodes.java - Main class
 * @author LaLa
 */
public class CouponCodes extends JavaPlugin implements CouponCodesInterface {
	
	private static CouponManager cm = null;
	
	private DatabaseOptions dataop = null;
	private Config config = null;
	private boolean ec = false;
	
	private SQLType sqltype;
	private SQL sql;
	
	public Server server = null;
	public Economy econ = null;
	
	@Override
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
		sqltype = config.getSQLType();
		
		switch (sqltype){
		case MySQL: dataop = new DatabaseOptions(config.getHostname(),
				config.getPort(),
				config.getDatabase(),
				config.getUsername(),
				config.getPassword());
		case SQLite: dataop = new DatabaseOptions(new File(this.getDataFolder()+"/coupon_data.db"));
		case Unknown:
			sendErr("The SQLType has the unknown value of: "+config.getSQLValue()+" CouponCodes will now disable.");
			this.setEnabled(false);
			return;
		}
		
		sql = new SQL(this, dataop);
		
		try {
			sql.createTable("CREATE TABLE IF NOT EXISTS couponcodes (name VARCHAR(24), ENUM('Item', 'Economy'), usetimes INT(10), usedplayers Array, ids Array, money INT(10))");
		} catch (SQLException e) {
			sendErr("SQLException while creating couponcodes table. CouponCodes will now disable.");
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		send("is now enabled! Version: "+this.getDescription().getVersion());
	}
	
	@Override
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		if (args.length == 0 || args[0].equalsIgnoreCase("help")){
			help(sender);
			return true;
		}
		CouponAPI api = CouponCodes.getCouponAPI();
		return false;
	}
	
	private void help(CommandSender sender){
		sender.sendMessage(ChatColor.GOLD+"|---------------------|");
		sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.DARK_RED+"CouponCodes Help"+ChatColor.GOLD+"--|");
		sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"/c help"+ChatColor.GOLD+"--|");
		sender.sendMessage(ChatColor.GOLD+"|---------------------|");
	}
	
	public void send(String message){
		System.out.println("[CouponCodes] "+message);
	}
	
	public void sendErr(String message){
		System.err.println("[CouponCodes] [Error] "+message);
	}
	
	public static CouponAPI getCouponAPI(){
		return (CouponAPI) cm;
	}
	
	public SQLAPI getSQLAPI(){
		return (SQLAPI) sql;
	}
	
	public DatabaseOptions getDatabaseOptions(){
		return dataop;
	}
	
	public boolean isEconomyEnabled(){
		return ec;
	}
	
	public SQLType getSQLType(){
		return sqltype;
	}
}
