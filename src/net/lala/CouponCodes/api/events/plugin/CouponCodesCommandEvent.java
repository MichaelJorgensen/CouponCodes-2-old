package net.lala.CouponCodes.api.events.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

/**
 * CouponCodesCommandEvent.java - Extension of event used when a command is send through coupon codes.
 * @author mike101102
 */
@SuppressWarnings("serial")
public class CouponCodesCommandEvent extends Event {

	private CommandSender sender;
	private Command command;
	private String commandLabel;
	private String[] args;
	
	private Boolean cancel = false;
	
	public CouponCodesCommandEvent(CommandSender sender, Command command, String commandLabel, String[] args) {
		super("CouponCodesCommandEvent");
		this.sender = sender;
		this.command = command;
		this.commandLabel = commandLabel;
		this.args = args;
	}
	
	public Boolean isCancelled() {
		return cancel;
	}
	
	public void setCancelled(Boolean cancel) {
		this.cancel = cancel;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public void setSender(CommandSender sender) {
		this.sender = sender;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public void setCommand(Command command) {
		this.command = command;
	}
	
	public String getCommandLabel() {
		return commandLabel;
	}
	
	public void setCommandLabel(String commandLabel) {
		this.commandLabel = commandLabel;
	}
	
	public String[] getArgs() {
		return args;
	}
	
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	/**
	 * Calls the event
	 */
	public void call() {
		Bukkit.getServer().getPluginManager().callEvent(this);
	}
}
