package de.mrpixeldream.bukkit.tdm.cmd;

import de.mrpixeldream.bukkit.tdm.WarFighter;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class CommandStartMatch implements CommandExecutor {
	private WarFighter plugin;
	private final String PREFIX = ChatColor.DARK_PURPLE + "[WarFighter] ";

	public CommandStartMatch(WarFighter plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (label.equalsIgnoreCase("startmatch")) {
			if (WarFighter.unlimitedTime == true) {
				// Command if the matches have unlimited time
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("tdm")) {
						if (sender.hasPermission("wf.start.tdm")) {
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ "Starting new match of type \"Team-Deathmatch\"!");
							EntityDamageEvent.getHandlerList().unregister(
									WarFighter.ff_handler);
							this.plugin
							.getServer()
							.getPluginManager()
							.registerEvents(WarFighter.ff_handler,
									this.plugin);

							WarFighter.dead = new ArrayList<Entity>();
							WarFighter.deaths = new HashMap<Player, Integer>();
							WarFighter.respawn_left = new ArrayList<Integer>();
							WarFighter.scores = new HashMap<Player, Integer>();

							WarFighter.isDeatmatch = false;

							this.plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_GREEN
									+ "New match started!");
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ WarFighter.timeLimit
									+ " minutes left in the \"Deathmatch\"!");
						} else {
							sender.sendMessage(this.PREFIX
									+ "You don't have permission!");
						}
					} else if (args[0].equalsIgnoreCase("dm")) {
						if (sender.hasPermission("wf.start.dm")) {
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ "Starting new match of type \"Deathmatch\"!");
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ WarFighter.time
									/ 60
									+ " minutes left in the \"Deathmatch\"!");
							EntityDamageEvent.getHandlerList().unregister(
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

							WarFighter.isDeatmatch = true;

							this.plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_GREEN
									+ "New match started!");
						}
					} else {
						sender.sendMessage(this.PREFIX + ChatColor.RED
								+ "Known match types: dm, tdm");
					}
				}
			} else {
				// command if matches have a set time limit
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("tdm")) {
						if (sender.hasPermission("wf.start.tdm")) {
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ "Starting new match of type \"Team-Deathmatch\"!");
							EntityDamageEvent.getHandlerList().unregister(
									WarFighter.ff_handler);
							this.plugin
							.getServer()
							.getPluginManager()
							.registerEvents(WarFighter.ff_handler,
									this.plugin);

							WarFighter.dead = new ArrayList<Entity>();
							WarFighter.deaths = new HashMap<Player, Integer>();
							WarFighter.respawn_left = new ArrayList<Integer>();
							WarFighter.scores = new HashMap<Player, Integer>();

							WarFighter.isDeatmatch = false;
							WarFighter.isTeamDeathMatch = true;

							this.plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_GREEN
									+ "New match started!");
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ WarFighter.timeLimit
									+ " minutes left in the \"Deathmatch\"!");
						} else {
							sender.sendMessage(this.PREFIX
									+ "You don't have permission!");
						}
					} else if (args[0].equalsIgnoreCase("dm")) {
						if (sender.hasPermission("wf.start.dm")) {
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ "Starting new match of type \"Deathmatch\"!");
							this.plugin
							.getServer()
							.broadcastMessage(
									this.PREFIX
									+ WarFighter.time
									/ 60
									+ " minutes left in the \"Deathmatch\"!");
							EntityDamageEvent.getHandlerList().unregister(
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

							WarFighter.isDeatmatch = true;
							WarFighter.isTeamDeathMatch = false;

							this.plugin.getServer().broadcastMessage(
									this.PREFIX + ChatColor.DARK_GREEN
									+ "New match started!");
						}
					} else {
						sender.sendMessage(this.PREFIX + ChatColor.RED
								+ "Known match types: dm, tdm");
					}
				}
			}

		}
		return true;
	}
}