package com.adamki11s.display;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.npcs.tasks.TaskManager;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.questx.QuestX;

public class TaskDisplay {
	
	public static void displayTaskInfo(Player p){
		if(TaskRegister.doesPlayerHaveTask(p.getName())){
			TaskManager tm = TaskRegister.getTaskManager(p.getName());
			QuestX.logChat(p, "[Task Info] Assigned NPC '" + tm.getTaskLoader().getNpcName() + "'");
			tm.sendWhatIsLeftToDo(p);
			QuestX.logChat(p, StaticStrings.separator);
		} else {
			QuestX.logChat(p, ChatColor.RED + "You do not have a task!");
		}
	}

}
