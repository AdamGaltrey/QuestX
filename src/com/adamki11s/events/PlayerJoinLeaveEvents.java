package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.quests.QuestManager;
import com.adamki11s.reputation.ReputationManager;

public class PlayerJoinLeaveEvents implements Listener{

	public PlayerJoinLeaveEvents(Plugin main){
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void playerJoin(final PlayerJoinEvent evt){
		ReputationManager.loadPlayerReputation(evt.getPlayer().getName());
		QuestManager.loadCurrentPlayerQuest(evt.getPlayer().getName());
	}
	
	@EventHandler
	public void playerQuit(final PlayerQuitEvent evt){
		ReputationManager.unloadPlayerReputation(evt.getPlayer().getName());
		QuestManager.unloadPlayerQuestData(evt.getPlayer().getName());
	}
	
}
