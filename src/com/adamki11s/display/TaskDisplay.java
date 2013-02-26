package com.adamki11s.display;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.npcs.tasks.TaskManager;
import com.adamki11s.npcs.tasks.TaskRegister;
import com.adamki11s.questx.QuestX;

public class TaskDisplay {

	public static void displayTaskInfo(Player p) {	
		if (TaskRegister.doesPlayerHaveTask(p.getName())) {
			TaskManager tm = TaskRegister.getTaskManager(p.getName());
			boolean completed = tm.isTaskCompleted();
			if (!completed) {
				QuestX.logChat(p, "[Task Info] " + LocaleBundle.getString("assigned_npc") + tm.getTaskLoader().getNpcName() + "'");
				tm.sendWhatIsLeftToDo(p);
				QuestX.logChat(p, StaticStrings.separator);
			} else {
				QuestX.logChat(p, LocaleBundle.getString("completed_return_npc") + tm.getTaskLoader().getNpcName() + LocaleBundle.getString("claim_reward_task"));
			}
		} else {
			QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("no_task_assigned"));
		}
	}

}
