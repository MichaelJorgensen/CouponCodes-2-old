package net.lala.CouponCodes;

import java.util.ArrayList;

public class Coupon {

	private String name;
	private int usetimesleft;
	private ArrayList<Integer> itemids;
	private int amount;
	private ArrayList<String> usedplayers;
	
	public Coupon(String name, int usetimesleft, ArrayList<Integer> ids, ArrayList<String> names, int prizeamount) {
		this.name = name;
		this.usetimesleft = usetimesleft;
		this.itemids = ids;
		this.usedplayers = names;
		this.amount = prizeamount;
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
	 * Returns an ArrayList of all the players that have used this coupon
	 * @return ArrayList<String> usedplayers
	 */
	public ArrayList<String> getUsedPlayers(){
		return this.usedplayers;
	}
	
	/**
	 * Returns the int amount of the prize to be rewareded
	 * @return int amount
	 */
	public int getAmount(){
		return this.amount;
	}
	
	/**
	 * Sets the name of the coupon
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
		//TODO: update database with new name
	}
	
	/**
	 * Sets the amount of times the couopn can be used
	 * @param usetimesleft
	 */
	public void setUseTimesLeft(int usetimesleft){
		this.usetimesleft = usetimesleft;
		//TODO: update database with new value
	}
	
	/**
	 * Sets the ID values for the prize
	 * @param ids
	 */
	public void setItemIDs(ArrayList<Integer> ids){
		this.itemids = ids;
		//TODO: update database with new value
	}
	
	/**
	 * Turns the Arraylist of Item IDs into a String separated by commans
	 * Example: 76,46,50
	 * @return String
	 */
	public String FormatIDsToDbString(){
		StringBuilder sb = new StringBuilder();
		for (int i = itemids.size(); i > 0; i--){
			sb.append(itemids.get(i-1));
			sb.append(",");
		}
		return sb.toString();
	}
	
	public String toString(){
		return this.getName()+"::"+this.getAmount();
	}
}
