package net.lala.CouponCodes.runnable;

import java.io.IOException;
import java.sql.SQLException;

import net.lala.CouponCodes.CouponCodes;
import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.misc.Metrics;
import net.lala.CouponCodes.sql.SQL;
import net.lala.CouponCodes.sql.options.MySQLOptions;
import net.lala.CouponCodes.sql.options.SQLiteOptions;

public class CustomDataSender implements Runnable {

	private CouponCodes plugin;
	private Metrics mt;
	private CouponManager cm;
	private SQL m_sql;
	
	public CustomDataSender(CouponCodes plugin, Metrics mt) {
		this.plugin = plugin;
		this.mt = mt;
		this.cm = CouponCodes.getCouponManager();
		m_sql = cm.getSQL();
	}
	
	@Override
	public void run() {
		plugin.debug("Beginning Custom data sending");
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return Coupon.getCount(m_sql);
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
					return Coupon.getCount(m_sql, Coupon.ITEMS);
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
					return Coupon.getCount(m_sql, Coupon.ECONOMY);
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
					return Coupon.getCount(m_sql, Coupon.RANK);
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
					return Coupon.getCount(m_sql, Coupon.XP);
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
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return Coupon.getCount(m_sql, Coupon.MULTI);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Multi Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return Coupon.getCount(m_sql, Coupon.WARP);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Warp Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				try {
					return Coupon.getCount(m_sql, Coupon.BAD);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
			@Override
			public String getColumnName() {
				return "Bad Coupons";
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				if (cm.getSQL().getDatabaseOptions() instanceof MySQLOptions)
					return 1;
				else
					return 0;
			}
			
			@Override
			public String getColumnName() {
				return ("MySQL Users");
			}
		});
		
		mt.addCustomData(plugin, new Metrics.Plotter() {
			
			@Override
			public int getValue() {
				if (cm.getSQL().getDatabaseOptions() instanceof SQLiteOptions)
					return 1;
				else
					return 0;
			}
			
			@Override
			public String getColumnName() {
				return ("SQLite Users");
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
