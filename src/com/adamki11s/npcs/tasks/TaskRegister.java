package com.adamki11s.npcs.tasks;

import java.io.File;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;


public class TaskRegister {

	static HashSet<TaskManager> managers = new HashSet<TaskManager>();

	public static void registerTask(TaskManager tm) {
		managers.add(tm);
		QuestX.logDebug("Task added to Register");
	}
	
	public static void unRegisterTask(TaskManager tm){
		if(managers.contains(tm)){
			managers.remove(tm);
		}
	}
	
	public static void cancelPlayerTask(Player p){
		if(doesPlayerHaveTask(p.getName())){
			TaskManager tm = getTaskManager(p.getName());
			unRegisterTask(tm);
			QuestX.logChat(p, ChatColor.GREEN + "Task cancelled!");
		} else {
			QuestX.logChat(p, ChatColor.RED + "You do not have a task to cancel.");
		}
	}
	
	public static TaskManager getTaskManager(String pName){
		for (TaskManager tm : managers) {
			if (tm.getPlayerName().equalsIgnoreCase(pName)) {
				return tm;
			}
		}
		return null;
	}

	public static boolean doesPlayerHaveTask(String player) {
		for (TaskManager tm : managers) {
			if (tm.getPlayerName().equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean doesPlayerHaveTaskFromNPC(String player, String npcName){
		for (TaskManager tm : managers) {
			if (tm.getPlayerName().equalsIgnoreCase(player) && tm.getTaskLoader().getNpcName().equalsIgnoreCase(npcName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean unloadManagerByName(String playerName) {
		TaskManager task = null;
		for (TaskManager tm : managers) {
			if (tm.getPlayerName().equalsIgnoreCase(playerName)) {
				task = tm;
				break;
			}
		}
		managers.remove(task);
		if (task == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean hasPlayerCompletedTask(String npcName, String playerName){
		File f = FileLocator.getNPCTaskProgressionPlayerFile(npcName, playerName);
		if(f == null){
			return false;
		} else {
			return f.exists();
		}
	}

}
