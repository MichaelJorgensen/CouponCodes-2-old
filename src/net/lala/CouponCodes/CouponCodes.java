package net.lala.CouponCodes;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.sql.DatabaseOptions;

import org.bukkit.plugin.java.JavaPlugin;

public class CouponCodes extends JavaPlugin {
	
	private static CouponManager cm;
	private DatabaseOptions op;
	
	public void onEnable(){
		if (cm == null)
			cm = new CouponManager(this);
	}
	
	public void onDisable(){
		
	}
	
	public void send(String message){
		System.out.println("[CouponCodes] "+message);
	}
	
	public static CouponManager getCouponManager(){
		return cm;
	}

}
