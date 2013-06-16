package de.mrpixeldream.bukkit.tdm;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DeathmatchHandler implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerHit(EntityDamageByEntityEvent e) {
		e.setDamage(e.getDamage());
		e.setCancelled(true);
	}
}