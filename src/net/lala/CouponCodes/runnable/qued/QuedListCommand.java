package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class QuedListCommand implements Runnable {

	private CouponCodes plugin;
	private CommandSender sender;
	private CouponManager api;
	private String[] args;
	
	public QuedListCommand(CouponCodes plugin, CommandSender sender, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.api = CouponCodes.getCouponManager();
		this.args = args;
	}
	
	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		try {
			boolean active = true;
			boolean inactive = false;
			String prefix = null;
			if(args.length >= 2) {
				if(args[1].equalsIgnoreCase("all"))
					inactive = true;
				else if(args[1].equalsIgnoreCase("inactive")) {
					inactive = true;
					active = false;
				} else {
					prefix = args[1];
					active = false;
				}
			}
			ArrayList<Coupon> c = Coupon.getAllCoupons(api.getSQL(), active, inactive, prefix);
			if (c.isEmpty() || c.size() <= 0 || c == null) {
				sender.sendMessage(ChatColor.RED + "No coupons found.");
				return;
			} else {
				sb.append(ChatColor.DARK_PURPLE + "Coupon list: " + ChatColor.GOLD);
				for (int i = 0; i < c.size(); i++)
					sb.append(c.get(i).getCode() + ", ");
				int sbl = sb.length();
				sb.delete(sbl - 2, sbl);
				sender.sendMessage(sb.toString());
				return;
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED+"Error while getting the coupon list from the database. Please check the console for more info.");
			sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
			e.printStackTrace();
			return;
		}
	}
}
