package com.adamki11s.quests.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.quests.QuestLoader;
import com.adamki11s.quests.QuestManager;
import com.adamki11s.quests.QuestTask;
import com.adamki11s.questx.QuestX;

public class GotoLocationController {

	private static HashMap<String, GotoLocationTask> tasks = new HashMap<String, GotoLocationTask>();

	private static HashMap<String, GotoLocationTask> completed = new HashMap<String, GotoLocationTask>();

	private static Object lock = new Object();

	public static void addLocationTask(String pName, GotoLocationTask task) {
		synchronized (lock) {
			tasks.put(pName, task);
		}
	}

	public static boolean removeLocationTask(String pName) {
		synchronized (lock) {
			if (tasks.containsKey(pName)) {
				tasks.remove(pName);
				return true;
			} else {
				return false;
			}
		}
	}

	public static void run() {
		synchronized (lock) {
			ArrayList<String> markRemove = new ArrayList<String>();
			for (Entry<String, GotoLocationTask> e : tasks.entrySet()) {
				Player p = Bukkit.getPlayer(e.getKey());
				if (p == null) {
					// remove from map after loop
					markRemove.add(e.getKey());
					continue;
				} else {
					GotoLocationTask t = e.getValue();
					Location l = p.getLocation();
					if (!t.isMarked()) {
						if (t.isInCheckRange(l)) {
							t.setMarked(true);
							if (t.isAtLocation(l)) {
								completed.put(p.getName(), t);
								// set to be added to completed list
							}
						} 
					} else {
						if (t.isAtLocation(l)) {
							completed.put(p.getName(), t);
							// set to be added to completed list
						}
					}
				}
			}

			// remove null players
			for (String rem : markRemove) {
				tasks.remove(rem);
			}

			// return completed players from list
			for (Entry<String, GotoLocationTask> e : completed.entrySet()) {
				if (tasks.containsKey(e.getKey())) {
					tasks.remove(e.getKey());
					completeTask(e.getKey());
					// complete quest
				}
			}
			
			if(completed.size() > 0){
				completed.clear();
			}

		}
	}

	private static void completeTask(String p) {
		
		
		QuestLoader ql = QuestManager.getQuestLoader(QuestManager.getCurrentQuestName(p));
		QuestTask qt = QuestManager.getCurrentQuestTask(p);

		if (!ql.isQuestComplete(p)) {
			// run checks
			Player player = Bukkit.getServer().getPlayer(p);

			ql.incrementTaskProgress(player);

			

			if (ql.isQuestComplete(p)) {
				QuestX.logChat(player, ql.getEndText());
				QuestManager.removeCurrentPlayerQuest(ql.getName(), p);
			} else {
				QuestX.logChat(player, qt.getCompleteTaskText());
			}

		}
	}

}
