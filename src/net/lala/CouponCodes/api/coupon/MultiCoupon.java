package net.lala.CouponCodes.api.coupon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;
import net.lala.CouponCodes.sql.SQL;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultiCoupon extends Coupon {

	private ArrayList<Coupon> m_codes;
	private int m_multigroup;
	
	public MultiCoupon(String codename, ArrayList<Coupon> codes, int totaluses, long expire, int multigroup) {
		this(0, codename, codes, totaluses, expire, multigroup);
	}

	public MultiCoupon(int id, String codename, ArrayList<Coupon> codes, int totaluses, long expire, int multigroup) {
		super(id, codename, Coupon.MULTI, 0, 1, totaluses, expire);
		m_codes = codes;
		m_multigroup = multigroup;
	}

	public MultiCoupon(ResultSet rs) throws SQLException {
		super(rs);
		ResultSet srs = m_sql.query("SELECT codes.* FROM multi JOIN codes ON multi.effect_code_id=codes.id WHERE trigger_code_id=" + getID());
		m_codes = new ArrayList<Coupon>();
		while(srs.next()) {
			m_codes.add(Coupon.loadFrom(srs));
		}
		srs = m_sql.query("SELECT DISTINCT(mg.id) as id FROM multi m JOIN multigroup mg ON m.multigroup_id=mg.id JOIN codes c ON c.id=m.trigger_code_id WHERE c.id=" + getID());
		if(srs.next())
			m_multigroup = srs.getInt("id");
		else
			m_multigroup = 0;
	}
	
	@Override
	public String effectText() {
		return "Multi";
	}

	@Override
	public void doEffect(Player player) {
		for(Coupon code : m_codes) {
			code.doEffect(player);
		}
	}

	@Override
	public boolean alreadyUsed(SQL sql, String playername) throws SQLException {
		if(super.alreadyUsed(sql, playername))
			return true;
		if(m_multigroup == 0 || getTotalUses() == 0)
			return false;
		String q = "SELECT c.id FROM uses u JOIN codes c ON u.code_id=c.id JOIN multi m ON c.id=m.trigger_code_id JOIN multigroup mg ON m.multigroup_id=mg.id WHERE mg.id=? GROUP BY c.id";
		PreparedStatement ps = sql.getConnection().prepareStatement(q);
		ps.setInt(1, m_multigroup);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			int rc = rs.getInt("id");
			return (rc > 0) ? true : false;
		}
		return false;
	}

	@Override
	public int dbAdd() throws SQLException {
		int newid = super.dbAdd();
		for(Coupon code : m_codes) {
//			Coupon i = Coupon.findCoupon(code);
			m_sql.query("INSERT INTO multi (trigger_code_id, effect_code_id, multigroup_id) VALUES (" + newid + ", " + code.getID() + ", " + m_multigroup + ")");
		}
		return newid;
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
		if (args.length > 6 || args.length < 4) {
			sender.sendMessage(CommandUsage.C_ADD_MULTI.toString());
			return;
		}
		try {
			String name = null;
			int group = 0;
			String[] na = args[2].split(":");
			String[] nga = na[0].split("-");
			if(nga.length == 2) {
				group = api.getGroupID(nga[0]);
				name = nga[1];
			} else
				name = na[0];
			int count = 1;
			if(na.length == 2)
				count = Integer.parseInt(na[1]);
			String addcodes = args[3];
			String[] codes = addcodes.split(":");
			ArrayList<Coupon> ca = new ArrayList<Coupon>();
			for(String code : codes) {
				Coupon c = Coupon.findCoupon(code); 
				ca.add(c);
			}
			int usetimes = 1;
			long time = 0;
			
			if (args.length >= 5) usetimes = Integer.parseInt(args[4]);
			if (args.length >= 6) time = parseExpire(args[5]);

			for(int i = 0; i < count; i++) {
				String codename = name;
				if (count > 1)
					codename = Misc.generateName(12, name);
				
				MultiCoupon mc = new MultiCoupon(codename, ca, usetimes, time, group);
				if(mc.dbAdd() > 0) {
					String msg = sender.getName() + " just added a multi code: " + mc.getCode();
					msg += (group > 0) ? " to group: " + nga[0] : "";
					mc.m_plugin.getLogger().info(msg);
					sender.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + mc.getCode() + ChatColor.GREEN + " has been added!");
				} else
					sender.sendMessage(ChatColor.RED+"This coupon already exists!");
			}
			return;
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.DARK_RED+"Expected a number, but got a string. Please check your syntax.");
			return;
		} catch (SQLException e) {
			sender.sendMessage(ChatColor.DARK_RED+"Error while interacting with the database. See console for more info.");
			sender.sendMessage(ChatColor.DARK_RED+"If this error persists, please report it.");
			e.printStackTrace();
			return;
		}
	}
	
	
}
