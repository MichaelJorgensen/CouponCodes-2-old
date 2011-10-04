package com.lala.CouponCodes;

import java.util.ArrayList;

public class Coupon {

	private String name;
	private int usetimesleft;
	private ArrayList<Integer> itemids = new ArrayList<Integer>();
	private int amount;
	
	public Coupon(String name, int usetimesleft, ArrayList<Integer> itemids, int amount) {
		this.name = name;
		this.usetimesleft = usetimesleft;
		this.itemids = itemids;
		this.amount = amount;
	}
	
	/**
	 * Returns the name of the coupon
	 * @return String name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the amount of times the coupon can be used
	 * @return int usetimesleft
	 */
	public int getUseTimesLeft(){
		return this.usetimesleft;
	}
	
	/**
	 * Returns an ArrayList of the item IDs that the coupon will reward
	 * @return ArrayList<Integer> itemids
	 */
	public ArrayList<Integer> getItemIDs(){
		return this.itemids;
	}
	
	/**
	 * Returns the int amount of the prize to be rewareded
	 * @return int amount
	 */
	public int getAmount(){
		return this.amount;
	}
}
