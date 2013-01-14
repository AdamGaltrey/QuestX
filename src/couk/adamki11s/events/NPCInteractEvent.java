package couk.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;

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
		 if(handle.getNPCManager().isNPC(e)){
			 SimpleNPC npc = handle.getSimpleNPCByEntity(e);
			 npc.interact(p);
		 }
	}

}
