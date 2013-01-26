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

	volatile QuestTask[] tasks;

	String questName, startText, endText;
	int nodes, rewardExp, rewardRep;
	ItemStack[] rewardItems;

	volatile HashMap<String, Integer> playerProgress = new HashMap<String, Integer>();
	volatile HashMap<Integer, String> nodeCompleteText = new HashMap<Integer, String>();
	volatile HashMap<String, QuestTask> currentTask = new HashMap<String, QuestTask>();

	public QuestLoader(File f) {
		this.config = new SyncConfiguration(f);
		this.load();
	}

	void load() {
		this.config.read();
		this.questName = config.getString("NAME");
		int i = 0;
		while (config.doesKeyExist((i + 1) + "")) {
			i++;
		}
		this.nodes = i;
		if (!this.config.getString("REWARD_ITEMS").equalsIgnoreCase("0")) {
			this.rewardItems = ISAParser.parseISA(this.config.getString("REWARD_ITEMS"));
		} else {
			this.rewardItems = null;
		}
		this.rewardExp = this.config.getInt("REWARD_EXP");
		this.rewardRep = this.config.getInt("REWARD_REP");
		this.startText = this.config.getString("START_TEXT");
		this.endText = this.config.getString("END_TEXT");

		this.tasks = new QuestTask[i];
		QuestX.logMSG("Loading Quest " + questName + " with " + this.nodes + " objectives.");
		for (int c = 1; c <= this.nodes; c++) {
			// load and parse string into a QuestTask object
			String raw = this.config.getString(c + "");
			String qtypeEnum = raw.substring(0, raw.indexOf(":"));
			String dataString = raw.substring(raw.indexOf(":") + 1);
			QuestX.logMSG("READING---------------");
			QType qType = QType.parseType(qtypeEnum);
			if (qType == null) {
				// throw exception
				QuestX.logMSG("QUEST TYPE IS NULL!!!!!");
			} else {
				QuestX.logMSG("Quest Type = '" + qType.toString() + "'");
			}
			QuestX.logMSG("raw = " + raw);
			QuestX.logMSG("qtypeEnum = " + qtypeEnum);
			QuestX.logMSG("dataString = " + dataString);
			
			this.tasks[c - 1] = QuestTaskParser.getTaskObject(dataString, qType);
			// this.tasks[c - 1] = new
			QuestX.logMSG("QUEST TASK LOAD LOOOP-----------");
		}
		
		QuestX.logMSG("QUEST LOAD COMPLETE");
	}

	public String getName() {
		return this.questName;
	}

	public String getStartText() {
		return this.startText;
	}

	public String getEndText() {
		return this.endText;
	}

	public boolean isPlayerProgressLoaded(String p) {
		return this.playerProgress.containsKey(p);
	}

	public void playerStartedQuest(String p) {
		this.loadAndCheckPlayerProgress(p);
		this.playerProgress.put(p, 1);
		this.currentTask.put(p, this.tasks[0].getClonedInstance());// We only
																	// want to
																	// use the
																	// initial
																	// array for
																	// refernce,
																	// we don't
																	// want to
																	// change
																	// anything
	}
	
	public void loadAndCheckPlayerProgress(String p){
		File f = FileLocator.getQuestProgressionPlayerFile(questName, p);
		SyncConfiguration c = new SyncConfiguration(f);
		if(f.exists()){
			c.read();
			this.playerProgress.put(p, c.getInt("P"));
		} else {
			c.createFileIfNeeded();
			c.add("P", 1);
			c.write();
			this.playerProgress.put(p, 1);
		}
	}

	public QuestTask getPlayerQuestTask(String p) {
		QuestX.logMSG("Logging player progress = " + this.playerProgress.get(p));
		return this.currentTask.get(p);
	}

	public int getCurrentQuestNode(String p) {
		return this.playerProgress.get(p);
	}

	public void loadPlayerProgress(String p) {
		File progression = FileLocator.getQuestProgressionPlayerFile(questName, p);
		SyncConfiguration load = new SyncConfiguration(progression);
		load.read();
		this.playerProgress.put(p, load.getInt("P"));
	}

	void setPlayerTask(String p) {
		this.currentTask.put(p, this.tasks[this.playerProgress.get(p) - 1].getClonedInstance());
	}

	public synchronized void setTaskComplete(String player) {
		this.incrementTaskProgress(player);
		Player p = Bukkit.getServer().getPlayer(player);
		if (p != null) {
			p.sendMessage("Quest task completed!");
		}
	}

	public boolean isQuestComplete(String player) {
		return this.playerProgress.get(player) > this.nodes;
	}

	public void incrementTaskProgress(String p) {
		int current = this.playerProgress.get(p) + 1;
		this.playerProgress.put(p, current);
		SyncConfiguration c = new SyncConfiguration(FileLocator.getQuestProgressionPlayerFile(questName, p));
		c.add("P", current);
		c.write();
	}

}
