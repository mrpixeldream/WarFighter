package de.mrpixeldream.bukkit.tdm;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFireHandler implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerHit(EntityDamageByEntityEvent e) {
		if (((e.getDamager() instanceof Player))
				&& ((e.getEntity() instanceof Player))) {
			if ((WarFighter.blue.contains(e.getDamager()))
					&& (WarFighter.blue.contains(e.getEntity()))) {
				Player dmg = (Player) e.getDamager();
				dmg.sendMessage(ChatColor.DARK_PURPLE + "[WarFighter] "
						+ ChatColor.RED + "You can't hurt your teammates!");
				e.setCancelled(true);
				return;
			}

			if ((WarFighter.red.contains(e.getDamager()))
					&& (WarFighter.red.contains(e.getEntity()))) {
				Player dmg = (Player) e.getDamager();
				dmg.sendMessage(ChatColor.DARK_PURPLE + "[WarFighter] "
						+ ChatColor.RED + "You can't hurt your teammates!");
				e.setCancelled(true);
				return;
			}

			if (WarFighter.dead.contains(e.getEntity())) {
				Player dmg = (Player) e.getDamager();
				dmg.sendMessage(ChatColor.DARK_PURPLE + "[WarFighter] "
						+ ChatColor.RED + "Do not spawn rape!");
				e.setCancelled(true);
				return;
			}

			return;
		}
	}
}