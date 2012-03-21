package net.lala.CouponCodes.api;

import java.util.HashMap;
import java.util.Map.Entry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.EventHandle;
import net.lala.CouponCodes.sql.SQL;

public class CouponManager {
	
	private CouponCodes plugin;
	private SQL sql;
	private Logger m_log;
	
	public CouponManager(CouponCodes plugin, SQL sql) {
		this.plugin = plugin;
		m_log = plugin.getLogger();
		this.sql = sql;
	}
	
	public int addCouponToDatabase(Coupon coupon) throws SQLException {
		if(Coupon.couponExists(sql, coupon)) return 0;
		String q = "INSERT INTO codes (code, effect, value, totaluses, expire, active) VALUES ('" +
				coupon.getCode() + "', " + coupon.getEffect() + ", " + coupon.getValue() + ", " + coupon.getTotalUses() + ", " +
				coupon.getExpireTimestamp().getTime() + ", " + coupon.getActive() + ")";
		m_log.info("addCouponToDatabase: " + q);
		sql.query(q);
		ResultSet rs = sql.query("SELECT MAX(id) FROM codes");
		rs.next();
		int newid = rs.getInt(1);
		EventHandle.callCouponAddToDatabaseEvent(coupon);
		return newid;
	}
	
	public boolean removeCouponFromDatabase(Coupon coupon) throws SQLException {
		if (!Coupon.couponExists(sql, coupon)) return false;
		sql.query("DELETE FROM codes WHERE id="+coupon.getID());
		EventHandle.callCouponRemoveFromDatabaseEvent(coupon);
		return true;
	}
	
	public void addUse(Player player, Coupon coupon) throws SQLException {
		int playerid = getUserID(player.getName());
		sql.query("INSERT INTO uses (user_id, code_id) VALUES (" + playerid + ", " + coupon.getID() + ")");
	}
	
	public int getUserID(String username) throws SQLException {
		ResultSet rs = sql.query("SELECT id FROM users WHERE name='" + username + "'");
		if(!rs.next()) {
			sql.query("INSERT INTO users (name) VALUES ('" + username + "')");
			rs = sql.query("SELECT id FROM users WHERE name='" + username + "'");
			rs.next();
		}
		return rs.getInt(1);
	}
	
	public int getRankID(String rankname) throws SQLException {
		ResultSet rs = sql.query("SELECT id FROM ranks WHERE name='" + rankname + "'");
		if(!rs.next()) {
			sql.query("INSERT INTO ranks (name) VALUES ('" + rankname + "')");
			rs = sql.query("SELECT id FROM ranks WHERE name='" + rankname + "'");
			rs.next();
		}
		return rs.getInt(1);
	}
	
	public SQL getSQL() {
		return sql;
	}
	
	public void doEffect(Player player, Coupon coupon) {
		switch(coupon.getEffect()) {
		case Coupon.ECONOMY:
			doEconomy(player, coupon);
			break;
		case Coupon.ITEMS:
			doItems(player, coupon);
			break;
		case Coupon.RANK:
			doRank(player, coupon);
			break;
		case Coupon.XP:
			doXP(player, coupon);
			break;
		case Coupon.MULTI:
			doMulti(player, coupon);
			break;
		}
	}

	public void doEconomy(Player player, Coupon coupon) {
		if(!plugin.isVaultEnabled())
			player.sendMessage(ChatColor.DARK_RED + "Vault support is currently disabled. You cannot redeem an economy coupon.");
		else {
			plugin.econ.depositPlayer(player.getName(), coupon.getValue());
			player.sendMessage(ChatColor.GREEN + "Coupon " + ChatColor.GOLD + coupon.getCode() + ChatColor.GREEN + " has been redeemed, and the money added to your account!");
		}
	}
	
