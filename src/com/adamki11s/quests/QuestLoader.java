package com.adamki11s.quests;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.tasks.ISAParser;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class QuestLoader {

	final SyncConfiguration config;

	QuestTask[] tasks;

	String questName;
	int nodes, rewardExp, rewardRep;
	ItemStack[] rewardItems;
	
	HashMap<String, Integer> playerProgress = new HashMap<String, Integer>();
	HashMap<String, QuestTask> currentTask = new HashMap<String, QuestTask>();

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
		this.rewardItems = ISAParser.parseISA(this.config.getString("REWARD_ITEMS"));
		this.rewardExp = this.config.getInt("REWARD_EXP");
		this.rewardRep = this.config.getInt("REWARD_REP");
		
		
		this.tasks = new QuestTask[i];
		QuestX.logMSG("Loading Quest " + questName + " with " + this.nodes + " objectives.");
		for(int c = 1; c <= this.nodes; c++){
			//load and parse string into a QuestTask object
			String raw = this.config.getString(c + "");
			String qtypeEnum = raw.substring(0, raw.indexOf(":"));
			String dataString = raw.substring(raw.indexOf(":"));
			QType qType = QType.parseType(qtypeEnum);
			if(qType == null){
				//throw exception
			}
			QuestX.logMSG("raw = " + raw);
			QuestX.logMSG("qtypeEnum = " + qtypeEnum);
			QuestX.logMSG("dataString = " + dataString);
			this.tasks[c - 1] = QuestTaskParser.getTaskObject(dataString, qType);
			//this.tasks[c - 1] = new 
		}
	}
	
	public String getName(){
		return this.questName;
	}
	
	public boolean isPlayerProgressLoaded(String p){
		return this.playerProgress.containsKey(p);
	}
	
	public QuestTask getPlayerQuestTask(String p){
		return this.currentTask.get(p);
	}
	
	public void loadPlayerProgress(String p){
		File progression = FileLocator.getQuestProgressionPlayerFile(questName, p);
		SyncConfiguration load = new SyncConfiguration(progression);
		load.read();
		this.playerProgress.put(p, load.getInt("P"));
	}
	
	void setPlayerTask(String p){
		this.currentTask.put(p, this.tasks[this.playerProgress.get(p) - 1]);
	}
	
	public synchronized void setTaskComplete(String player){
		this.incrementTaskProgress(player);
		Player p = Bukkit.getServer().getPlayer(player);
		if(p != null){
			p.sendMessage("Quest task completed!");
		}
	}
	
	public boolean isQuestComplete(String player){
		return this.playerProgress.get(player) > this.nodes;
	}
	
	void incrementTaskProgress(String p){
		int current = this.playerProgress.get(p) + 1;
		this.playerProgress.put(p, current);
		SyncConfiguration c = new SyncConfiguration(FileLocator.getQuestProgressionPlayerFile(questName, p));
		c.add("P", current);
	}

}
