package net.lala.CouponCodes.misc;

import java.sql.SQLException;

import net.lala.CouponCodes.CouponCodes;

public class ReconnectTimer implements Runnable {

	private CouponCodes plugin;
	
	public ReconnectTimer(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		try {
			plugin.getSQLAPI().reload();
			plugin.debug("SQL reload successful");
		} catch (SQLException e) {
			plugin.debug("SQL reload unsuccessful");
		}
	}
}
