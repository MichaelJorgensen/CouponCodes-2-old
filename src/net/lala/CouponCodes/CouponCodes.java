package net.lala.CouponCodes;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.SQLite;
import net.lala.CouponCodes.Configuration.SQLiteCfg;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
			send("Table 'couponcodes' does not exist, creating");
			sql.createTable("CREATE TABLE couponcodes (name String, usetimes Integer, itemsids ArrayList, usedplayers ArrayList)");
			return;
		}
		send("Table 'couponcodes' detected");
	}
	
	public void send(String message){
		System.out.println("[CouponCodes] "+message);
	}
	
	public void send(String message, int times){
		for (int i = times; i > 0; i--){
			send(message);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String cl, String[] args){
		
		//TODO: proper coding..
		
		SQLiteCfg db = new SQLiteCfg(this, sql);
		
		
		
		
		/**ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		ids.add(46);
		ids.add(57);
		names.add("Notch");
		names.add("DumbFuck");
		names.add("fucker");
		sender.sendMessage(db.addCoupon(new Coupon("testcoupon", 1, ids, names, 10)));*/
		return true;
	}
}