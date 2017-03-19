package net.lala.CouponCodes.runnable.qued;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
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
		Coupon.parseAddArgs(api, sender, args);
	}
}
