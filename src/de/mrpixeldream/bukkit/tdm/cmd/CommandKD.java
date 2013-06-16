package de.mrpixeldream.bukkit.tdm.cmd;

import de.mrpixeldream.bukkit.tdm.WarFighter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKD implements CommandExecutor {
	private WarFighter plugin;
	private final String PREFIX = ChatColor.DARK_PURPLE + "[WarFighter] ";

	private int kills;
	private int deaths;

	public CommandKD(WarFighter plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((cmd.getName().equalsIgnoreCase("kd"))
				|| (label.equalsIgnoreCase("ratio"))) {
			if (checkPerms(sender)) {
				if (checkArgs(args)) {
					if (args.length == 1) {
						if (((Integer) WarFighter.scores
								.get(this.plugin.getServer().getPlayer(args[1])))
								.intValue() != 0 || ((Integer) WarFighter.deaths
										.get(this.plugin.getServer().getPlayer(args[1])))
										.intValue() != 0){
							
							@SuppressWarnings("unused")
							int kills = ((Integer) WarFighter.scores
									.get(this.plugin.getServer().getPlayer(args[1])))
									.intValue();
							@SuppressWarnings("unused")
							int deaths = ((Integer) WarFighter.deaths
									.get(this.plugin.getServer().getPlayer(args[1])))
									.intValue();

							
						}
						
						if (deaths > 0) {
							float kd = kills / deaths;

							sender.sendMessage(this.PREFIX + ChatColor.GOLD
									+ "[]--- K/D of player " + ChatColor.AQUA
									+ args[0] + ChatColor.GOLD + " ---[]");
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Kills: " + ChatColor.DARK_AQUA + kills);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Deaths: " + ChatColor.DARK_AQUA + deaths);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "K/D: " + ChatColor.DARK_AQUA + kd);
						} else {
							sender.sendMessage(this.PREFIX + ChatColor.GOLD
									+ "[]--- K/D of player " + ChatColor.AQUA
									+ args[0] + ChatColor.GOLD + " ---[]");
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Kills: " + ChatColor.DARK_AQUA + kills);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Deaths: " + ChatColor.DARK_AQUA + 0);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "K/D: " + ChatColor.DARK_AQUA + kills
									+ ".0");
						}
					} else {

						if (deaths != 0) {

							int kills = ((Integer) WarFighter.scores
									.get((Player) sender)).intValue();
							int deaths = ((Integer) WarFighter.deaths
									.get((Player) sender)).intValue();

							float kd = kills / deaths;

							sender.sendMessage(this.PREFIX + ChatColor.GOLD
									+ "[]--- K/D of player " + ChatColor.AQUA
									+ sender.getName() + ChatColor.GOLD
									+ " ---[]");
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Kills: " + ChatColor.DARK_AQUA + kills);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Deaths: " + ChatColor.DARK_AQUA + deaths);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "K/D: " + ChatColor.DARK_AQUA + kd);
						} else {
							sender.sendMessage(this.PREFIX + ChatColor.GOLD
									+ "[]--- K/D of player " + ChatColor.AQUA
									+ sender.getName() + ChatColor.GOLD
									+ " ---[]");
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Kills: " + ChatColor.DARK_AQUA + kills);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "Deaths: " + ChatColor.DARK_AQUA + 0);
							sender.sendMessage(this.PREFIX + ChatColor.GREEN
									+ "K/D: " + ChatColor.DARK_AQUA + kills
									+ ".0");
						}
					}
				} else {
					sender.sendMessage(this.PREFIX + ChatColor.RED
							+ "Usage: /kd [player] or /ratio [player]");
				}
			} else {
				sender.sendMessage(this.PREFIX + ChatColor.RED
						+ "You don't have permission!");
			}

		}

		return true;
	}

	private boolean checkPerms(CommandSender sender) {
		return sender.hasPermission("wf.kd");
	}

	private boolean checkArgs(String[] args) {
		if ((args.length == 0) || (args.length == 1)) {
			return true;
		}

		return false;
	}
}