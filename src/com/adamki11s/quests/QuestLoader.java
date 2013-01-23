package com.adamki11s.quests;

import java.io.File;
import java.util.LinkedList;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class QuestLoader {

	final SyncConfiguration config;

	QuestTask[] tasks;

	String questName;
	int nodes, rewardExp, rewardRep;
	ItemStack[] rewardItems;

	public QuestLoader(File f) {
		this.config = new SyncConfiguration(f);
	}

	void load() {
		this.config.read();
		this.questName = config.getString("NAME");
		int i = 0;
		do {
			i++;
		} while (config.doesKeyExist(i + ""));
		this.nodes = i;
		this.tasks = new QuestTask[i];
		
		for(int c = 1; c <= this.nodes; c++){
			//load and parse string into a QuestTask object
		}
	}

}
