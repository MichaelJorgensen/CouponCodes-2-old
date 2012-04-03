package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultiCoupon extends Coupon {

	private ArrayList<Coupon> m_codes;
	
	public MultiCoupon(String codename, ArrayList<Coupon> codes, int totaluses, long expire) {
		this(0, codename, codes, totaluses, expire);
	}

	public MultiCoupon(int id, String codename, ArrayList<Coupon> codes, int totaluses, long expire) {
		super(id, codename, Coupon.MULTI, 0, 1, totaluses, expire);
		m_codes = codes;
	}

	public MultiCoupon(ResultSet rs) throws SQLException {
		super(rs);
		ResultSet srs = m_sql.query("SELECT codes.* FROM multi JOIN codes ON multi.effect_code_id=codes.id WHERE trigger_code_id=" + getID());
		m_codes = new ArrayList<Coupon>();
		while(srs.next()) {
			m_codes.add(Coupon.loadFrom(srs));
		}
	}
	
	@Override
	public String effectText() {
		return "Multi";
	}

	@Override
	public void doEffect(Player player) {
		for(Coupon code : m_codes) {
//			Coupon coupon = Coupon.findCoupon(code);
			code.doEffect(player);
		}
	}

	@Override
	public int dbAdd() throws SQLException {
		int newid = super.dbAdd();
		for(Coupon code : m_codes) {
//			Coupon i = Coupon.findCoupon(code);
			m_sql.query("INSERT INTO multi (trigger_code_id, effect_code_id) VALUES (" + newid + ", " + code.getID() + ")");
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
			String[] na = args[2].split(":");
			String name = na[0];
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
					codename = Misc.generateName(24, name);
				
				MultiCoupon mc = new MultiCoupon(codename, ca, usetimes, time);
				if(mc.dbAdd() > 0) {
					mc.m_plugin.getLogger().info(sender.getName() + " just added a multi code: " + name);
					sender.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + codename + ChatColor.GREEN + " has been added!");
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
		}/* catch (Exception e) {
			sender.sendMessage(e.getMessage());
			e.printStackTrace();
		}*/
	}

}
