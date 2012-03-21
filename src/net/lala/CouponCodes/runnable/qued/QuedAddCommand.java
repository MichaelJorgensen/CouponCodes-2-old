package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;
import java.util.HashMap;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class QuedAddCommand implements Runnable {

	private CouponCodes plugin;
	private CommandSender sender;
	private String[] args;
	private CouponManager api;
	
	public QuedAddCommand(CouponCodes plugin, CommandSender sender, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.args = args;
		this.api = CouponCodes.getCouponManager();
	}
	
	@Override
	public void run() {
		if (args[1].equalsIgnoreCase("item")) {
			if (args.length >= 4) {
				try {
					String name = args[2];
					int active = 1;
					int totaluses = 1;
					int time = -1;
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
					if (args.length >= 7) time = Integer.parseInt(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_ITEM.toString());
						return;
					}
					
					if (api.addItemCoupon(name, args[3], active, totaluses, time)) {
						sender.sendMessage(ChatColor.GREEN+"Coupon "+ChatColor.GOLD+name+ChatColor.GREEN+" has been added!");
						return;
					} else {
						sender.sendMessage(ChatColor.RED+"This coupon already exists!");
						return;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got a string. Please check your syntax.");
					return;
				} catch (SQLException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Error while interacting with the database. See console for more info.");
					sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
					e.printStackTrace();
					return;
				}
			} else {
				sender.sendMessage(CommandUsage.C_ADD_ITEM.toString());
				return;
			}
		}
		
		else if (args[1].equalsIgnoreCase("econ")) {
			if (args.length >= 4) {
				try {
					String name = args[2];
					int active = 1;
					int usetimes = 1;
					int time = -1;
					int money = Integer.parseInt(args[3]);
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) usetimes = Integer.parseInt(args[5]);
					if (args.length >= 7) time = Integer.parseInt(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_ECON.toString());
						return;
					}
					
					if(api.addEconomyCoupon(name, money, active, usetimes, time)) {
						sender.sendMessage(ChatColor.GREEN+"Coupon "+ChatColor.GOLD+name+ChatColor.GREEN+" has been added!");
						return;
					} else {
						sender.sendMessage(ChatColor.RED+"This coupon already exists!");
						return;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got a string. Please check your syntax.");
					return;
				} catch (SQLException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Error while interacting with the database. See console for more info.");
					sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
					e.printStackTrace();
					return;
				}
			} else {
				sender.sendMessage(CommandUsage.C_ADD_ECON.toString());
				return;
			}
		}
		
		else if (args[1].equalsIgnoreCase("rank")) {
			if (args.length >= 4) {
				try {
					String name = args[2];
					String group = args[3];
					int active = 1;
					int usetimes = 1;
					int time = -1;
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) usetimes = Integer.parseInt(args[5]);
					if (args.length >= 7) time = Integer.parseInt(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_RANK.toString());
						return;
					}

					int rankid = api.getRankID(group);
					if(api.addRankCoupon(name, rankid, active, usetimes, time)) {
						sender.sendMessage(ChatColor.GREEN+"Coupon "+ChatColor.GOLD+name+ChatColor.GREEN+" has been added!");
						return;
					} else {
						sender.sendMessage(ChatColor.RED+"This coupon already exists!");
						return;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got a string. Please check your syntax.");
					return;
				} catch (SQLException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Error while interacting with the database. See console for more info.");
					sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
					e.printStackTrace();
					return;
				}
			} else {
				sender.sendMessage(CommandUsage.C_ADD_RANK.toString());
				return;
			}
		}
		
		else if (args[1].equalsIgnoreCase("xp")) {
			if (args.length >= 4) {
				try {
					String name = args[2];
					int xp = Integer.parseInt(args[3]);
					int active = 1;
					int usetimes = 1;
					int time = -1;
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) usetimes = Integer.parseInt(args[5]);
					if (args.length >= 7) time = Integer.parseInt(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_XP.toString());
						return;
					}
					
					if(api.addXPCoupon(name, xp, active, usetimes, time)) {
						sender.sendMessage(ChatColor.GREEN+"Coupon "+ChatColor.GOLD+name+ChatColor.GREEN+" has been added!");
						return;
					} else {
						sender.sendMessage(ChatColor.RED+"This coupon already exists!");
						return;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got a string. Please check your syntax.");
					return;
				} catch (SQLException e) {
					sender.sendMessage(ChatColor.DARK_RED+"Error while interacting with the database. See console for more info.");
					sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
					e.printStackTrace();
					return;
				}
			} else {
				sender.sendMessage(CommandUsage.C_ADD_XP.toString());
				return;
			}
		} else if (args[1].equalsIgnoreCase("multi")) {
			try {
				String[] na = args[2].split(":");
				String name = na[0];
				int count = 1;
				if(na.length == 2)
					count = Integer.parseInt(na[1]);
				String addcodes = args[3];
				String[] codes = addcodes.split(":");
				int usetimes = 1;
				int time = -1;
				
				if (args.length >= 5) usetimes = Integer.parseInt(args[4]);
				if (args.length >= 6) time = Integer.parseInt(args[5]);
				if (args.length > 6) {
					sender.sendMessage(CommandUsage.C_ADD_XP.toString());
					return;
				}

				for(int i = 0; i < count; i++) {
					String codename = name;
					if (count > 1)
						codename = Misc.generateName(24, name);
					if(api.addMultiCoupon(codename, codes, usetimes, time))
						sender.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + codename + ChatColor.GREEN + " has been added!");
					else
						sender.sendMessage(ChatColor.RED+"This coupon already exists!");
				}
				return;
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got a string. Please check your syntax.");
				return;
			} catch (SQLException e) {
				sender.sendMessage(ChatColor.DARK_RED+"Error while interacting with the database. See console for more info.");
				sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
				e.printStackTrace();
				return;
			}
			
		} else {
			plugin.helpAdd(sender);
			return;
		}
	}
}
