package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.npcs.NPCHandler;


public class MovementMonitor implements Listener {

	final NPCHandler handle;

	public MovementMonitor(Plugin p, NPCHandler handle) {
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		this.handle = handle;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(final PlayerMoveEvent evt) {
		if (ConversationRegister.isPlayerConversing(evt.getPlayer().getName())) {
			if (!ConversationRegister.isPlayerWithinTalkingDistance(evt.getPlayer())) {
				ConversationRegister.endPlayerNPCConversation(evt.getPlayer());
			} 
		}
	}

}
