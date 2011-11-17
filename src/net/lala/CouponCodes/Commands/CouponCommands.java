package net.lala.CouponCodes.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.lala.CouponCodes.CouponCodes;

public class CouponCommands implements CommandExecutor{

	@SuppressWarnings("unused")
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
		
		if (args[0].equalsIgnoreCase("remove")){
			if (sender.hasPermission("coupon.remove")){
				
			}else{
				sender.sendMessage("You do not have permission to remove a coupon!");
			}
		}
		return false;
	}	
}
