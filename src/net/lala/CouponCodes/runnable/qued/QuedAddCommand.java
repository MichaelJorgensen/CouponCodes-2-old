package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Item;
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
					long time = 0;
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
					if (args.length >= 7) time = parseExpire(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_ITEM.toString());
						return;
					}
//					HashMap<Integer, Integer> aa = parseToItemMap(args[3]);
					ArrayList<Item> items = Item.parseToItems(args[3]);
					if(api.addItemCoupon(name, items, active, totaluses, time)) {
						api.getLogger().info(sender.getName() + " just added an item code: " + name);
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
				} catch(Exception e) {
					String msg = e.getMessage();
					sender.sendMessage(ChatColor.DARK_RED + "Your item string did not parse correctly. Error: " + msg);
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
					long time = 0;
					int money = Integer.parseInt(args[3]);
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) usetimes = Integer.parseInt(args[5]);
					if (args.length >= 7) time = parseExpire(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_ECON.toString());
						return;
					}
					
					if(api.addEconomyCoupon(name, money, active, usetimes, time)) {
						api.getLogger().info(sender.getName() + " just added an econ code: " + name);
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
					long time = 0;
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) usetimes = Integer.parseInt(args[5]);
					if (args.length >= 7) time = parseExpire(args[6]);
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_RANK.toString());
						return;
					}

					int rankid = api.getRankID(group);
					if(api.addRankCoupon(name, rankid, active, usetimes, time)) {
						api.getLogger().info(sender.getName() + " just added a rank code: " + name);
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
					long time = 0;
					
					if (name.equalsIgnoreCase("random")) name = Misc.generateName();
					if (args.length >= 5) active = Integer.parseInt(args[4]);
					if (args.length >= 6) usetimes = Integer.parseInt(args[5]);
					if (args.length >= 7) time = parseExpire(args[6]);
					
					if (args.length > 7) {
						sender.sendMessage(CommandUsage.C_ADD_XP.toString());
						return;
					}
					
					if(api.addXPCoupon(name, xp, active, usetimes, time)) {
						api.getLogger().info(sender.getName() + " just added an xp code: " + name);
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
			if (args.length > 6 || args.length < 4) {
				sender.sendMessage(CommandUsage.C_ADD_MULTI.toString());
				return;
			}
			try {
				String[] na = args[2].split(":");
				String name = na[0];
				int count = 1;
				if(na.length == 2)
					count = Integer.parseInt(na[1]);
				String addcodes = args[3];
				String[] codes = addcodes.split(":");
				int usetimes = 1;
				long time = 0;
				
				if (args.length >= 5) usetimes = Integer.parseInt(args[4]);
				if (args.length >= 6) time = parseExpire(args[5]);

				for(int i = 0; i < count; i++) {
					String codename = name;
					if (count > 1)
						codename = Misc.generateName(24, name);
					if(api.addMultiCoupon(codename, codes, usetimes, time)) {
						api.getLogger().info(sender.getName() + " just added a multi code: " + name);
						sender.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + codename + ChatColor.GREEN + " has been added!");
					} else
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
	
	public long parseExpire(String expire) {
		long time = 0;
		try {
			if(expire.charAt(0) == '+') {
				String val = expire.substring(1, expire.length());
				String[] vals = val.split(":");
				int amt = Integer.parseInt(vals[0]);
				String unit = vals[1];
				long now = (new Date()).getTime();
				long exptime = 0;
				if(unit.toLowerCase().contains("min") == true)
					exptime = now + (amt * 1000 * 60);
				else if(unit.toLowerCase().contains("hour") == true)
					exptime = now + (amt * 1000 * 60 * 60);
				else if(unit.toLowerCase().contains("day") == true)
					exptime = now + (amt * 1000 * 60 * 60 * 24);
				else if(unit.toLowerCase().contains("week") == true)
					exptime = now + (amt * 1000 * 60 * 60 * 24 * 7);
				time = exptime;
			} else
				time = Integer.parseInt(args[6]);
		} catch(NumberFormatException e) {
			return 0;
		}
		return time;
	}
	
	public HashMap<Integer, Integer> parseToItemMap(String s) throws Exception {
		HashMap<Integer, Integer> wm = new HashMap<Integer, Integer>();
		String[] splits = s.split(",");
		for(String str : splits) {
			String[] strsplit = str.split(":");
			int item_id = Integer.valueOf(strsplit[0]);
			if(item_id < 0 || item_id > 385)
				throw(new Exception("ID_RANGE"));
			int amount = Integer.valueOf(strsplit[1]);
			if(amount < 0 || amount > 64)
				throw(new Exception("AMOUNT_RANGE"));
			wm.put(item_id, amount);
		}
		return wm;
	}
}