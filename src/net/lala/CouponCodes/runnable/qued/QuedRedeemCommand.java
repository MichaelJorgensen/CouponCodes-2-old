package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.misc.CommandUsage;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuedRedeemCommand implements Runnable {

	private Player player;
	private String[] args;
	
	private boolean va;
	private CouponManager api;
	private Permission perm;
	private Economy econ;
	
	public QuedRedeemCommand(CouponCodes plugin, Player player, String[] args) {
		this.player = player;
		this.args = args;
		this.va = plugin.isVaultEnabled();
		this.api = CouponCodes.getCouponManager();
		this.perm = plugin.perm;
		this.econ = plugin.econ;
	}
	
	@Override
	public void run() {
		if (args.length == 2) {
			try {
				Coupon coupon = api.findCoupon(args[1]);
				if (coupon == null || coupon.getActive() == 0) {
					player.sendMessage(ChatColor.RED+"That coupon doesn't exist!");
					return;
				}
				
				if (api.getTimesUsed(coupon) > coupon.getTotalUses()) {
					player.sendMessage(ChatColor.RED+"This coupon has been used up!");
					return;
				}
				
				if(api.alreadyUsed(player.getName(), coupon)) {
					player.sendMessage(ChatColor.RED+"You have already used this coupon");
					return;
				}
				
				if (api.isExpired(coupon)) {
					player.sendMessage(ChatColor.RED+"This coupon has expired!");
					return;
				}
				
				api.doEffect(player, coupon);
				api.addUse(player, coupon);
				return;
			} catch (SQLException e) {
				player.sendMessage(ChatColor.DARK_RED+"Error while trying to find "+ChatColor.GOLD+args[1]+ChatColor.DARK_RED+" in the database. Please check the console for more info.");
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
