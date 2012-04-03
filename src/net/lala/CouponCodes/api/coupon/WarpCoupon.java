package net.lala.CouponCodes.api.coupon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCoupon extends Coupon {

	private Double m_x;
	private Double m_y;
	private Double m_z;
	
	public WarpCoupon(String code, int warp_id, int active, int totaluses, long expire) {
		this(0, code, warp_id, active, totaluses, expire);
	}
	
	public WarpCoupon(int id, String code, int warp_id, int active, int totaluses, long expire) {
		super(id, code, Coupon.WARP, warp_id, active, totaluses, expire);
	}

	public WarpCoupon(ResultSet rs) throws SQLException {
		super(rs);
		ResultSet xyzrs = m_sql.query("SELECT * FROM warp WHERE id=" + getValue());
		if(xyzrs.next()) {
			m_x = xyzrs.getDouble("x");
			if(xyzrs.wasNull())
				m_x = null;
			m_y = xyzrs.getDouble("y");
			if(xyzrs.wasNull())
				m_y = null;
			m_z = xyzrs.getDouble("z");
			if(xyzrs.wasNull())
				m_z = null;
		}
	}

	@Override
	public String effectText() {
		return "Warp";
	}

	@Override
	public int dbAdd() throws SQLException {
		int newid = super.dbAdd();
		String q = "INSERT INTO warp (x, y, z) VALUES(?, ?, ?)";
		PreparedStatement ps = m_sql.getConnection().prepareStatement(q);
		if(m_x == null)
			ps.setNull(1, java.sql.Types.DOUBLE);
		else
			ps.setDouble(1, m_x);
		if(m_y == null)
			ps.setNull(2, java.sql.Types.DOUBLE);
		else
			ps.setDouble(2, m_y);
		if(m_z == null)
			ps.setNull(3, java.sql.Types.DOUBLE);
		else
			ps.setDouble(3, m_z);
		int rc = ps.executeUpdate();
		return newid;
	}

	@Override
	public void doEffect(Player player) {
		double x;
		double y;
		double z;
		Location pl = player.getLocation();
		x = (m_x == null) ? pl.getX() : m_x;
		y = (m_y == null) ? pl.getY() : m_y;
		z = (m_z == null) ? pl.getZ() : m_z;

		Location nl = new Location(player.getWorld(), x, y, z);
		player.teleport(nl);
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
		if(args.length >= 6) {
				String code = args[2];
				Double x, y, z;
				x = parseDouble(args[3]);
				y = parseDouble(args[4]);
				z = parseDouble(args[5]);
				int active = 1;
				int totaluses = 1;
				long expire = 0;
				
			try {
				if (code.equalsIgnoreCase("random")) code = Misc.generateName();
				if (args.length >= 7) active = Integer.parseInt(args[6]);
				if (args.length >= 8) totaluses = Integer.parseInt(args[7]);
				if (args.length >= 9) expire = parseExpire(args[8]);
				if (args.length > 9) {
					sender.sendMessage(CommandUsage.C_ADD_RANK.toString());
					return;
				}

				int warp_id = api.getWarpID(x, y, z);
				WarpCoupon rc = new WarpCoupon(code, warp_id, active, totaluses, expire);
				if(rc.dbAdd() > 0) {
					api.getLogger().info(sender.getName() + " just added a warp code: " + code);
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
			sender.sendMessage(CommandUsage.C_ADD_WARP.toString());
			return;
		}
	}

	static public Double parseDouble(String sd) {
		Double ret = null;
		try {
			ret = Double.parseDouble(sd);
		} catch(NumberFormatException e) {
			return null;
		} 
		return ret;
	}
}
