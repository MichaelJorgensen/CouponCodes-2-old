package com.lala.CouponCodes.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.lala.CouponCodes.CouponCodes;

public class CouponCommands implements CommandExecutor{

	private CouponCodes plugin;
	public CouponCommands(CouponCodes cc){
		this.plugin = cc;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
		
		if (args[0].equalsIgnoreCase("add")){
			if (sender.hasPermission("coupon.add")){
				
			}else{
				sender.sendMessage("You do not have permission to add a coupon!");
				return true;
			}
		}
		return false;
	}	
}
