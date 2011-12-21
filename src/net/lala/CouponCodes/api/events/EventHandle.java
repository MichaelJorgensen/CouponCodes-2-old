package net.lala.CouponCodes.api.events;

import java.sql.Connection;
import java.sql.ResultSet;

import net.lala.CouponCodes.api.coupon.Coupon;
import net.lala.CouponCodes.api.events.coupon.CouponAddToDatabaseEvent;
import net.lala.CouponCodes.api.events.coupon.CouponCreateEvent;
import net.lala.CouponCodes.api.events.coupon.CouponRemoveFromDatabaseEvent;
import net.lala.CouponCodes.api.events.database.DatabaseCloseConnectionEvent;
import net.lala.CouponCodes.api.events.database.DatabaseOpenConnectionEvent;
import net.lala.CouponCodes.api.events.database.DatabaseQueryEvent;
import net.lala.CouponCodes.sql.DatabaseOptions;

public class EventHandle {

	public static void callCouponAddToDatabaseEvent(Coupon coupon) {
		CouponAddToDatabaseEvent ev = new CouponAddToDatabaseEvent(coupon);
		ev.call();
	}
	
	public static void callCouponRemoveFromDatabaseEvent(Coupon coupon) {
		CouponRemoveFromDatabaseEvent ev = new CouponRemoveFromDatabaseEvent(coupon);
		ev.call();
	}
	
	public static void callCouponCreateEvent(Coupon coupon) {
		CouponCreateEvent ev = new CouponCreateEvent(coupon);
		ev.call();
	}
	
	public static void callDatabaseOpenConnectionEvent(Connection con, DatabaseOptions dop, Boolean success) {
		DatabaseOpenConnectionEvent ev = new DatabaseOpenConnectionEvent(con, dop, success);
		ev.call();
	}
	
	public static void callDatabaseCloseConnectionEvent(Connection con, DatabaseOptions dop) {
		DatabaseCloseConnectionEvent ev = new DatabaseCloseConnectionEvent(con, dop);
		ev.call();
	}
	
	public static void callDatabaseQueryEvent(DatabaseOptions dop, String query, ResultSet rs) {
		DatabaseQueryEvent ev = new DatabaseQueryEvent(dop, query, rs);
		ev.call();
	}
}
