package de.mrpixeldream.bukkit.tdm;

import de.mrpixeldream.bukkit.tdm.cmd.CommandKD;
import de.mrpixeldream.bukkit.tdm.cmd.CommandStartMatch;
import de.mrpixeldream.bukkit.tdm.cmd.CommandStats;
import de.mrpixeldream.bukkit.tdm.cmd.CommandStopMatch;
import de.mrpixeldream.bukkit.tdm.cmd.CommandTeam;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WarFighter extends JavaPlugin implements Listener {

	private final String PREFIX = ChatColor.DARK_PURPLE + "[WarFighter] ";
	public static LinkedList<Entity> red;
	public static LinkedList<Entity> blue;
	public static ArrayList<Entity> dead;
	public static ArrayList<Integer> respawn_left;
	public static HashMap<Player, Integer> scores;
	public static HashMap<Player, Integer> deaths;
	public int redScore;
	public int blueScore;
	public static int respawn_time;
	public static int timeLimit;
	public static int time;
	public static boolean unlimitedTime;
	public static boolean unlimitedScore;
	public static boolean isDeatmatch;
	public static boolean isTeamDeathMatch;
	public static final FriendlyFireHandler ff_handler = new FriendlyFireHandler();
	public static final DeadPlayerRespawnHandler deadrs_handler = new DeadPlayerRespawnHandler();
	public static final DeadPlayerMoveHandler deadmv_handler = new DeadPlayerMoveHandler();
	public static final DeathmatchHandler dm_handler = new DeathmatchHandler();

	/*
	 * private final CommandKD CMD_KD = new CommandKD(this); private final
	 * CommandRestart CMD_RESTART = new CommandRestart(this); private final
	 * CommandStats CMD_STATS = new CommandStats(); private final CommandTeam
	 * CMD_TEAM = new CommandTeam();
	 */

	public void onEnable() {
		System.out.println("[WarFighter] Enabling WarFighter plugin...");

		red = new LinkedList<Entity>();
		blue = new LinkedList<Entity>();

		dead = new ArrayList<Entity>();
		respawn_left = new ArrayList<Integer>();

		scores = new HashMap<Player, Integer>();
		deaths = new HashMap<Player, Integer>();

		isDeatmatch = false;

		System.out.println("[WarFighter] Loading config...");

		loadConfig();

		System.out
		.println("[WarFighter] Config successfully loaded! Setting respawn time...");

		respawn_time = getConfig().getInt("Respawn.time");

		System.out.println("[WarFighter] Setting Time Limit...");

		timeLimit = getConfig().getInt("General.time limit") / 60;

		time = getConfig().getInt("General.time limit");

		System.out.println("Loading Time Limit...");

		unlimitedTime = getConfig().getBoolean("General.unlimted time");

		System.out.println("Loading Time Preferences...");

		unlimitedScore = getConfig().getBoolean("General.unlimted score");

		System.out.println("Loading Score Preferences...");

		System.out.println(timeLimit + "minutes");

		System.out.println("[WarFighter] Registering events...");

		getCommand("kd").setExecutor(new CommandKD(this));
		System.out.println("[WarFighter] KD...");
		getCommand("ratio").setExecutor(new CommandKD(this));
		System.out.println("[WarFighter] ratio...");
		getCommand("startmatch").setExecutor(new CommandStartMatch(this));
		System.out.println("[WarFighter] startmatch...");
		getCommand("stopmatch").setExecutor(new CommandStopMatch(this));
		System.out.println("[WarFighter] stopmatch...");
		getCommand("stats").setExecutor(new CommandStats(this));
		System.out.println("[WarFighter] stats...");
		getCommand("team").setExecutor(new CommandTeam(this));
		System.out.println("[WarFighter] team...");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(deadmv_handler, this);

		if (!getConfig().getBoolean("General.friendly_fire")) {
			getServer().getPluginManager().registerEvents(ff_handler, this);
		}

		getServer().getScheduler().scheduleSyncRepeatingTask(this,
				deadrs_handler, 0L, 20L);

		System.out.println("[WarFighter] Events successfully registered!");
		System.out.println("[WarFighter] Thanks for using WarFighter!");
	}

	public void onDisable() {
		System.out.println("[WarFighter] Disabling WarFighter...");
		System.out.println("[WarFighter] Kicking all players...");

		for (Player now : getServer().getOnlinePlayers()) {
			now.kickPlayer(this.PREFIX + ChatColor.RED
					+ "Server reload/restart!" + ChatColor.AQUA
					+ " Please rejoin soon!");
		}

		System.out.println("[WarFighter] All players kicked! Ending match...");

		dead = null;
		red = null;
		blue = null;

		System.out.println("[WarFighter] WarFighter succesfully disabled!");
		System.out.println("[WarFighter] Thanks for using!");
	}

	public void loadConfig() {
		boolean configFileExistant = false;

		if (new File("plugins/WarFighter/").exists()) {
			configFileExistant = true;
		}

		getConfig().options().copyDefaults(true);

		if (!configFileExistant) {
			getConfig().addDefault("General.friendly_fire",
					Boolean.valueOf(false));
			getConfig().addDefault("General.unlimited time",
					Boolean.valueOf(false));
			getConfig().addDefault("General.unlimited score",
					Boolean.valueOf(false));
			getConfig().addDefault("General.score limit", Integer.valueOf(10));
			getConfig().addDefault("Respawn.time", Integer.valueOf(7));
			getConfig().addDefault("General.time limit", Integer.valueOf(600));
		}

		saveConfig();
		reloadConfig();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		String player_display_name = e.getPlayer().getDisplayName();

		if (red.size() > blue.size()) {
			e.setJoinMessage(this.PREFIX + ChatColor.BLUE
					+ e.getPlayer().getName() + ChatColor.GOLD
					+ " joined the game!");

			blue.add(e.getPlayer());
			e.getPlayer().sendMessage(
					this.PREFIX + ChatColor.BLUE + "You joined team blue!");
			e.getPlayer().getInventory()
			.setHelmet(new ItemStack(35, 1, (short) 11));

			e.getPlayer().setDisplayName(ChatColor.BLUE + player_display_name);
		} else {
			e.setJoinMessage(this.PREFIX + ChatColor.RED
					+ e.getPlayer().getName() + ChatColor.GOLD
					+ " joined the game!");

			red.add(e.getPlayer());
			e.getPlayer().sendMessage(
					this.PREFIX + ChatColor.RED + "You joined team red!");
			e.getPlayer().getInventory()
			.setHelmet(new ItemStack(35, 1, (short) 14));

			e.getPlayer().setDisplayName(ChatColor.RED + player_display_name);
		}

		scores.put(e.getPlayer(), Integer.valueOf(0));
		deaths.put(e.getPlayer(), Integer.valueOf(0));

		e.getPlayer().sendMessage(
				this.PREFIX + ChatColor.AQUA
				+ "This server is running WarFighter!");
		e.getPlayer().sendMessage(
				this.PREFIX + ChatColor.AQUA + "Have fun and good luck!");
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (red.contains(e.getPlayer())) {
			e.setQuitMessage(this.PREFIX + ChatColor.RED
					+ e.getPlayer().getName() + ChatColor.GOLD
					+ " left the game!");
		} else {
			e.setQuitMessage(this.PREFIX + ChatColor.BLUE
					+ e.getPlayer().getName() + ChatColor.GOLD
					+ " left the game!");
		}

		if (blue.contains(e.getPlayer())) {
			blue.remove(e.getPlayer());
		} else {
			red.remove(e.getPlayer());
		}

		scores.remove(e.getPlayer());
		deaths.remove(e.getPlayer());
	}

	@EventHandler
	public void onPlayerKilled(PlayerDeathEvent e) {
		Player killed = e.getEntity();
		String killed_name = killed.getName();

		dead.add(killed);
		respawn_left.add(Integer.valueOf(0));

		int player_score = ((Integer) scores.get(killed.getKiller()))
				.intValue();
		player_score++;
		scores.remove(killed.getKiller());
		scores.put(killed.getKiller(), Integer.valueOf(player_score));

		int player_deaths = ((Integer) deaths.get(killed)).intValue();
		player_deaths++;
		deaths.remove(killed);
		deaths.put(killed, Integer.valueOf(player_deaths));

		if (red.contains(killed.getKiller())) {
			redScore++;
			e.setDeathMessage(this.PREFIX + ChatColor.RED
					+ "Red team scored one point!");
			getServer().broadcastMessage(
					this.PREFIX + ChatColor.RED
					+ e.getEntity().getKiller().getName() + " killed "
					+ killed_name + " with "
					+ killed.getKiller().getItemInHand().getType());
		} else {
			blueScore++;
			e.setDeathMessage(this.PREFIX + ChatColor.BLUE
					+ "Blue team scored one point!");
			getServer().broadcastMessage(
					this.PREFIX + ChatColor.BLUE
					+ e.getEntity().getKiller().getName() + " killed "
					+ killed_name + " with "
					+ killed.getKiller().getItemInHand().getType());
		}

		e.getDrops().clear();
	}

	public void gameWin() {
		if(unlimitedTime == true && unlimitedScore == false){
			if(blueScore == getConfig().getInt("General.score limit") || redScore == getConfig().getInt("General.score limit")){
				if (redScore > blueScore) {

					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.RED
							+ "Red team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.RED + "Red team got "
									+ redScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
				else{
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.BLUE
							+ "Blue team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.BLUE + "Blue team got "
									+ blueScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
			}
		}
		
		if(unlimitedTime == false && unlimitedScore == true){
			if(time == 0){
				if (redScore > blueScore) {

					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.RED
							+ "Red team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.RED + "Red team got "
									+ redScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
				else{
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.BLUE
							+ "Blue team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.BLUE + "Blue team got "
									+ blueScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
			}
		}
		
		if(unlimitedTime == false && unlimitedScore == false){
			if(time == 0){
				if (redScore > blueScore) {

					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.RED
							+ "Red team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.RED + "Red team got "
									+ redScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
				else{
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.BLUE
							+ "Blue team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.BLUE + "Blue team got "
									+ blueScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
			}
			
			if(blueScore == getConfig().getInt("General.score limit") || redScore == getConfig().getInt("General.score limit")){
				if (redScore > blueScore) {

					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.RED
							+ "Red team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.RED + "Red team got "
									+ redScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
				else{
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.DARK_RED
							+ "Match over!");
					getServer().broadcastMessage(
							this.PREFIX
							+ ChatColor.BLUE
							+ "Blue team won! ("
							+ getConfig()
							.getInt("General.duration")
							+ " points)");
					getServer().broadcastMessage(
							this.PREFIX + ChatColor.BLUE + "Blue team got "
									+ blueScore + " points!");

					blueScore = 0;
					redScore = 0;

					for (Player now : getServer().getOnlinePlayers()) {
						now.setHealth(20);
						now.sendMessage(this.PREFIX
								+ ChatColor.AQUA
								+ "You have been healed to be prepared for next match!");
					}
				}
			}
			
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (red.contains(e.getPlayer())) {
			e.getPlayer().getInventory()
			.setHelmet(new ItemStack(35, 1, (short) 14));
		} else {
			e.getPlayer().getInventory()
			.setHelmet(new ItemStack(35, 1, (short) 11));
		}
	}
}