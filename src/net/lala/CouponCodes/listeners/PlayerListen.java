package net.lala.CouponCodes.listeners;

import net.lala.CouponCodes.CouponCodes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListen implements Listener {

	private CouponCodes plugin;
	
	public PlayerListen(CouponCodes plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.has(player, "cc.update")) {
			if (plugin.checkForUpdate()) {
				String[] info = plugin.getUpdateInfo();
				player.sendMessage(ChatColor.GREEN+"There is a new update for CouponCodes! Current version: "+plugin.getDescription().getVersion()+" New version: "+info[0]);
				player.sendMessage(ChatColor.GOLD+"About the update: "+info[1]);
			}
		}
	}
}