	public void doRank(Player player, Coupon coupon) {
		m_log.info("isVaultEnabled: " + plugin.isVaultEnabled());
		if (!plugin.isVaultEnabled()) {
			player.sendMessage(ChatColor.DARK_RED+"Vault support is currently disabled. You cannot redeem a rank coupon.");
			return;
		} else {
			boolean permbuk = plugin.perm.getName().equalsIgnoreCase("PermissionsBukkit");
			try {
				ResultSet rs = plugin.sql.query("SELECT name FROM ranks WHERE id=" + coupon.getValue());
				rs.next();
				String rankname = rs.getString(1);
				if (permbuk) {
					plugin.perm.playerAddGroup((String) null, player.getName(), rankname);
					for (String i : plugin.perm.getPlayerGroups((String) null, player.getName())) {
						if (i.equalsIgnoreCase(rankname)) continue;
						plugin.perm.playerRemoveGroup((String) null, player.getName(), i);
					}
				} else {
					plugin.perm.playerAddGroup(player, rankname);
					for (String pg : plugin.perm.getPlayerGroups(player)) {
						if (pg.equalsIgnoreCase(rankname)) continue;
						plugin.perm.playerRemoveGroup(player, pg);
					}
				}
				player.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + coupon.getCode() + ChatColor.GREEN +
						" has been redeemed, and your group has been set to " + ChatColor.GOLD + rankname);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void doXP(Player player, Coupon coupon) {
		player.setLevel(player.getLevel() + coupon.getValue());
		player.sendMessage(ChatColor.GREEN + "Coupon " + ChatColor.GOLD + coupon.getCode() + ChatColor.GREEN + " has been redeemed, and you have received " +
				ChatColor.GOLD + coupon.getValue() + ChatColor.GREEN + " XP levels!");
	}
	
	public void doItems(Player player, Coupon coupon) {
		try {
			ResultSet rs = sql.query("SELECT item_id, amount FROM items WHERE coupon_id=" + coupon.getID());
			while(rs.next()) {
				int iid = rs.getInt(1);
				int iamount = rs.getInt(2);
				ItemStack is = new ItemStack(iid, iamount);
				if (player.getInventory().firstEmpty() == -1) {
					player.getLocation().getWorld().dropItem(player.getLocation(), is);
					player.sendMessage(ChatColor.RED + "Your inventory is full, so the items have been dropped below you.");
				} else {
					player.getInventory().addItem(is);
					player.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + coupon.getCode() + ChatColor.GREEN + " has been redeemed, and the items added to your inventory!");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doMulti(Player player, Coupon coupon) {
		try {
			ResultSet rs = plugin.sql.query("SELECT codes.* FROM multi JOIN codes ON multi.effect_code_id=codes.id WHERE trigger_code_id=" + coupon.getID());
			while(rs.next()) {
				Coupon c = new Coupon(rs);
				doEffect(player, c);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean addItemCoupon(String code, HashMap<Integer, Integer> items, int active, int totaluses, long expire) throws SQLException {
		Coupon c = new Coupon();
		c.setCode(code);
		c.setEffect(Coupon.ITEMS);
		c.setActive(active);
		c.setUseTimes(totaluses);
		Timestamp ets = new Timestamp(expire);
		c.setExpireTimestamp(ets);
		c.addToDatabase();
		int id = c.getID();
		for(Entry<Integer, Integer> pair : items.entrySet()) {
			sql.query("INSERT INTO items (coupon_id, item_id, amount) VALUES (" + id + ", " + pair.getKey() + ", " + pair.getValue() + ")");
		}
		return true;
	}
	
	public boolean addEconomyCoupon(String code, int amount, int active, int totaluses, long expire) throws SQLException {
		Coupon c = new Coupon();
		c.setCode(code);
		c.setEffect(Coupon.ECONOMY);
		c.setValue(amount);
		c.setActive(active);
		c.setUseTimes(totaluses);
		Timestamp ets = new Timestamp(expire);
		c.setExpireTimestamp(ets);
		return c.addToDatabase();
	}
	
	public boolean addRankCoupon(String code, int rankid, int active, int totaluses, long expire) throws SQLException {
		Coupon c = new Coupon();
		c.setCode(code);
		c.setEffect(Coupon.RANK);
		c.setValue(rankid);
		c.setActive(active);
		c.setUseTimes(totaluses);
		Timestamp ets = new Timestamp(expire);
		c.setExpireTimestamp(ets);
		return c.addToDatabase();
	}
	
	public boolean addXPCoupon(String code, int amount, int active, int totaluses, long expire) throws SQLException {
		Coupon c = new Coupon();
		c.setCode(code);
		c.setEffect(Coupon.XP);
		c.setValue(amount);
		c.setActive(active);
		c.setUseTimes(totaluses);
		Timestamp ets = new Timestamp(expire);
		c.setExpireTimestamp(ets);
		return c.addToDatabase();
	}
	
	public boolean addMultiCoupon(String code, String[] subcodes, int totaluses, long expire) throws SQLException {
		Coupon c = new Coupon();
		c.setCode(code);
		c.setEffect(Coupon.MULTI);
		c.setUseTimes(totaluses);
		Timestamp ets = new Timestamp(expire);
		c.setExpireTimestamp(ets);
		c.addToDatabase();
		int id = c.getID();
		for(String sc : subcodes) {
			Coupon subc = Coupon.findCoupon(sql, sc);
			sql.query("INSERT INTO multi (trigger_code_id, effect_code_id) VALUES (" + id + ", " + subc.getID() + ")");
		}
		return true;
	}
}
