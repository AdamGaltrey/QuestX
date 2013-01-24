package com.adamki11s.quests;

import java.util.HashMap;
import java.util.HashSet;

import com.adamki11s.io.FileLocator;

public class QuestManager {

	public static HashSet<QuestLoader> quests = new HashSet<QuestLoader>();
	public static HashMap<String, String> currentQuest = new HashMap<String, String>();

	public static void loadQuest(String name) {
		quests.add(new QuestLoader(FileLocator.getQuestFile(name)));
	}

	public static boolean isQuestLoaded(String name) {
		for (QuestLoader q : quests) {
			if (q.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean doesPlayerHaveQuest(String pName){
		return currentQuest.containsKey(pName);
	}
	
	public static QuestLoader getQuestLoader(String quest){
		for(QuestLoader q : quests){
			if(q.getName().equalsIgnoreCase(quest)){
				return q;
			}
		}
		return null;
	}

}
