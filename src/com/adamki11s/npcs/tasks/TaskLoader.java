package com.adamki11s.npcs.tasks;

import java.io.File;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.exceptions.InvalidKillTrackerException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class TaskLoader {

	private final File taskFile;

	final String npcName;
	String taskName, taskDescription, incompleteTaskSpeech, completeTaskSpeech;
	String[] addPerms, remPerms, playerCmds, serverCmds;
	ItemStack[] retrieveItems, rewardItems;
	int rewardExp, rewardRep, rewardGold, fwRadius, fwSectors;
	EntityKillTracker ekt;
	NPCKillTracker nkt;
	boolean fetchItems, killEntities, killNPCS, awardItems, apAdd, apRem, execPlayerCommand, execServerCommand, fireWorks;

	public TaskLoader(File taskFile, String npcName) {
		this.taskFile = taskFile;
		this.npcName = npcName;
		QuestX.logDebug("TaskLoader Instantiated");
	}

	public void setTaskCompleted(String playerName) {
		File completed = FileLocator.getNPCTaskProgressionPlayerFile(npcName, playerName);
		try {
			completed.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		SyncConfiguration config = new SyncConfiguration(this.taskFile);
		QuestX.logDebug("Reading config");
		config.read();
		QuestX.logDebug("Config read to memory");

		this.taskName = config.getString("TASK_NAME", "TASK_NAME");

		this.taskDescription = config.getString("TASK_DESCRIPTION", "Task Description");

		this.incompleteTaskSpeech = config.getString("INCOMPLETE_TASK_SPEECH", "The Task has not been completed yet.");

		this.completeTaskSpeech = config.getString("COMPLETE_TASK_SPEECH", "Task completed.");

		String fetchI = config.getString("FETCH_ITEMS", "0");
		if (!fetchI.trim().equalsIgnoreCase("0")) {
			try {
				this.retrieveItems = ISAParser.parseISA(fetchI, this.npcName, false);
				this.fetchItems = true;
			} catch (InvalidISAException e) {
				e.printErrorReason();
				this.fetchItems = false;
			}
		} else {
			this.fetchItems = false;
		}

		String rewI = config.getString("REWARD_ITEMS", "0");
		if (!rewI.trim().equalsIgnoreCase("0")) {
			try {
				this.rewardItems = ISAParser.parseISA(rewI, this.npcName, false);
				this.awardItems = true;
			} catch (InvalidISAException e) {
				e.printErrorReason();
				this.awardItems = false;
			}
		} else {
			this.awardItems = false;
		}

		String killE = config.getString("KILL_ENTITIES", "0");
		if (!killE.trim().equalsIgnoreCase("0")) {
			try {
				this.ekt = new EntityKillTracker(killE);
				this.killEntities = true;
			} catch (InvalidKillTrackerException e) {
				e.printCustomErrorReason(false, this.npcName);
				this.killEntities = false;
			}
		} else {
			this.killEntities = false;
		}

		String killNPC = config.getString("KILL_NPCS", "0");
		if (!killNPC.trim().equalsIgnoreCase("0")) {
			QuestX.logDebug("Loading NPC's to kill");
			this.nkt = new NPCKillTracker(killNPC);
			this.killNPCS = true;
		} else {
			QuestX.logDebug("Not loading NPC's to kill");
			this.killNPCS = false;
		}

		String fWorks = config.getString("FIREWORKS", "0");
		if (!fWorks.equalsIgnoreCase("0")) {
			String parts[] = fWorks.split(",");
			int rad, sect;
			try {
				rad = Integer.parseInt(parts[0]);
				sect = Integer.parseInt(parts[1]);
				fwRadius = rad;
				fwSectors = sect;
				this.fireWorks = true;
			} catch (NumberFormatException nfe) {
				QuestX.logError("Failed to parse integer for FIREWORKS tag, make sure the value is greater than or equal to 0 and is a whole number.");
				QuestX.logError("Line : " + fWorks);
				this.fireWorks = false;
			}
		} else {
			this.fireWorks = false;
		}

		this.rewardExp = config.getInt("REWARD_EXP", 0);

		this.rewardRep = config.getInt("REWARD_REP", 0);

		this.rewardGold = config.getInt("REWARD_GOLD", 0);

		String rewPAdd = config.getString("REWARD_PERMISSIONS_ADD", "0");
		if (!rewPAdd.equalsIgnoreCase("0")) {
			this.addPerms = rewPAdd.split("#");
			this.apAdd = true;
		} else {
			this.apAdd = false;
		}

		String rewPRem = config.getString("REWARD_PERMISSIONS_REMOVE", "0");
		if (!rewPRem.equalsIgnoreCase("0")) {
			this.remPerms = rewPRem.split("#");
			this.apRem = true;
		} else {
			this.apRem = false;
		}

		String execPCmd = config.getString("EXECUTE_PLAYER_CMD", "0");
		if (!execPCmd.equalsIgnoreCase("0")) {
			this.playerCmds = execPCmd.split("#");
			this.execPlayerCommand = true;
		} else {
			this.execPlayerCommand = false;
		}

		String execSCmd = config.getString("EXECUTE_SERVER_CMD", "0");

		if (!execSCmd.equalsIgnoreCase("0")) {
			this.serverCmds = execSCmd.split("#");
			this.execServerCommand = true;
		} else {
			this.execServerCommand = false;
		}

	}

	public boolean isExecutingPlayerCmds() {
		return this.execPlayerCommand;
	}

	public boolean isExecutingServerCmds() {
		return this.execServerCommand;
	}

	public String[] getServerCmds() {
		return this.serverCmds;
	}

	public String[] getPlayerCmds() {
		return this.playerCmds;
	}

	public boolean isAwardingAddPerms() {
		return this.apAdd;
	}

	public boolean isAwardingRemPerms() {
		return this.apRem;
	}

	public String[] getAddPerms() {
		return this.addPerms;
	}

	public String[] getRemPerms() {
		return this.remPerms;
	}

	public int getRewardExp() {
		return this.rewardExp;
	}

	public int getRewardRep() {
		return this.rewardRep;
	}

	public int getRewardGold() {
		return this.rewardGold;
	}

	public boolean isAwardGold() {
		return (this.rewardGold > 0);
	}

	public boolean isAwardExp() {
		return (this.rewardExp > 0);
	}

	public boolean isAwardRep() {
		return (this.rewardRep != 0);
	}

	public String getTaskDescription() {
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

	public boolean isKillNPCS() {
		return killNPCS;
	}

	public boolean isAwardItems() {
		return awardItems;
	}

	public EntityKillTracker getEKT() {
		return this.ekt;
	}

	public NPCKillTracker getNKT() {
		return this.nkt;
	}

	public ItemStack[] getRequiredItems() {
		return this.retrieveItems;
	}

	public ItemStack[] getRewardItems() {
		return this.rewardItems;
	}

	public String getIncompleteTaskSpeech() {
		return incompleteTaskSpeech;
	}

	public String getCompleteTaskSpeech() {
		return completeTaskSpeech;
	}

}
