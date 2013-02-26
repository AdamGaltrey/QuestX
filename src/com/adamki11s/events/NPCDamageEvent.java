package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.questx.QuestX;

public class NPCDamageEvent implements Listener {

	final NPCHandler handle;

	public NPCDamageEvent(Plugin p, NPCHandler handle) {
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.handle = handle;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onNPCDamage(final EntityDamageByEntityEvent evt) {
		if (evt.getDamager() instanceof Player && handle.getNPCManager().isNPC(evt.getEntity())) {
			// Player attacking NPC

			if (evt.getDamager().getLocation().distance(evt.getEntity().getLocation()) < 3) {

				Player damager = (Player) evt.getDamager();
				SimpleNPC attacked = handle.getSimpleNPCByEntity(evt.getEntity());
				if (attacked != null && attacked.isAttackable()) {
					// QuestX.logChat(damager, "You did " + evt.getDamage() +
					// " damage to NPC " + attacked.getName());
					attacked.getHumanNPC().actAsHurt();
					attacked.damageNPC(damager, evt.getDamage());
				} else {
					QuestX.logChat(damager, attacked.getName() + LocaleBundle.getString("cannot_harm"));
					evt.setCancelled(true);
				}

			} else {
				evt.setCancelled(true);
			}

		}
	}

}
