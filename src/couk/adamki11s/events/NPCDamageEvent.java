package couk.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;

public class NPCDamageEvent implements Listener{
	
	final NPCHandler handle;
	
	public NPCDamageEvent(Plugin p, NPCHandler handle){
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.handle = handle;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onNPCDamage(final EntityDamageByEntityEvent evt){
		if(evt.getDamager() instanceof Player && handle.getNPCManager().isNPC(evt.getEntity())){
			//Player attacking NPC
			Player damager = (Player) evt.getDamager();
			SimpleNPC attacked = handle.getSimpleNPCByEntity(evt.getEntity());
			if(attacked != null && attacked.isAttackable()){
				damager.sendMessage("You did " + evt.getDamage() + " damage to NPC " + attacked.getName());
				attacked.getHumanNPC().actAsHurt();
				attacked.damageNPC(damager, evt.getDamage());
			} else {
				damager.sendMessage(attacked.getName() + " can not be harmed.");
				evt.setCancelled(true);
			}
		}
	}

}
