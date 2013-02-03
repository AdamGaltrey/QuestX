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
		QuestX.logMSG("TaskLoader Instantiated");
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
		QuestX.logMSG("Reading config");
		config.read();
		QuestX.logMSG("Config read to memory");

		if (config.doesKeyExist("TASK_NAME")) {
			this.taskName = config.getString("TASK_NAME");
		} else {
			QuestX.logError("Missing property 'TASK_NAME' in task file for NPC " + this.npcName);
		}
		
		if (config.doesKeyExist("TASK_DESCRIPTION")) {
			this.taskDescription = config.getString("TASK_DESCRIPTION");
		} else {
			QuestX.logError("Missing property 'TASK_DESCRIPTION' in task file for NPC " + this.npcName);
		}
		
		if (config.doesKeyExist("INCOMPLETE_TASK_SPEECH")) {
			this.incompleteTaskSpeech = config.getString("INCOMPLETE_TASK_SPEECH");
		} else {
			QuestX.logError("Missing property 'INCOMPLETE_TASK_SPEECH' in task file for NPC " + this.npcName);
		}
		
		if (config.doesKeyExist("COMPLETE_TASK_SPEECH")) {
			this.completeTaskSpeech = config.getString("COMPLETE_TASK_SPEECH");
		} else {
			QuestX.logError("Missing property 'COMPLETE_TASK_SPEECH' in task file for NPC " + this.npcName);
		}

		QuestX.logMSG("Reading fetch_items");

		if (config.doesKeyExist("FETCH_ITEMS")) {
			if (!config.getString("FETCH_ITEMS").trim().equalsIgnoreCase("0")) {
				try {
					this.retrieveItems = ISAParser.parseISA(config.getString("FETCH_ITEMS"), this.npcName, false);
					this.fetchItems = true;
				} catch (InvalidISAException e) {
					e.printErrorReason();
					this.fetchItems = false;
				}
			} else {
				this.fetchItems = false;
			}
		} else {
			QuestX.logError("Missing property 'FETCH_ITEMS' in task file for NPC " + this.npcName);
			this.fetchItems = false;
		}

		QuestX.logMSG("Reading reward_items");

		if (config.doesKeyExist("REWARD_ITEMS")) {
			if (!config.getString("REWARD_ITEMS").trim().equalsIgnoreCase("0")) {
				try {
					this.rewardItems = ISAParser.parseISA(config.getString("REWARD_ITEMS"), this.npcName, false);
					this.awardItems = true;
				} catch (InvalidISAException e) {
					e.printErrorReason();
					this.awardItems = false;
				}
			} else {
				this.awardItems = false;
			}
		} else {
			QuestX.logError("Missing property 'REWARD_ITEMS' in task file for NPC " + this.npcName);
			this.awardItems = false;
		}

		QuestX.logMSG("Reading kill_entities");

		if (config.doesKeyExist("KILL_ENTITIES")) {
			if (!config.getString("KILL_ENTITIES").trim().equalsIgnoreCase("0")) {
				try {
					this.ekt = new EntityKillTracker(config.getString("KILL_ENTITIES"));
					this.killEntities = true;
				} catch (InvalidKillTrackerException e) {
					e.printCustomErrorReason(false, this.npcName);
					this.killEntities = false;
				}
			} else {
				this.killEntities = false;
			}
		} else {
			QuestX.logError("Missing property 'KILL_ENTITIES' in task file for NPC " + this.npcName);
			this.killEntities = false;
		}

		if (config.doesKeyExist("KILL_NPCS")) {
			if (!config.getString("KILL_NPCS").trim().equalsIgnoreCase("0")) {
				QuestX.logMSG("Loading NPC's to kill");
				this.nkt = new NPCKillTracker(config.getString("KILL_NPCS"));
				this.killNPCS = true;
			} else {
				QuestX.logMSG("Not loading NPC's to kill");
				this.killNPCS = false;
			}
		} else {
			QuestX.logError("Missing property 'KILL_NPCS' in task file for NPC " + this.npcName);
			this.killNPCS = false;
		}

		if (config.doesKeyExist("FIREWORKS")) {
			if (!config.getString("FIREWORKS").equalsIgnoreCase("0")) {
				String parts[] = config.getString("FIREWORKS").split(",");
				int rad, sect;
				try {
					rad = Integer.parseInt(parts[0]);
					sect = Integer.parseInt(parts[1]);
					fwRadius = rad;
					fwSectors = sect;
					this.fireWorks = true;
				} catch (NumberFormatException nfe) {
					QuestX.logError("Failed to parse integer for FIREWORKS tag, make sure the value is greater than or equal to 0 and is a whole number.");
					QuestX.logError("Line : " + config.getString("FIREWORKS"));
					this.fireWorks = false;
				}
			} else {
				this.fireWorks = false;
			}
		} else {
			QuestX.logError("Missing property 'FIREWORKS' in task file for NPC " + this.npcName);
			this.fireWorks = false;
		}

		if (config.doesKeyExist("REWARD_EXP")) {
			this.rewardExp = config.getInt("REWARD_EXP");
		} else {
			QuestX.logError("Missing property 'REWARD_EXP' in task file for NPC " + this.npcName);
		}
		if (config.doesKeyExist("REWARD_REP")) {
			this.rewardRep = config.getInt("REWARD_REP");
		} else {
			QuestX.logError("Missing property 'REWARD_REP' in task file for NPC " + this.npcName);
		}
		if (config.doesKeyExist("REWARD_GOLD")) {
			this.rewardGold = config.getInt("REWARD_GOLD");
		} else {
			QuestX.logError("Missing property 'REWARD_GOLD' in task file for NPC " + this.npcName);
		}

		if (config.doesKeyExist("REWARD_PERMISSIONS_ADD")) {
			if (!config.getString("REWARD_PERMISSIONS_ADD").equalsIgnoreCase("0")) {
				this.addPerms = config.getString("REWARD_PERMISSIONS_ADD").split("#");
				this.apAdd = true;
			} else {
				this.apAdd = false;
			}
		} else {
			QuestX.logError("Missing property 'REWARD_PERMISSIONS_ADD' in task file for NPC " + this.npcName);
			this.apAdd = false;
		}

		if (config.doesKeyExist("REWARD_PERMISSIONS_REMOVE")) {
			if (!config.getString("REWARD_PERMISSIONS_REMOVE").equalsIgnoreCase("0")) {
				this.remPerms = config.getString("REWARD_PERMISSIONS_REMOVE").split("#");
				this.apRem = true;
			} else {
				this.apRem = false;
			}
		} else {
			QuestX.logError("Missing property 'REWARD_PERMISSIONS_REMOVE' in task file for NPC " + this.npcName);
			this.apRem = false;
		}

		if (config.doesKeyExist("EXECUTE_PLAYER_CMD")) {
			if (!config.getString("EXECUTE_PLAYER_CMD").equalsIgnoreCase("0")) {
				this.playerCmds = config.getString("EXECUTE_PLAYER_CMD").split("#");
				this.execPlayerCommand = true;
			} else {
				this.execPlayerCommand = false;
			}
		} else {
			QuestX.logError("Missing property 'EXECUTE_PLAYER_CMD' in task file for NPC " + this.npcName);
			this.execPlayerCommand = false;
		}

		if (config.doesKeyExist("EXECUTE_SERVER_CMD")) {
			if (!config.getString("EXECUTE_SERVER_CMD").equalsIgnoreCase("0")) {
				this.serverCmds = config.getString("EXECUTE_SERVER_CMD").split("#");
				this.execServerCommand = true;
			} else {
				this.execServerCommand = false;
			}
		} else {
			QuestX.logError("Missing property 'EXECUTE_SERVER_CMD' in task file for NPC " + this.npcName);
			this.execServerCommand = false;
		}

		QuestX.logMSG("TaskLoad Operation completed");
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
