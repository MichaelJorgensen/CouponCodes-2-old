package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.misc.CommandUsage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuedRedeemCommand implements Runnable {

	private Player player;
	private String[] args;
	
	private CouponManager api;
	
	public QuedRedeemCommand(CouponCodes plugin, Player player, String[] args) {
		this.player = player;
		this.args = args;
		this.api = CouponCodes.getCouponManager();
	}
	
	@Override
	public void run() {
		if (args.length == 2) {
			try {
				Coupon coupon = Coupon.findCoupon(args[1]);
				if (coupon == null) {
					player.sendMessage(ChatColor.RED + "That coupon doesn't exist!");
					api.getLogger().info(player.getName() + " failed to redeem non-existant code: " + args[1]);
					api.addAttempt(player, args[1]);
					return;
				}
				
				String eff = coupon.effectText();
				if (coupon.getActive() == 0) {
					player.sendMessage(ChatColor.RED + "That coupon doesn't exist!");
					api.getLogger().info(player.getName() + " failed to redeem inactive " + eff + " code: " + args[1]);
					api.addAttempt(player, args[1]);
					return;
				}
				
				if (Coupon.isExpired(api.getSQL(), coupon)) {
					player.sendMessage(ChatColor.RED + "That coupon is expired!");
					api.getLogger().info(player.getName() + " failed to redeem expired " + eff + " code: " + args[1]);
					api.addAttempt(player, args[1]);
					return;
				}
				
				if (Coupon.getTimesUsed(api.getSQL(), coupon) > coupon.getTotalUses() && coupon.getTotalUses() > 0) {
					player.sendMessage(ChatColor.RED + "This coupon has been used up!");
					api.getLogger().info(player.getName() + " failed to redeem used up " + eff + " code: " + args[1]);
					api.addAttempt(player, args[1]);
					return;
				}
				
				if(coupon.alreadyUsed(api.getSQL(), player.getName())) {
					player.sendMessage(ChatColor.RED + "You have already used this coupon");
					api.getLogger().info(player.getName() + " failed to redeem already used " + eff + " code: " + args[1]);
					api.addAttempt(player, args[1]);
					return;
				}
				
				if (Coupon.isExpired(api.getSQL(), coupon)) {
					player.sendMessage(ChatColor.RED + "This coupon has expired!");
					api.getLogger().info(player.getName() + " failed to redeem expired " + eff + " code: " + args[1]);
					api.addAttempt(player, args[1]);
					return;
				}
				
				coupon.doEffect(player);
				api.addUse(player, coupon);
				api.getLogger().info(player.getName() + " redeemed " + eff + " code: " + coupon.getCode());
				return;
			} catch (SQLException e) {
				player.sendMessage(ChatColor.DARK_RED + "Error while trying to find " + ChatColor.GOLD + args[1] + ChatColor.DARK_RED + 
						" in the database. Please check the console for more info.");
				player.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
				e.printStackTrace();
				return;
			}
		} else {
			player.sendMessage(CommandUsage.C_REDEEM.toString());
			return;
		}
	}
}
