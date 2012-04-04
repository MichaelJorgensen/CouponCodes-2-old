package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCoupon extends Coupon {
	
	ArrayList<Item> m_items = null;
	
	public ItemCoupon(ResultSet rs) throws SQLException {
		super(rs);
		m_items = new ArrayList<Item>();
		ResultSet irs = m_sql.query("SELECT item_id, amount, damage, enchantment FROM items WHERE code_id=" + getID());
		while(irs.next()) {
			int iid = irs.getInt("item_id");
			int iamount = irs.getInt("amount");
			short idamage = (short) irs.getInt("damage");
			int ienchantment = irs.getInt("enchantment");
			m_items.add(new Item(iid, iamount, idamage, ienchantment));
		}
	}

	public ItemCoupon(String name, ArrayList<Item> items, int active, int totaluses, long expire) {
		this(0, name, items, active, totaluses, expire);
	}

	public ItemCoupon(int id, String name, ArrayList<Item> items, int active, int totaluses, long expire) {
		super(id, name, Coupon.ITEMS, 0, active, totaluses, expire);
		m_items = items; 
	}

	@Override
	public String effectText() {
		return "Items";
	}

	@Override
	public void doEffect(Player player) {
		try {
			for(Item item : m_items) {
				ItemStack is = item.getItemStack();
				if(item.enchantment() > 0) {
					Enchantment enc = Enchantment.getById(item.enchantment());
					is.addEnchantment(enc, 1);
				}
				if (player.getInventory().firstEmpty() == -1) {
					player.getLocation().getWorld().dropItem(player.getLocation(), is);
					player.sendMessage(ChatColor.RED + "Your inventory is full, so the items have been dropped below you.");
				} else {
					player.getInventory().addItem(is);
				}
			}
			player.sendMessage(ChatColor.GREEN+"Coupon " + ChatColor.GOLD + getCode() + ChatColor.GREEN + " has been redeemed, and the items added to your inventory!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int dbAdd() throws SQLException {
		int newid = super.dbAdd();
		for(Item item : m_items)
			m_sql.query("INSERT INTO items (code_id, item_id, amount, damage, enchantment) VALUES (" + newid + ", " + item.id() + ", " + item.amount() + 
					 ", " + item.damage() + ", " + item.enchantment() + ")");
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
		if (args.length >= 4) {
			try {
				String code = args[2];
				if(code.equalsIgnoreCase("random"))
					code = Misc.generateName();

				ArrayList<Item> items = Item.parseToItems(args[3]);

				int active = 1;
				int totaluses = 1;
				long expire = 0;
				if (args.length >= 5) active = Integer.parseInt(args[4]);
				if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
				if (args.length >= 7) expire = parseExpire(args[6]);
				if (args.length > 7) {
					sender.sendMessage(CommandUsage.C_ADD_ITEM.toString());
					throw new Exception("Too many parameters");
				}
				ItemCoupon ic = new ItemCoupon(code, items, active, totaluses, expire);
				if(ic.dbAdd() > 0) {
					ic.m_plugin.getLogger().info(sender.getName() + " just added an item code: " + code);
					sender.sendMessage(ChatColor.GREEN + "ItemCoupon " + ChatColor.GOLD + code + ChatColor.GREEN + " has been added!");
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
			} catch(Exception e) {
				String msg = e.getMessage();
				sender.sendMessage(ChatColor.DARK_RED + "Your item string did not parse correctly. Error: " + msg);
			}
		} else {
			sender.sendMessage(CommandUsage.C_ADD_ITEM.toString());
			return;
		}
	}
}
