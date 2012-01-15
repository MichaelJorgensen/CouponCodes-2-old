package net.lala.CouponCodes.listeners;

import net.lala.CouponCodes.CouponCodes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerListen extends PlayerListener {

	private CouponCodes plugin;
	
	public PlayerListen(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.has(player, "cc.update")) {
			if (plugin.checkForUpdate()) {
				player.sendMessage(ChatColor.GREEN+"There is a new update for CouponCodes! Please download it at http://dev.bukkit.org/server-mods/couponcodes/");
			}
		}
	}
}
