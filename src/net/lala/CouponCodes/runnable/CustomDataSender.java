package net.lala.CouponCodes.runnable;

import java.io.IOException;
import java.sql.SQLException;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.Metrics;
import net.lala.CouponCodes.sql.SQL;

public class CustomDataSender implements Runnable {

	private CouponCodes plugin;
	private Metrics mt;
	private CouponManager cm;
	
	public CustomDataSender(CouponCodes plugin, Metrics mt) {
		this.plugin = plugin;
		this.mt = mt;
		cm = new CouponManager(plugin, new SQL(plugin, plugin.getDatabaseOptions()));
		try {
			cm.getSQL().open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		plugin.debug("Beginning Custom data sending");
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return cm.getCoupons().size();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Total Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return cm.getAmountOf("Item");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Item Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return cm.getAmountOf("Economy");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Economy Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return cm.getAmountOf("Rank");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Rank Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return cm.getAmountOf("Xp");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Xp Coupons";
			}
		});
		
		try {
			mt.beginMeasuringPlugin(plugin);
		} catch (IOException e) {
			plugin.sendErr("Could not measure plugin");
			e.printStackTrace();
		}
		
		plugin.debug("End of custom data sending");
	}
}
