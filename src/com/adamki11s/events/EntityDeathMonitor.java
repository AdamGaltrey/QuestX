package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.npcs.tasks.TaskManager;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.questx.QuestX;


public class EntityDeathMonitor implements Listener {

	public EntityDeathMonitor(Plugin main) {
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDeath(final EntityDeathEvent evt) {
		Entity e = evt.getEntity();
		if (!(e instanceof Player)) {
			if (e.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) e.getLastDamageCause();
				if (edbee.getDamager() instanceof Player) {
					Player p = (Player) edbee.getDamager();
					if(TaskRegister.doesPlayerHaveTask(p.getName())){
						TaskManager tm = TaskRegister.getTaskManager(p.getName());
						tm.trackEntityKill(e.getType());
					}
				}
			}
		}
	}

}
