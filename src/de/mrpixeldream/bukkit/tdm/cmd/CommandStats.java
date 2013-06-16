package de.mrpixeldream.bukkit.tdm.cmd;

import de.mrpixeldream.bukkit.tdm.WarFighter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CommandStats implements CommandExecutor {
	private final String PREFIX = ChatColor.DARK_PURPLE + "[WarFighter] ";

	private WarFighter plugin;

	public CommandStats(WarFighter plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((label.equalsIgnoreCase("stats"))) {
			if (WarFighter.isDeatmatch) {
				sender.sendMessage(this.PREFIX + ChatColor.RED
						+ "There is a Deathmatch running! No teams!");

			}

			String best_blue = "Nobody";
			String best_red = "Nobody";

			float kd_blue = 0.0F;
			float kd_red = 0.0F;

			float score_best = 0.0F;

			for (Entity now : WarFighter.blue) {
				Player p = (Player) now;
				float tmp;
				if (WarFighter.deaths.get(now).intValue() != 0) {
					tmp = WarFighter.scores.get(now).intValue()
							/ WarFighter.deaths.get(now).intValue();
				} else {
					tmp = Float.parseFloat(WarFighter.scores.get(now) + ".0");
				}

				if (tmp > score_best) {
					score_best = tmp;
					tmp = -1.0F;

					best_blue = p.getName();
				}
			}

			kd_blue = score_best;

			for (Entity now : WarFighter.red) {
				Player p = (Player) now;
				float tmp;
				if (WarFighter.deaths.get(now).intValue() != 0) {
					tmp = WarFighter.scores.get(now).intValue()
							/ WarFighter.deaths.get(now).intValue();
				} else {
					tmp = Float.parseFloat(WarFighter.scores.get(now) + ".0");
				}

				if (tmp > score_best) {
					score_best = tmp;
					tmp = -1.0F;

					best_red = p.getName();
				}
			}

			kd_red = score_best;

			sender.sendMessage(this.PREFIX + ChatColor.GOLD
					+ "[]--- Stats of the teams: ---[]");
			sender.sendMessage("");
			sender.sendMessage(this.PREFIX + ChatColor.RED + "Red team:");
			sender.sendMessage(this.PREFIX + ChatColor.GREEN + "Total kills: "
					+ ChatColor.AQUA + plugin.redScore);
			sender.sendMessage(this.PREFIX + ChatColor.GREEN + "Best player: "
					+ ChatColor.AQUA + best_red + ChatColor.GOLD + " (K/D: "
					+ kd_red + ")");
			sender.sendMessage("");
			sender.sendMessage(this.PREFIX + ChatColor.BLUE + "Blue team:");
			sender.sendMessage(this.PREFIX + ChatColor.GREEN + "Total kills: "
					+ ChatColor.AQUA + plugin.blueScore);
			sender.sendMessage(this.PREFIX + ChatColor.GREEN + "Best player: "
					+ ChatColor.AQUA + best_blue + ChatColor.GOLD + " (K/D: "
					+ kd_blue + ")");
		}
		return true;
	}

	public boolean checkPerms(CommandSender sender) {
		return sender.hasPermission("wf.teams");
	}

	public boolean checkArgs(String[] args) {
		return args.length == 0;
	}
}