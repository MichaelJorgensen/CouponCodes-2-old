package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCoupon extends Coupon {

	public EconomyCoupon(String code, int value, int active, int totaluses, long expire) {
		this(0, code, value, active, totaluses, expire);
	}
	public EconomyCoupon(int id, String code, int value, int active, int totaluses, long expire) {
		super(id, code, Coupon.ECONOMY, value, active, totaluses, expire);
	}

	public EconomyCoupon(ResultSet rs) throws SQLException {
		super(rs);
	}

	@Override
	public String effectText() {
		return "Economy";
	}

	@Override
	public void doEffect(Player player) {
		if(!m_plugin.isVaultEnabled())
			player.sendMessage(ChatColor.DARK_RED + "Vault support is currently disabled. You cannot redeem an economy coupon.");
		else {
			m_plugin.econ.depositPlayer(player.getName(), getValue());
			player.sendMessage(ChatColor.GREEN + "Coupon " + ChatColor.GOLD + getCode() + ChatColor.GREEN + " has been redeemed, and "
						+ ChatColor.GOLD + getValue() + ChatColor.GREEN + " Crits have been added to your account.");
		}
	}

	@Override
	public void dbUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dbRemove() {
		// TODO Auto-generated method stub
		
	}

	public static void parseAddArgs(CouponManager api, CommandSender sender, String[] args) {
		if (args.length >= 4) {
			try {
				String code = args[2];
				int active = 1;
				int totaluses = 1;
				long expire = 0;
				int money = Integer.parseInt(args[3]);
				
				if (code.equalsIgnoreCase("random"))
					code = Misc.generateName();
				if (args.length >= 5) active = Integer.parseInt(args[4]);
				if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
				if (args.length >= 7) expire = parseExpire(args[6]);
				if (args.length > 7) {
					sender.sendMessage(CommandUsage.C_ADD_ECON.toString());
					return;
				}
			
				EconomyCoupon ec = new EconomyCoupon(code, money, active, totaluses, expire);
				if(ec.dbAdd() > 0) {
					api.getLogger().info(sender.getName() + " just added an econ code: " + code);
					sender.sendMessage(ChatColor.GREEN + "Coupon " + ChatColor.GOLD + code + ChatColor.GREEN + " has been added!");
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
}
