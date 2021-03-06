package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.coupon.EconomyCoupon;
import net.lala.CouponCodes.api.coupon.ItemCoupon;
import net.lala.CouponCodes.api.coupon.RankCoupon;
import net.lala.CouponCodes.api.coupon.XpCoupon;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class QuedInfoCommand implements Runnable {

	private CouponCodes plugin;
	private CommandSender sender;
	private String[] args;
	private CouponManager api;
	
	public QuedInfoCommand(CouponCodes plugin, CommandSender sender, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.args = args;
		this.api = CouponCodes.getCouponManager();
	}
	
	@Override
	public void run() {
		try {
			if (args.length == 2) {
				Coupon c = api.getCoupon(args[1]);
				if (c != null) {
					sender.sendMessage(ChatColor.GOLD+"|----------------------|");
					sender.sendMessage(ChatColor.GOLD+"|---"+ChatColor.DARK_RED+"Coupon "+ChatColor.YELLOW+c.getName()+ChatColor.DARK_RED+" info"+ChatColor.GOLD+"---|");
					sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Name: "+ChatColor.DARK_PURPLE+c.getName());
					sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Type: "+ChatColor.DARK_PURPLE+c.getType());
					sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Use times left: "+ChatColor.DARK_PURPLE+c.getUseTimes());
					if (c.getTime() != -1)
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Time left: "+ChatColor.DARK_PURPLE+c.getTime()+ChatColor.YELLOW+" seconds");
					else
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Time left: "+ChatColor.DARK_PURPLE+"Unlimited");
					sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Expired: "+ChatColor.DARK_PURPLE+c.isExpired());
					if (c.getUsedPlayers().isEmpty())
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Used players: "+ChatColor.DARK_PURPLE+"None");
					else
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Used players: "+ChatColor.DARK_PURPLE+plugin.convertHashToString2(c.getUsedPlayers()));
					if (c instanceof ItemCoupon)
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Items: "+ChatColor.DARK_PURPLE+plugin.convertHashToString(((ItemCoupon) c).getIDs()));
					else if (c instanceof EconomyCoupon)
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Money: "+ChatColor.DARK_PURPLE+((EconomyCoupon) c).getMoney());
					else if (c instanceof RankCoupon)
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Rank: "+ChatColor.DARK_PURPLE+((RankCoupon) c).getGroup());
					else if (c instanceof XpCoupon)
						sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"XP: "+ChatColor.DARK_PURPLE+((XpCoupon) c).getXp());
					sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Totally random name: "+ChatColor.DARK_PURPLE+Misc.generateName());
					sender.sendMessage(ChatColor.GOLD+"|----------------------|");
					return;
				} else {
					sender.sendMessage(ChatColor.RED+"That coupon doesn't exist!");
					return;
				}
			} else {
				StringBuilder sb1 = new StringBuilder();
				StringBuilder sb2 = new StringBuilder();
				ArrayList<String> co = api.getCoupons();
				int total = 0;
				if (co.isEmpty() || co.equals(null)) {
					sb1.append("None");
					sb2.append("Out of those, 0% are item, 0% are economy, and 0% are rank coupons.");
				} else {
					double j = co.size();
					double it = 0;
					double ec = 0;
					double ra = 0;
					double xp = 0;
					String it2 = null;
					String ec2 = null;
					String ra2 = null;
					String xp2 = null;
					DecimalFormat d = new DecimalFormat("##.##");
					for (String name : co) {
						sb1.append(name+", ");
						Coupon coo = api.getBasicCoupon(name);
						if (coo instanceof ItemCoupon) it++;
						if (coo instanceof EconomyCoupon) ec++;
						if (coo instanceof RankCoupon) ra++;
						if (coo instanceof XpCoupon) xp++;
					}
					sb1.deleteCharAt(sb1.length()-1);
					sb1.deleteCharAt(sb1.length()-1);
					it2 = d.format(it/j*100);
					ec2 = d.format(ec/j*100);
					ra2 = d.format(ra/j*100);
					xp2 = d.format(xp/j*100);
					total = (int) (it+ec+ra+xp);
					sb2.append("Out of those, "+it2+"% are item, "+ec2+"% are economy, "+ra2+"% are rank, and "+xp2+"% are XP coupons.");
				}
				sender.sendMessage(ChatColor.GOLD+"|-----------------------|");
				sender.sendMessage(ChatColor.GOLD+"|-"+ChatColor.DARK_RED+"Info on current coupons"+ChatColor.GOLD+"-|");
				sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.GOLD+"Use /c info [name] to view a specific coupon");
				sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Current coupons: "+ChatColor.DARK_PURPLE+sb1.toString());
				sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+sb2.toString());
				sender.sendMessage(ChatColor.GOLD+"|--"+ChatColor.YELLOW+"Total Coupons: "+ChatColor.DARK_PURPLE+total);
				sender.sendMessage(ChatColor.GOLD+"|-----------------------|");
				return;
			}
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED+"Error while finding coupons in the database. Please check the console for more info.");
			sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
			e.printStackTrace();
			return;
		}
	}
}
