package de.mrpixeldream.bukkit.tdm;

import java.util.Iterator;
import org.bukkit.entity.Entity;

public class DeadPlayerRespawnHandler
  implements Runnable
{
  public void run()
  {
    Iterator<?> localIterator = WarFighter.dead.iterator(); if (localIterator.hasNext()) { Entity now = (Entity)localIterator.next();

      int index = WarFighter.dead.indexOf(now);

      int time = ((Integer)WarFighter.respawn_left.get(index)).intValue();
      time++;

      if (time < WarFighter.respawn_time)
      {
        WarFighter.respawn_left.set(index, Integer.valueOf(time));
        return;
      }

      WarFighter.respawn_left.remove(index);
      WarFighter.dead.remove(index);
      return;
    }
  }
}