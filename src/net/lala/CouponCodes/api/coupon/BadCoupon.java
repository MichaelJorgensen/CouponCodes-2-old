package net.lala.CouponCodes.api.coupon;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.lala.CouponCodes.api.CouponManager;
import net.lala.CouponCodes.misc.CommandUsage;
import net.lala.CouponCodes.misc.Misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BanCommand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BadCoupon extends Coupon {

	static final int BURN = 1;
	static final int EXPLODE = 2;
	static final int CHICKEN = 3;
	static final int LIGHTNING = 4;
	static final int POISON = 5;
	static final int DROP = 6;
	static final int KICK = 7;
	static final int BAN = 8;
	
	public BadCoupon(String code, int value, int active, int totaluses, long expire) {
		this(0, code, value, active, totaluses, expire);
	}
	
	public BadCoupon(int id, String code, int value, int active, int totaluses, long expire) {
		super(id, code, Coupon.BAD, value, active, totaluses, expire);
		// TODO Auto-generated constructor stub
	}

	public BadCoupon(ResultSet rs) throws SQLException {
		super(rs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String effectText() {
		return "Bad";
	}

	@Override
	public void doEffect(Player player) {
		World w = null;
		Location pl = null;
		switch(getValue()) {
		case BURN:
			player.sendMessage(badMessage(BURN));
			player.setFireTicks(400);
			return;
		case EXPLODE:
			player.sendMessage(badMessage(EXPLODE));
			w = player.getWorld();
			w.createExplosion(player.getLocation(), 4F);
			return;
		case CHICKEN:
			player.sendMessage(badMessage(CHICKEN));
			w = player.getWorld();
			pl = player.getLocation();
			Location[] ns = new Location[8];
			ns[0] = new Location(w, pl.getX() - 1,	pl.getY(), pl.getZ() - 1);
			ns[1] = new Location(w, pl.getX() - 1,	pl.getY(), pl.getZ());
			ns[2] = new Location(w, pl.getX(),		pl.getY(), pl.getZ() - 1);
			ns[3] = new Location(w, pl.getX() + 1,	pl.getY(), pl.getZ() + 1);
			ns[4] = new Location(w, pl.getX() + 1,	pl.getY(), pl.getZ());
			ns[5] = new Location(w, pl.getX(),		pl.getY(), pl.getZ() + 1);
			ns[6] = new Location(w, pl.getX() - 1,	pl.getY() + 1, pl.getZ() + 1);
			ns[7] = new Location(w, pl.getX() + 1,	pl.getY(), pl.getZ() - 1);
			for(Location l : ns)
			w.spawnCreature(l, org.bukkit.entity.EntityType.CHICKEN);
			return;
		case LIGHTNING:
			player.sendMessage(badMessage(LIGHTNING));
			w = player.getWorld();
			pl = player.getLocation();
			w.strikeLightning(pl);
			return;
		case POISON:
			player.sendMessage(badMessage(POISON));
			player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(800, 2));
			player.addPotionEffect(PotionEffectType.POISON.createEffect(800, 2));
			player.addPotionEffect(PotionEffectType.SLOW.createEffect(800, 2));
			return;
		case DROP:
			player.sendMessage(badMessage(DROP));
			w = player.getWorld();
			pl = player.getLocation();
			player.teleport(new Location(w, pl.getX(), 1024, pl.getZ()));
			return;
		case KICK:
			player.kickPlayer(badMessage(KICK));
			return;
		case BAN:
			Bukkit.getOfflinePlayer(player.getName()).setBanned(true);
			player.kickPlayer(badMessage(BAN));
			return;
		}
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
				int bad = parseBad(args[3]);
				int active = 1;
				int totaluses = 1;
				long expire = 0;
				
				if (code.equalsIgnoreCase("random")) code = Misc.generateName();
				if (args.length >= 5) active = Integer.parseInt(args[4]);
				if (args.length >= 6) totaluses = Integer.parseInt(args[5]);
				if (args.length >= 7) expire = parseExpire(args[6]);
				
				if (args.length > 7) {
					sender.sendMessage(CommandUsage.C_ADD_BAD.toString());
					return;
				}
				
				BadCoupon bc = new BadCoupon(code, bad, active, totaluses, expire);
				if(bc.dbAdd() > 0) {
					api.getLogger().info(sender.getName() + " just added an bad code: " + code);
					sender.sendMessage(ChatColor.GREEN + "BadCoupon " + ChatColor.GOLD + code + ChatColor.GREEN+" has been added!");
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
			}
		} else {
			sender.sendMessage(CommandUsage.C_ADD_XP.toString());
			return;
		}
	}
	
	static public int parseBad(String bad) {
		if(bad.equalsIgnoreCase("burn"))
			return BURN;
		if(bad.equalsIgnoreCase("explode"))
			return EXPLODE;
		if(bad.equalsIgnoreCase("chicken"))
			return CHICKEN;
		if(bad.equalsIgnoreCase("lightning"))
			return LIGHTNING;
		if(bad.equalsIgnoreCase("poison"))
			return POISON;
		if(bad.equalsIgnoreCase("drop"))
			return DROP;
		if(bad.equalsIgnoreCase("kick"))
			return KICK;
		if(bad.equalsIgnoreCase("ban"))
			return BAN;
		return CHICKEN;
	}
	
	static public String badMessage(int bad) {
		switch(bad) {
		case BURN:
			return ChatColor.GREEN + "You're " + ChatColor.RED +"fired!";
		case EXPLODE:
			return ChatColor.GREEN + "You've been " + ChatColor.RED +"gibbed!";
		default:
		case CHICKEN:
			return ChatColor.GREEN + "You win: " + ChatColor.GOLD + "chickens!";
		case LIGHTNING:
			return ChatColor.GREEN + "The fury of " + ChatColor.GOLD + "Mighty Zeus" + ChatColor.GREEN + " crashes upon you!";
		case POISON:
			return ChatColor.GREEN + "Just relax and let the " + ChatColor.DARK_RED + "poison" + ChatColor.GREEN + " do its work...";
		case DROP:
			return ChatColor.GREEN + "Its not the fall that gets you, its the " + ChatColor.RED + "SUDDEN STOP" + ChatColor.GREEN + " at the end.";
		case KICK:
			return ChatColor.RED + "Redeeming random coupon codes can have ill effects.";
		case BAN:
			return ChatColor.RED + "Appeal the The Mighty BANHAMMER® at appeals@bestminecraftserverever.com.";
		}
	}
}
