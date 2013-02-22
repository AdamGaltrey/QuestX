package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.pathing.decision.DecisionController;

public class PlayerTeleportListener implements Listener{

	public PlayerTeleportListener(Plugin main){
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void playerTele(final PlayerTeleportEvent evt){
		DecisionController.forceUpdate(evt.getPlayer().getLocation());
	}
	
	@EventHandler
	public void playerRespawn(final PlayerRespawnEvent evt){
		DecisionController.forceUpdate(evt.getPlayer().getLocation());
	}
	
}
