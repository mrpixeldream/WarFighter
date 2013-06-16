package de.mrpixeldream.bukkit.tdm.cmd;

import de.mrpixeldream.bukkit.tdm.WarFighter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CommandTeam implements CommandExecutor {
	private final String PREFIX = ChatColor.DARK_PURPLE + "[WarFighter] ";
	@SuppressWarnings("unused")
	private WarFighter plugin;

	public CommandTeam(WarFighter plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((label.equalsIgnoreCase("team"))) {
			if (checkPerms(sender)) {
				if (checkArgs(args)) {
					if (args[0].equalsIgnoreCase("blue")) {
						if (WarFighter.isDeatmatch) {
							sender.sendMessage(this.PREFIX
									+ ChatColor.RED
									+ "There is a Deathmatch running! No teams!");
						} else {
							sender.sendMessage(this.PREFIX + ChatColor.GOLD
									+ "Members of the " + ChatColor.BLUE
									+ "blue " + ChatColor.GOLD + "team:");

							for (Entity now : WarFighter.blue) {
								Player p = (Player) now;
								sender.sendMessage(this.PREFIX + ChatColor.GOLD
										+ "- " + ChatColor.BLUE + p.getName());
							}
						}
					} else if (args[0].equalsIgnoreCase("red")) {
						if (WarFighter.isDeatmatch) {
							sender.sendMessage(this.PREFIX
									+ ChatColor.RED
									+ "There is a Deathmatch running! No teams!");
						} else {
							sender.sendMessage(this.PREFIX + ChatColor.GOLD
									+ "Members of the " + ChatColor.RED
									+ "red " + ChatColor.GOLD + "team:");

							for (Entity now : WarFighter.red) {
								Player p = (Player) now;
								sender.sendMessage(this.PREFIX + ChatColor.GOLD
										+ "- " + ChatColor.RED + p.getName());
							}
						}
					} else {
						sender.sendMessage(this.PREFIX + ChatColor.RED
								+ "Color can be only 'blue' or 'red'!");
					}
				} else {
					sender.sendMessage(this.PREFIX + ChatColor.RED
							+ "Usage: /team [color]");
				}
			} else {
				sender.sendMessage(this.PREFIX + ChatColor.RED
						+ "You don't have permission!");
			}
		}
		return true;
	}

	private boolean checkPerms(CommandSender sender) {
		return sender.hasPermission("wf.teams");
	}

	private boolean checkArgs(String[] args) {
		return args.length == 1;
	}
}