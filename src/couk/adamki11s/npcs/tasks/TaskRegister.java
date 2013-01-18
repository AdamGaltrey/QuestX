package couk.adamki11s.npcs.tasks;

import java.util.HashSet;

import couk.adamki11s.questx.QuestX;

public class TaskRegister {

	static HashSet<TaskManager> managers = new HashSet<TaskManager>();

	public static void registerTask(TaskManager tm) {
		managers.add(tm);
		QuestX.logMSG("Task added to Register");
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

}
