package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;


public class NPCInteractEvent implements Listener{
	
final NPCHandler handle;
	
	public NPCInteractEvent(Plugin p, NPCHandler handle){
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.handle = handle;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityInteract(final PlayerInteractEntityEvent evt){
		 Player p = (Player) evt.getPlayer();
		 Entity e = evt.getRightClicked();
		 System.out.println("r-clicked entity, entity is npc? = " + handle.getNPCManager().isNPC(e));
		 if(handle.getNPCManager().isNPC(e)){
			 SimpleNPC npc = handle.getSimpleNPCByEntity(e);
			 if(npc != null){
			 npc.interact(p);
			 } else {
				 QuestX.logChat(p, "Could not match entity to NPC");
			 }
		 }
	}

}
