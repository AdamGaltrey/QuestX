package com.adamki11s.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.io.GeneralConfigData;
import com.adamki11s.pathing.decision.DecisionController;
import com.adamki11s.quests.QuestManager;
import com.adamki11s.reputation.ReputationManager;
import com.adamki11s.threads.AsyncThread;
import com.adamki11s.updates.UpdateNotifier;

public class PlayerJoinLeaveEvents implements Listener{

	public PlayerJoinLeaveEvents(Plugin main){
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void playerJoin(final PlayerJoinEvent evt){
		AsyncThread.playerJoined();
		if(GeneralConfigData.isNotifyAdmin()){
			UpdateNotifier.onPlayerLogin(evt.getPlayer());
		}
		ReputationManager.loadPlayerReputation(evt.getPlayer().getName());
		QuestManager.loadCurrentPlayerQuest(evt.getPlayer().getName());
	}
	
	@EventHandler
	public void playerQuit(final PlayerQuitEvent evt){
		AsyncThread.playerLeft();
		ReputationManager.unloadPlayerReputation(evt.getPlayer().getName());
		QuestManager.unloadPlayerQuestData(evt.getPlayer().getName());
	}
	
}
