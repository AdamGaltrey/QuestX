package com.adamki11s.npcs.triggers.action;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.questx.QuestX;

public class TeleportAction implements Action{
	
	private boolean isActive = true;
	private final String world; 
	private int tickDelay;
	private double x, y, z;
	private float yaw, pitch;
	
	public TeleportAction(String npc, String data){
		String[] parts = data.split("#");
		String[] locInfo = parts[0].split(",");
		
		
		
		world = locInfo[0];
		try{
			x = Double.parseDouble(locInfo[1]);
			y = Double.parseDouble(locInfo[2]);
			z = Double.parseDouble(locInfo[3]);
			yaw = Float.parseFloat(locInfo[4]);
			pitch = Float.parseFloat(locInfo[5]);
			tickDelay = Integer.parseInt(parts[1]);
		} catch (NumberFormatException nfe){
			this.isActive = false;
			QuestX.logError("Error parsing Location for 'TELEPORT_PLAYER' for NPC '" + npc + "' in custom_trigger. Setting disabled");
		}
		if(Bukkit.getServer().getWorld(world) == null){
			this.isActive = false;
			QuestX.logError("World '" + world + "' does not exist for 'TELEPORT_PLAYER' for NPC '" + npc + "' in custom_trigger. Setting disabled");
		}
	}

	@Override
	public void implement(final Player p) {
		final Location l = new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
		if(this.tickDelay > 0){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(QuestX.p, new Runnable(){
				
				public void run(){
					p.teleport(l);
				}
				
			}, this.tickDelay);
		} else {
			p.teleport(l);
		}
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
