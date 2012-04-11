package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.sql.SQL;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Item {
	private int m_bid;
	private int m_amount;
	private short m_damage;
	private int m_enchantment;
	public Item(int item_bid, int amount, short damage, int enchantment) {
		m_bid = item_bid;
		m_amount = amount;
		m_damage = damage;
		m_enchantment = enchantment;
	}
	
	public ItemStack getItemStack() {
		ItemStack stack = null;
		if(this.damage() == 0)
			stack = new ItemStack(this.id(), this.amount());
		else
			stack = new ItemStack(this.id(), this.amount(), this.damage());

		if(enchantment() > 0) {
			Enchantment enc = Enchantment.getById(enchantment());
			try {
				stack.addEnchantment(enc, 1);
			} catch(IllegalArgumentException e) {
				String msg = "Failed to add enchantment #" + enchantment() + " to item #" + id();
				CouponCodes.getCouponManager().getLogger().info(msg);
			}
		}
		
		return stack;
	}

	public int id() { return m_bid; }
	public int amount() { return m_amount; }
	public short damage() { return m_damage; }
	public int enchantment() { return m_enchantment; }
	
	static public ArrayList<Item> findItems(SQL sql, String code) {
		ArrayList<Item> ret = new ArrayList<Item>();
		String q = "SELECT items.* FROM items JOIN codes ON items.code_id=codes.id WHERE code='" + code + "'";
		try {
			ResultSet rs = sql.query(q);
			while(rs.next()) {
				ret.add(Item.loadFrom(rs));
			}
		} catch(SQLException e) {
			return null;
		}
		return ret;
	}
	
	static public Item loadFrom(ResultSet rs) throws SQLException {
		Item ret = null;
		int bid = rs.getInt("id");
		int amount = rs.getInt("amount");
		short damage = rs.getShort("damage");
		int enchantment = rs.getInt("enchantment");
		ret = new Item(bid, amount, damage, enchantment);
		return ret;
	}
	
	static public ArrayList<Item> parseToItems(CommandSender sender, String str) throws IllegalArgumentException {
		ArrayList<Item> ret = new ArrayList<Item>();
		String[] items = new String[1];
		if(str.indexOf(',') != -1)
			items = str.split(",");
		else
			items[0] = str;
		for(String item : items) {
			int bid = 0;
			int qua = 0;
			short dam = 0;
			int enc = 0;
			String[] args = item.split(":");
			int argc = args.length;
			switch(argc) {
			case 4:
				try {
					enc = Integer.parseInt(args[3]);
				} catch(NumberFormatException e) {
					String msg = ChatColor.RED + "Enchantment" + ChatColor.GREEN + " is not a number (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
			case 3:
				try {
					dam = Short.parseShort(args[2]);
				} catch(NumberFormatException e) {
					String msg = ChatColor.RED + "Damage" + ChatColor.GREEN + " is not a number (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				if(dam < 0) {
					String msg = ChatColor.RED + "Damage" + ChatColor.GREEN + " can not be less than 0 (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				if(dam > 32767) {
					String msg = ChatColor.RED + "Damage" + ChatColor.GREEN + " must be less than 32767 (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
			case 2:
				try {
					qua = Integer.parseInt(args[1]);
				} catch(NumberFormatException e) {
					String msg = ChatColor.RED + "Quantity" + ChatColor.GREEN + " is not a number (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				if(qua < 1) {
					String msg = ChatColor.RED + "Quantity" + ChatColor.GREEN + " has to be greater than 0 (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				if(qua > 64) {
					String msg = ChatColor.RED + "Quantity" + ChatColor.GREEN + " should be less than 64 (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				try {
					bid = Integer.parseInt(args[0]);
				} catch(NumberFormatException e) {
					String msg = ChatColor.RED + "Item Code" + ChatColor.GREEN + " is not a number (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				if(bid < 1) {
					String msg = ChatColor.RED + "Item Code" + ChatColor.GREEN + " must be greater than 0 (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				if(bid > 386) {
					String msg = ChatColor.RED + "Item Code" + ChatColor.GREEN + " must be less than 386 (" + str + ")";
					throw new IllegalArgumentException(msg);
				}
				break;
			default:
				throw(new IllegalArgumentException("Items must have between 2 and 4 parameters. ID:Quantity(:Damage:Enchantment)"));
			}
			if(enc > 0) {
				ItemStack test = new ItemStack(bid, qua);
				Enchantment teste = Enchantment.getById(enc);
				if(teste == null || !teste.canEnchantItem(test)) {
					String msg = "Item #" + bid + " cannot accept enchatment #" + enc;
					throw new IllegalArgumentException(msg);
				}
			}
			ret.add(new Item(bid, qua, dam, enc));
		}
		return ret;
	}
}
