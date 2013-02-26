package com.adamki11s.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuestXCMDExecutor {

	public static void executeAsPlayer(String pName, String[] commands) {
		Player p = Bukkit.getServer().getPlayer(pName);
		if (p != null) {
			for (String cmd : commands) {
				p.performCommand(cmd);
			}
		}
	}
	
	public static void executeAsServer(String[] commands){
		for(String cmd : commands){
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		}
	}
}
