package net.lala.CouponCodes.api.coupon;

import java.util.ArrayList;

public class Item {
	private int m_bid;
	private int m_quantity;
	private int m_damage;
	private int m_enchantment;
	public Item(int item_bid, int quantity, int damage, int enchantment) {
		m_bid = item_bid;
		m_quantity = quantity;
		m_damage = damage;
		m_enchantment = enchantment;
	}
	
	public int id() { return m_bid; }
	public int quantity() { return m_quantity; }
	public int damage() { return m_damage; }
	public int enchantment() { return m_enchantment; }
	
	public static ArrayList<Item> parseToItems(String str) {
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
				int dam = 0;
				int enc = 0;
				int argc = args.length;
				switch(argc) {
				case 4:
					enc = Integer.parseInt(args[3]);
				case 3:
					dam = Integer.parseInt(args[2]);
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
