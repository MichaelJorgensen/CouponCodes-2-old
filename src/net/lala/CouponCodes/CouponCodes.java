package net.lala.CouponCodes;

import java.io.File;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import net.lala.CouponCodes.api.CouponCodesInterface;
import net.lala.CouponCodes.api.SQLAPI;
import net.lala.CouponCodes.api.couponapi.Coupon;
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
import org.bukkit.entity.Player;
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
	public void onEnable() {
		if (cm == null)
			cm = new CouponManager(this);
		
		if (server == null)
			server = getServer();
		
		if (!setupEcon()){
			send("Economy support is disabled.");
			ec = false;
		} else {
			ec = true;
		}
		
		config = new Config(this);
		sqltype = config.getSQLType();
		
		switch (sqltype) {
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
			sql.open();
			sql.createTable("CREATE TABLE IF NOT EXISTS couponcodes (name VARCHAR(24), ENUM('Item', 'Economy'), usetimes INT(10), usedplayers Array, ids Array, money INT(10))");
		} catch (SQLException e) {
			sendErr("SQLException while creating couponcodes table. CouponCodes will now disable.");
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		this.saveConfig();
		send("is now enabled! Version: "+this.getDescription().getVersion());
	}
	
	@Override
	public void onDisable() {
		this.saveConfig();
		try {
			sql.close();
		} catch (SQLException e){
			sendErr("Could not close SQL connection");
		} catch (NullPointerException e){
			sendErr("SQL is null. Connection doesn't exist.");
		}
		send("is now disabled.");
	}
	
	private boolean setupEcon() {
		try {
			RegisteredServiceProvider<Economy> ep = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (ep == null)
				return false;
			else
				econ = ep.getProvider();
				return true;
		} catch (NoClassDefFoundError e){
			return false;
		}
	}
	
	/*
	 * Notes
	 * /c add item [name] [item1,item2] [usetimes]
	 * /c add econ [name] [money] [usetimes]
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		boolean pl = false;
		if (sender instanceof Player) pl = true;
		if (args.length == 0 || args[0].equalsIgnoreCase("help")){
			help(sender);
			return true;
		}
		CouponAPI api = CouponCodes.getCouponAPI();
		if (args[0].equalsIgnoreCase("add")) {
			if (sender.hasPermission("cc.add")) {
				if (args[0].equalsIgnoreCase("item")) {
					if (args.length == 5) {
						try {
							Coupon coupon = api.createNewItemCoupon(args[2], Integer.parseInt(args[4]), (Array) new ArrayList<String>(Arrays.asList(args[3].split(","))), null);
							if (api.couponExists(coupon)) {
								sender.sendMessage(ChatColor.DARK_RED+"This coupon already exists!");
								return true;
							} else {
								coupon.addToDatabase();
								return true;
							}
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got "+ChatColor.YELLOW+args[4]);
							return true;
						} catch (SQLException e) {
							sender.sendMessage(ChatColor.DARK_RED+"Error while adding coupon to database. Please check console for more info.");
							sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
							e.printStackTrace();
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"Invalid syntax length");
						help(sender);
						return true;
					}
				}
				else if (args[0].equalsIgnoreCase("econ")) {
					if (args.length == 5) {
						try{
							Coupon coupon = api.createNewEconomyCoupon(args[2], Integer.parseInt(args[4]), (Array) new ArrayList<String>(Arrays.asList(args[3].split(","))), Integer.parseInt(args[3]));
							if (api.couponExists(coupon)) {
								sender.sendMessage(ChatColor.DARK_RED+"This coupon already exists!");
								return true;
							} else {
								coupon.addToDatabase();
								return true;
							}
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got "+ChatColor.YELLOW+args[3]);
							return true;
						} catch (SQLException e) {
							sender.sendMessage(ChatColor.DARK_RED+"Error while adding coupon to database. Please check console for more info.");
							sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
							e.printStackTrace();
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"Invalid syntax length");
						help(sender);
						return true;
					}
				} else {
					help(sender);
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command");
				return true;
			}
		}
		return false;
	}
	
	private void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"|---------------------|");
		sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.DARK_RED+"CouponCodes Help"+ChatColor.GOLD+"--|");
		sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"/c help"+ChatColor.GOLD+"--|");
		sender.sendMessage(ChatColor.GOLD+"|---------------------|");
	}
	
	public void send(String message) {
		System.out.println("[CouponCodes] "+message);
	}
	
	public void sendErr(String message) {
		System.err.println("[CouponCodes] [Error] "+message);
	}
	
	public static CouponAPI getCouponAPI() {
		return (CouponAPI) cm;
	}
	
	public SQLAPI getSQLAPI() {
		return (SQLAPI) sql;
	}
	
	public DatabaseOptions getDatabaseOptions() {
		return dataop;
	}
	
	public boolean isEconomyEnabled() {
		return ec;
	}
	
	public SQLType getSQLType() {
		return sqltype;
	}
}
