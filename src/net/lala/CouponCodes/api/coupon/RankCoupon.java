package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCoupon extends Coupon {

	private String m_rank;
	
	public RankCoupon(String code, int value, int active, int totaluses, long expire) {
		this(0, code, value, active, totaluses, expire);
	}
	
	public RankCoupon(int id, String code, int value, int active, int totaluses, long expire) {
		super(id, code, Coupon.RANK, value, active, totaluses, expire);
	}

	public RankCoupon(ResultSet rs) throws SQLException {
		super(rs);
		ResultSet rrs = m_sql.query("SELECT name FROM ranks WHERE id=" + getValue());
		if(rrs.next())
			m_rank = rrs.getString("name");
	}

	@Override
	public String effectText() {
		return "Rank";
	}

	@Override
	public void doEffect(Player player) {
		if (!m_plugin.isVaultEnabled()) {
			player.sendMessage(ChatColor.DARK_RED+"Vault support is currently disabled. You cannot redeem a rank coupon.");
			return;
		} else {
			boolean permbuk = m_plugin.perm.getName().equalsIgnoreCase("PermissionsBukkit");
			try {
				ResultSet rs = m_plugin.sql.query("SELECT name FROM ranks WHERE id=" + getValue());
				rs.next();
				if (permbuk) {
					m_plugin.perm.playerAddGroup((String) null, player.getName(), m_rank);
					for (String i : m_plugin.perm.getPlayerGroups((String) null, player.getName())) {
						if (i.equalsIgnoreCase(m_rank)) continue;
						m_plugin.perm.playerRemoveGroup((String) null, player.getName(), i);
					}
				} else {
					m_plugin.perm.playerAddGroup(player, m_rank);
					for (String pg : m_plugin.perm.getPlayerGroups(player)) {
						if (pg.equalsIgnoreCase(m_rank)) continue;
						m_plugin.perm.playerRemoveGroup(player, pg);
					}
				}
				player.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + getCode() + ChatColor.GREEN +
						" has been redeemed, and your group has been set to " + ChatColor.GOLD + m_rank);
			} catch(Exception e) {
				e.printStackTrace();
			}
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

	static public void parseAddArgs(CouponManager api, CommandSender sender, String[] args) {
		if(args.length >= 4) {
			try {
				String code = args[2];
				String group = args[3];
				int active = 1;
				int totaluses = 1;
				long expire = 0;
				
				if (code.equalsIgnoreCase("random")) code = Misc.generateName();
				if (args.length >= 5) active = Integer.parseInt(args[4]);
				if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
				if (args.length >= 7) expire = parseExpire(args[6]);
				if (args.length > 7) {
					sender.sendMessage(CommandUsage.C_ADD_RANK.toString());
					return;
				}

				int rankid = api.getRankID(group);
//				public RankCoupon(String code, int effect, int value, int active, int totaluses, long expire) {
				RankCoupon rc = new RankCoupon(code, rankid, active, totaluses, expire);
				if(rc.dbAdd() > 0) {
					api.getLogger().info(sender.getName() + " just added a rank code: " + code);
					sender.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + code + ChatColor.GREEN+" has been added!");
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
}
