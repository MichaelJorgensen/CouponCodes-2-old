package net.lala.CouponCodes.misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum CommandUsage {
	C_ADD_ITEM	(ChatColor.YELLOW+"/c add item [name] [item1:amount,item2:amount..] (active) (usetimes) (expire)"),
	C_ADD_ECON	(ChatColor.YELLOW+"/c add econ [name] [money] (active) (usetimes) (expire)"),
	C_ADD_RANK	(ChatColor.YELLOW+"/c add rank [name] [group] (active) (usetimes) (expire)"),
	C_ADD_XP	(ChatColor.YELLOW+"/c add xp [name] [xp] (active) (usetimes) (expire)"),
	C_ADD_WARP	(ChatColor.YELLOW+"/c add warp [name] [x] [y] [z] (active) (usetimes) (expire)"),
	C_ADD_BAD	(ChatColor.YELLOW+"/c add bad [name] [burn/explode/chicken/lightning/poison/drop] (active) (usetimes) (expire)"),
	C_ADD_MULTI	(ChatColor.YELLOW+"/c add multi [name(:count)] [subcoupon1(:subcoupon2...)] (usetimes) (expire)"),
	C_REDEEM	(ChatColor.YELLOW+"/c redeem [name]"),
	C_REMOVE	(ChatColor.YELLOW+"/c remove [name/all]"),
	C_LIST		(ChatColor.YELLOW+"/c list (all/inactive/prefix)"),
	C_INFO		(ChatColor.YELLOW+"/c info (name)"),
	C_RELOAD	(ChatColor.YELLOW+"/c reload");
	
	private String usage;
	
	private CommandUsage(String usage) {
		this.usage = usage;
	}
	
	public String getUsage() {
		return usage;
	}
	
	@Override
	public String toString() {
		return getUsage();
	}
	
	static public void sendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "|-[] = required-" + ChatColor.DARK_RED + "CouponCodes Help" + ChatColor.GOLD + "-() = optional-|");
		CommandUsage[] v = CommandUsage.values();
		for(CommandUsage u : v)
			sender.sendMessage(ChatColor.GOLD + "|--" + u.toString());
	}
}
