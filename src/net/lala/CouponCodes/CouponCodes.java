package net.lala.CouponCodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.api.events.plugin.CouponCodesCommandEvent;
import net.lala.CouponCodes.listeners.DebugListen;
import net.lala.CouponCodes.listeners.PlayerListen;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Metrics;
//import net.lala.CouponCodes.runnable.CouponTimer;
import net.lala.CouponCodes.runnable.CustomDataSender;
import net.lala.CouponCodes.runnable.qued.QuedAddCommand;
import net.lala.CouponCodes.runnable.qued.QuedInfoCommand;
import net.lala.CouponCodes.runnable.qued.QuedListCommand;
import net.lala.CouponCodes.runnable.qued.QuedRedeemCommand;
import net.lala.CouponCodes.runnable.qued.QuedRemoveCommand;
import net.lala.CouponCodes.sql.SQL;
import net.lala.CouponCodes.sql.options.DatabaseOptions;
import net.lala.CouponCodes.sql.options.MySQLOptions;
import net.lala.CouponCodes.sql.options.SQLiteOptions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CouponCodes extends JavaPlugin {
	
	private static CouponCodes instance;
	private static CouponManager cm;
	
	private DatabaseOptions dataop;
	private Config config;
	
	private boolean va = false;
	private boolean debug = false;
	private boolean usethread = true;
	
	private Metrics mt = null;
	
	public SQL sql;
	public Server server;
	public Economy econ;
	public Permission perm;
	
	public String version;
	public String newversion;
	public String verinfo;
	
	@Override
	public void onEnable() {
		instance = this;
		server = getServer();
		config = new Config(this);
		debug = config.getDebug();
		version = getDescription().getVersion();
		usethread = config.getUseThread();
		
		try {
			mt = new Metrics();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setUpdateInfo();
		
		if (!setupVault()) {
			send("Vault support is disabled. This option can be changed in the config.");
			va = false;
		} else {
			send("Vault support is enabled.");
			va = true;
		}
		
		if (!version.equals(newversion) && !version.contains("TEST"))
			send("New update is available for CouponCodes! Current version: "+version+" New version: "+newversion);
		
		// This is for this plugin's own events!
		server.getPluginManager().registerEvents(new DebugListen(this), this);
		
		// Bukkit listeners
		server.getPluginManager().registerEvents(new PlayerListen(this), this);
		
		if (!setupSQL()) {
			send("Database could not be setup. CouponCodes will now disable");
			server.getPluginManager().disablePlugin(this);
			return;
		}
		
		// Timers!
		if (usethread) {
//			getServer().getScheduler().scheduleAsyncRepeatingTask(this, new CouponTimer(), 200L, 200L);
		}
		
		// This timer is required, so it can't be in (usethread)!
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new CustomDataSender(this, mt));
		
		send("is now enabled! Version: "+version);
	}
	
	@Override
	public void onDisable() {
		server.getScheduler().cancelTasks(this);
		send("Tasks cancelled");
		try {
			sql.close();
		} catch (SQLException e) {
			sendErr("Could not close SQL connection");
		} catch (NullPointerException e) {
			sendErr("SQL is null. Connection doesn't exist");
		}
		send("SQL connection closed");
		cm = null;
		send("is now disabled.");
	}
	
	private boolean setupSQL() {
		if (config.getSQLValue().equalsIgnoreCase("MySQL")) {
			dataop = new MySQLOptions(config.getHostname(), config.getPort(), config.getDatabase(), config.getUsername(), config.getPassword());
		}
		else if (config.getSQLValue().equalsIgnoreCase("SQLite")) {
			dataop = new SQLiteOptions(new File(getDataFolder()+"/coupon_data.db"));
		}
		else if (!config.getSQLValue().equalsIgnoreCase("MySQL") && !config.getSQLValue().equalsIgnoreCase("SQLite")) {
			sendErr("The SQLType has the unknown value of: "+config.getSQLValue());
			return false;
		}
		
		sql = new SQL(this, dataop);
		
		try {
			sql.open();
			Coupon.createTables(sql);
			cm = new CouponManager(this, sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean setupVault() {
		if (!config.getVault())
			return false;
		try {
			RegisteredServiceProvider<Economy> ep = server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			RegisteredServiceProvider<Permission> pe = server.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if (ep == null)
				getLogger().info("ep is null!");
			if (pe == null)
				getLogger().info("pe is null!");
			if (ep == null || pe == null)
				return false;
			else {
				getLogger().info("ep and pe are good!");
				econ = ep.getProvider();
				perm = pe.getProvider();
				return true;
			}
		} catch (NoClassDefFoundError e) {
			return false;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		// Event handling
		CouponCodesCommandEvent ev = EventHandle.callCouponCodesCommandEvent(sender, command, commandLabel, args);
		if (ev.isCancelled()) return true;
		sender = ev.getSender();
		command = ev.getCommand();
		commandLabel = ev.getCommandLabel();
		args = ev.getArgs();
		
		if (args.length == 0) {
			help(sender);
			return true;
		}
		
		boolean pl = false;
		if (sender instanceof Player) pl = true;
		
		
		// Add command 2.0
		if (args[0].equalsIgnoreCase("add")) {
			if (args.length < 2) {
				helpAdd(sender);
				return true;
			}
			if (has(sender, "cc.add")) {
				server.getScheduler().scheduleAsyncDelayedTask(this, new QuedAddCommand(this, sender, args));
				return true;
			} else {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command.");
				return true;
			}
		}
		
		// Remove command
		else if (args[0].equalsIgnoreCase("remove")) {
			if (has(sender, "cc.remove")) {
				server.getScheduler().scheduleAsyncDelayedTask(this, new QuedRemoveCommand(sender, args));
				return true;
			} else {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command");
				return true;
			}
		}
		
		// Redeem command
		else if (args[0].equalsIgnoreCase("redeem")) {
			if (!pl) {
				sender.sendMessage("You must be a player to redeem a coupon");
				return true;
			} else {
				Player player = (Player) sender;
				if (has(player, "cc.redeem")) {
					server.getScheduler().scheduleAsyncDelayedTask(this, new QuedRedeemCommand(this, player, args));
					return true;
				} else {
					player.sendMessage(ChatColor.RED+"You do not have permission to use this command");
					return true;
				}
			}
		}
		
		// List command
		else if (args[0].equalsIgnoreCase("list")) {
			if (has(sender, "cc.list")) {
				server.getScheduler().scheduleAsyncDelayedTask(this, new QuedListCommand(this, sender, args));
				return true;
			} else {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command");
				return true;
			}
		}
		
		// Info command
		else if (args[0].equalsIgnoreCase("info")) {
			if (has(sender, "cc.info")) {
				server.getScheduler().scheduleAsyncDelayedTask(this, new QuedInfoCommand(this, sender, args));
				return true;
			} else {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command");
				return true;
			}
		}
		
		// Reload command
		else if (args[0].equalsIgnoreCase("reload")) {
			if (has(sender, "cc.reload")) {
				if (!sql.reload())
					sender.sendMessage(ChatColor.DARK_RED+"Could not reload the database");
				else
					sender.sendMessage(ChatColor.GREEN+"Database reloaded");
				reloadConfig();
				config = new Config(this);
				debug = config.getDebug();
				sender.sendMessage(ChatColor.GREEN+"Config reloaded");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED+"You do not have permission to use this command");
				return true;
			}
		} else {
			help(sender);
			return true;
		}
	}
	
	public boolean has(CommandSender sender, String permission) {
		if (sender instanceof ConsoleCommandSender) return true;
		if (!va) return sender.hasPermission(permission);
		return perm.has(sender, permission);
	}
	
	private void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"|-[] = required-"+ChatColor.DARK_RED+"CouponCodes Help"+ChatColor.GOLD+"-() = optional-|");
		CommandUsage[] s = CommandUsage.values();
		int l = s.length;
		for(int i = 0; i < l; i++) {
			sender.sendMessage(ChatColor.GOLD + "|--" + s[i].toString());
		}
	}
	
	public void helpAdd(CommandSender sender) {
		sender.sendMessage(CommandUsage.C_ADD_ITEM.toString());
		sender.sendMessage(CommandUsage.C_ADD_ECON.toString());
		sender.sendMessage(CommandUsage.C_ADD_RANK.toString());
		sender.sendMessage(CommandUsage.C_ADD_XP.toString());
		sender.sendMessage(CommandUsage.C_ADD_MULTI.toString());
	}
	
	public boolean checkForUpdate() {
		if (newversion == null)
			return false;
		else if (newversion.equals(version))
			return false;
		else
			return true;
	}
	
	public void setUpdateInfo() {
		try {
			URL url = new URL("http://www.craftmod.net/jar/CouponCodes/version.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			newversion = br.readLine();
			
			url = new URL("http://www.craftmod.net/jar/CouponCodes/info.txt");
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			verinfo = br.readLine();
			
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<Integer, Integer> convertStringToHash(String args) {
		HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();
		String[] sp = args.split(",");
		try {
			for (int i = 0; i < sp.length; i++) {
				int a = Integer.parseInt(sp[i].split(":")[0]);
				int b = Integer.parseInt(sp[i].split(":")[1]);
				ids.put(a, b);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return ids;
	}
	
	public String convertHashToString(HashMap<Integer, Integer> hash) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, Integer> en : hash.entrySet()) {
			sb.append(en.getKey()+":"+en.getValue()+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	
	public HashMap<String, Boolean> convertStringToHash2(String args) {
		HashMap<String, Boolean> pl = new HashMap<String, Boolean>();
		if (args.equals(null) || args.length() < 1) return pl;
		String[] sp = args.split(",");
		try {
			for (int i = 0; i < sp.length; i++) {
				String a = String.valueOf(sp[i].split(":")[0]);
				Boolean b = Boolean.valueOf(sp[i].split(":")[1]);
				pl.put(a, b);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return pl;
	}
	
	public String convertHashToString2(HashMap<String, Boolean> hash) {
		if (hash.isEmpty() || hash == null || hash.size() < 1) return "";
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Boolean> en : hash.entrySet()) {
			sb.append(en.getKey()+":"+en.getValue()+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public void send(String message) {
		System.out.println("[CouponCodes] "+message);
	}
	
	public void sendErr(String message) {
		System.err.println("[CouponCodes] [Error] "+message);
	}
	
	public void debug(String message) {
		if (!debug) return;
		System.out.println("[CouponCodes] [Debug] "+message);
	}
	
	public static CouponCodes getInstance() {
		return instance;
	}
	
	public static CouponManager getCouponManager() {
		return cm;
	}
	
	public SQL getSQLAPI() {
		return sql;
	}
	
	public DatabaseOptions getDatabaseOptions() {
		return dataop;
	}
	
	public boolean isVaultEnabled() {
		return va;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public boolean useThread() {
		return usethread;
	}
}
