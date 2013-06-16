package de.mrpixeldream.bukkit.tdm.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.mrpixeldream.bukkit.tdm.WarFighter;

public class CommandStopMatch implements CommandExecutor {
	private WarFighter plugin;
	private final String PREFIX = ChatColor.DARK_PURPLE + "[WarFighter] ";

	public CommandStopMatch(WarFighter plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if ((label.equalsIgnoreCase("stopmatch"))) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("tdm")) {
					if (sender.hasPermission("wf.stopmatch.tdm")) {
						this.plugin.getServer().broadcastMessage(
								this.PREFIX + "Stopping \"Deatmatch\"!");

						this.plugin.getServer().broadcastMessage(
								this.PREFIX + ChatColor.DARK_GREEN
								+ "Match has ended!");
						
						if (plugin.redScore > plugin.blueScore) {

							plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_RED
									+ "Match over!");
							plugin.getServer().broadcastMessage(
									this.PREFIX
									+ ChatColor.RED
									+ "Red team won! ("
									+ plugin.getConfig()
									.getInt("General.duration")
									+ " points)");
							plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.BLUE + "Blue team got "
											+ plugin.blueScore + " points!");

							plugin.blueScore = 0;
							plugin.redScore = 0;

							for (Player now : plugin.getServer().getOnlinePlayers()) {
								now.setHealth(20);
								now.sendMessage(this.PREFIX
										+ ChatColor.AQUA
										+ "You have been healed to be prepared for next match!");
							}
						}

						if (plugin.blueScore > plugin.redScore) {
							plugin.blueScore = 0;
							plugin.redScore = 0;

							plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_RED
									+ "Match over!");
							plugin.getServer().broadcastMessage(
									this.PREFIX
									+ ChatColor.BLUE
									+ "Blue team won! ("
									+ plugin.getConfig()
									.getInt("General.duration")
									+ " points)");
							plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.RED + "Red team got "
											+ plugin.blueScore + " points!");

							for (Player now : plugin.getServer().getOnlinePlayers()) {
								now.setHealth(20);
								now.sendMessage(this.PREFIX
										+ ChatColor.AQUA
										+ "You have been healed to be prepared for next match!");
							}
						}

						else {
							plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_RED
									+ "Match over!");
							plugin.getServer().broadcastMessage(
									this.PREFIX
									+ ChatColor.RED
									+ "Game tied!"
									+ plugin.getConfig()
									.getInt("General.duration")
									+ " points)");

							plugin.blueScore = 0;
							plugin.redScore = 0;

							for (Player now : plugin.getServer().getOnlinePlayers()) {
								now.setHealth(20);
								now.sendMessage(this.PREFIX
										+ ChatColor.AQUA
										+ "You have been healed to be prepared for next match!");
							}
						}
						
						WarFighter.isTeamDeathMatch = false;

					} else {
						sender.sendMessage(this.PREFIX
								+ "You don't have permission!");
					}
				} else if (args[0].equalsIgnoreCase("dm")) {
					if (sender.hasPermission("wf.stop.dm")) {
						this.plugin.getServer().broadcastMessage(
								this.PREFIX + "Stopping \"Deatmatch\"!");
						EntityDamageByEntityEvent.getHandlerList().unregister(
								WarFighter.ff_handler);

						this.plugin
						.getServer()
						.getPluginManager()
						.registerEvents(WarFighter.dm_handler,
								this.plugin);

						WarFighter.dead = new ArrayList<Entity>();
						WarFighter.deaths = new HashMap<Player, Integer>();
						WarFighter.respawn_left = new ArrayList<Integer>();
						WarFighter.scores = new HashMap<Player, Integer>();

						WarFighter.isDeatmatch = false;

						this.plugin.getServer().broadcastMessage(
								this.PREFIX + ChatColor.DARK_GREEN
								+ "Match has ended!");
					}
				} else {
					sender.sendMessage(this.PREFIX + ChatColor.RED
							+ "Known match types: dm, tdm");
				}
			}
		}
		return true;
	}
}
