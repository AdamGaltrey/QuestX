package com.adamki11s.npcs.tasks;

import java.io.File;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;


public class TaskLoader {

	private final File taskFile;

	final String npcName;
	String taskName, taskDescription, incompleteTaskSpeech, completeTaskSpeech;
	ItemStack[] retrieveItems, rewardItems;
	int rewardExp, rewardRep;
	EntityKillTracker ekt;
	boolean fetchItems, killEntities, awardItems;

	public TaskLoader(File taskFile, String npcName) {
		this.taskFile = taskFile;
		this.npcName = npcName;
		QuestX.logMSG("TaskLoader Instantiated");
	}
	
	public void setTaskCompleted(String playerName){
		File completed = FileLocator.getNPCTaskProgressionPlayerFile(npcName, playerName);
		try {
			completed.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		SyncConfiguration config = new SyncConfiguration(this.taskFile);
		QuestX.logMSG("Reading config");
		config.read();
		QuestX.logMSG("Config read to memory");
		
		this.taskName = config.getString("TASK_NAME");
		this.taskDescription = config.getString("TASK_DESCRIPTION");
		
		this.incompleteTaskSpeech = config.getString("INCOMPLETE_TASK_SPEECH");
		this.completeTaskSpeech = config.getString("COMPLETE_TASK_SPEECH");

		QuestX.logMSG("Reading fetch_items");
		
		if (!config.getString("FETCH_ITEMS").trim().equalsIgnoreCase("0")) {
			this.retrieveItems = ISAParser.parseISA(config.getString("FETCH_ITEMS"));
			this.fetchItems = true;
		} else {
			this.fetchItems = false;
		}
		
		QuestX.logMSG("Reading reward_items");

		if (!config.getString("REWARD_ITEMS").trim().equalsIgnoreCase("0")) {
			this.rewardItems = ISAParser.parseISA(config.getString("REWARD_ITEMS"));
			this.awardItems = true;
		} else {
			this.awardItems = false;
		}
		
		QuestX.logMSG("Reading kill_entities");
		
		if (!config.getString("KILL_ENTITIES").trim().equalsIgnoreCase("0")) {
			this.ekt = new EntityKillTracker(config.getString("KILL_ENTITIES"));
			this.killEntities = true;
		} else {
			this.killEntities = false;
		}
		
		

		this.rewardExp = config.getInt("REWARD_EXP");
		this.rewardRep = config.getInt("REWARD_REP");
		
		QuestX.logMSG("TaskLoad Operation completed");
	}

	public String getTaskDescription(){
		return this.taskDescription;
	}
	
	public String getNpcName() {
		return npcName;
	}

	public String getTaskName() {
		return taskName;
	}

	public boolean isFetchItems() {
		return fetchItems;
	}

	public boolean isKillEntities() {
		return killEntities;
	}

	public boolean isAwardItems() {
		return awardItems;
	}
	
	public EntityKillTracker getEKT(){
		return this.ekt;
	}
	
	public ItemStack[] getRequiredItems(){
		return this.retrieveItems;
	}
	
	public ItemStack[] getRewardItems(){
		return this.rewardItems;
	}

	public String getIncompleteTaskSpeech() {
		return incompleteTaskSpeech;
	}

	public String getCompleteTaskSpeech() {
		return completeTaskSpeech;
	}
	
	

}
