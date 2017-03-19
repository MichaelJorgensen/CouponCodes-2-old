package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XPCoupon extends Coupon {

	public XPCoupon(String code, int value, int active,	int totaluses, long expire) {
		this(0, code, value, active, totaluses, expire);
	}
	
	public XPCoupon(int id, String code, int value, int active,	int totaluses, long expire) {
		super(id, code, Coupon.XP, value, active, totaluses, expire);
		// TODO Auto-generated constructor stub
	}

	public XPCoupon(ResultSet rs) throws SQLException {
		super(rs);
	}

	@Override
	public String effectText() {
		return "XP";
	}

	@Override
	public void doEffect(Player player) {
		player.setLevel(player.getLevel() + getValue());
		player.sendMessage(ChatColor.GREEN + "Coupon " + ChatColor.GOLD + getCode() + ChatColor.GREEN + " has been redeemed, and you have received " +
				ChatColor.GOLD + getValue() + ChatColor.GREEN + " XP levels!");
	}

	@Override
	public void dbUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dbRemove() {
		// TODO Auto-generated method stub

	}

	static public void parseAddArgs(CouponManager api, CommandSender sender, String[] args) {
		if (args.length >= 4) {
			try {
				String code = args[2];
				int xp = Integer.parseInt(args[3]);
				int active = 1;
				int totaluses = 1;
				long expire = 0;
				
				if (code.equalsIgnoreCase("random")) code = Misc.generateName();
				if (args.length >= 5) active = Integer.parseInt(args[4]);
				if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
				if (args.length >= 7) expire = parseExpire(args[6]);
				
				if (args.length > 7) {
					sender.sendMessage(CommandUsage.C_ADD_XP.toString());
					return;
				}
				
				XPCoupon xc = new XPCoupon(code, xp, active, totaluses, expire);
				if(xc.dbAdd() > 0) {
					api.getLogger().info(sender.getName() + " just added an xp code: " + code);
					sender.sendMessage(ChatColor.GREEN + "XPCoupon "+ChatColor.GOLD + code + ChatColor.GREEN+" has been added!");
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
	}
}
