package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.misc.CommandUsage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class QuedRemoveCommand implements Runnable {

	private CommandSender sender;
	private String[] args;
	private CouponManager api;
	
	public QuedRemoveCommand(CommandSender sender, String[] args) {
		this.sender = sender;
		this.args = args;
		this.api = CouponCodes.getCouponManager();
	}
	
	@Override
	public void run() {
		if (args.length == 2) {
			try {
				if (args[1].equalsIgnoreCase("all")) {
					Coupon.deleteAllCoupons(api.getSQL());
					sender.sendMessage(ChatColor.GREEN + "All coupons removed.");
					return;
				}
				Coupon c = Coupon.findCoupon(api.getSQL(), args[1]);
				if(c == null) {
					sender.sendMessage(ChatColor.RED + "That coupon doesn't exist!");
					return;
				}
				Coupon.deleteCoupon(api.getSQL(), c);
				sender.sendMessage(ChatColor.GREEN+"The coupon " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " has been removed.");
				return;
			} catch (SQLException e) {
				sender.sendMessage(ChatColor.DARK_RED+"Error while removing coupon from the database. Please check the console for more info.");
				sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
				e.printStackTrace();
				return;
			}
		} else {
			sender.sendMessage(CommandUsage.C_REMOVE.toString());
			return;
		}
	}
}
