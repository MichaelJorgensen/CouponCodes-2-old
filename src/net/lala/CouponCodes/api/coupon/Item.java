package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.sql.SQL;

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
		return stack;
	}

	public int id() { return m_bid; }
	public int amount() { return m_amount; }
	public short damage() { return m_damage; }
	public int enchantment() { return m_enchantment; }
	
	static public ArrayList<Item> findItems(SQL sql, String code) {
		ArrayList<Item> ret = new ArrayList<Item>();
		String q = "SELECT items.* FROM items JOIN codes ON items.coupon_id=codes.id WHERE code='" + code + "'";
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
	
	static public ArrayList<Item> parseToItems(String str) {
		ArrayList<Item> ret = new ArrayList<Item>();
		try {
			String[] items = new String[1];
			if(str.indexOf(',') != -1)
				items = str.split(",");
			else
				items[0] = str;
			for(String item : items) {
				String[] args = item.split(":");
				int bid = 0;
				int qua = 0;
				short dam = 0;
				int enc = 0;
				int argc = args.length;
				switch(argc) {
				case 4:
					enc = Integer.parseInt(args[3]);
				case 3:
					dam = (short) Integer.parseInt(args[2]);
				case 2:
					qua = Integer.parseInt(args[1]);
					bid = Integer.parseInt(args[0]);
					break;
				default:
					throw(new Exception());
				}
				ret.add(new Item(bid, qua, dam, enc));
			}
		} catch(Exception e) {
			
		}
		return ret;
	}
}
