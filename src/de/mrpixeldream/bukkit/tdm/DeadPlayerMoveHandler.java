package de.mrpixeldream.bukkit.tdm;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DeadPlayerMoveHandler implements Listener {
	private static final String BEGIN_MSG = ChatColor.DARK_PURPLE
			+ "[WarFighter] " + ChatColor.RED + "There are ";

	// private static final String END_MSG =
	// " seconds left until your respawn time is over!";

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (WarFighter.dead != null) {
			for (Entity now : WarFighter.dead) {
				int index = WarFighter.dead.indexOf(now);
				int left = WarFighter.respawn_time
						- ((Integer) WarFighter.respawn_left.get(index))
								.intValue();

				if (e.getPlayer() == now) {
					e.getPlayer()
							.sendMessage(
									BEGIN_MSG
											+ left
											+ " seconds left until your respawn time is over!");
					e.getPlayer().teleport(e.getFrom());
					e.setCancelled(true);
				}
			}
		}
	}
}