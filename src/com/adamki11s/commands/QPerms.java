package com.adamki11s.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.questx.QuestX;

public class QPerms {
	
	public static boolean hasPermission(Player p, String perm){
		if(p.isOp()){
			return true;
		} else if(p.hasPermission("questx.*")){
			return true;
		} else {
			if(p.hasPermission(perm)){
				return true;
			} else {
				QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("no_perms"));
				QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("node_needed") + ChatColor.YELLOW + perm);
				return false;
			}
		}
	}

}
