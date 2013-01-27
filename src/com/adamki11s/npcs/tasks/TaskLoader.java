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
	String[] addPerms, remPerms, playerCmds, serverCmds;
	ItemStack[] retrieveItems, rewardItems;
	int rewardExp, rewardRep, rewardGold;
	EntityKillTracker ekt;
	NPCKillTracker nkt;
	boolean fetchItems, killEntities, killNPCS, awardItems, apAdd, apRem, execPlayerCommand, execServerCommand;

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
		
		if (!config.getString("KILL_NPCS").trim().equalsIgnoreCase("0")) {
			QuestX.logMSG("Loading NPC's to kill");
			this.nkt = new NPCKillTracker(config.getString("KILL_NPCS"));
			this.killNPCS = true;
		} else {
			QuestX.logMSG("Not loading NPC's to kill");
			this.killNPCS = false;
		}

		this.rewardExp = config.getInt("REWARD_EXP");
		this.rewardRep = config.getInt("REWARD_REP");
		this.rewardGold = config.getInt("REWARD_GOLD");
		
		if(!config.getString("REWARD_PERMISSIONS_ADD").equalsIgnoreCase("0")){
			this.addPerms = config.getString("REWARD_PERMISSIONS_ADD").split(",");
			this.apAdd = true;
		} else {
			this.apAdd = false;
		}
		
		if(!config.getString("REWARD_PERMISSIONS_REMOVE").equalsIgnoreCase("0")){
			this.remPerms = config.getString("REWARD_PERMISSIONS_REMOVE").split(",");
			this.apRem = true;
		} else {
			this.apRem = false;
		}
		
		if(!config.getString("EXECUTE_PLAYER_CMD").equalsIgnoreCase("0")){
			this.playerCmds = config.getString("EXECUTE_PLAYER_CMD").split(",");
			this.execPlayerCommand = true;
		} else {
			this.execPlayerCommand = false;
		}
		
		if(!config.getString("EXECUTE_SERVER_CMD").equalsIgnoreCase("0")){
			this.serverCmds = config.getString("EXECUTE_SERVER_CMD").split(",");
			this.execServerCommand = true;
		} else {
			this.execServerCommand = false;
		}
		
		QuestX.logMSG("TaskLoad Operation completed");
	}
	
	public boolean isExecutingPlayerCmds(){
		return this.execPlayerCommand;
	}
	
	public boolean isExecutingServerCmds(){
		return this.execServerCommand;
	}
	
	public String[] getServerCmds(){
		return this.serverCmds;
	}
	
	public String[] getPlayerCmds(){
		return this.playerCmds;
	}
	
	
	public boolean isAwardingAddPerms(){
		return this.apAdd;
	}
	
	public boolean isAwardingRemPerms(){
		return this.apRem;
	}
	
	public String[] getAddPerms(){
		return this.addPerms;
	}
	
	public String[] getRemPerms(){
		return this.remPerms;
	}
	
	public int getRewardExp(){
		return this.rewardExp;
	}
	
	public int getRewardRep(){
		return this.rewardRep;
	}
	
	public int getRewardGold(){
		return this.rewardGold;
	}
	
	public boolean isAwardGold(){
		return (this.rewardGold > 0);
	}
	
	public boolean isAwardExp(){
		return (this.rewardExp > 0);
	}
	
	public boolean isAwardRep(){
		return (this.rewardRep != 0);
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
	
	public boolean isKillNPCS(){
		return killNPCS;
	}

	public boolean isAwardItems() {
		return awardItems;
	}
	
	public EntityKillTracker getEKT(){
		return this.ekt;
	}
	
	public NPCKillTracker getNKT(){
		return this.nkt;
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
