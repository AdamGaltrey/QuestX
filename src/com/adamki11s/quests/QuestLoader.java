package com.adamki11s.quests;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.commands.QuestXCMDExecutor;
import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.exceptions.InvalidKillTrackerException;
import com.adamki11s.exceptions.InvalidQuestException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.tasks.EntityKillTracker;
import com.adamki11s.npcs.tasks.Fireworks;
import com.adamki11s.npcs.tasks.ISAParser;
import com.adamki11s.npcs.tasks.NPCKillTracker;
import com.adamki11s.quests.locations.GotoLocationController;
import com.adamki11s.quests.locations.GotoLocationTask;
import com.adamki11s.questx.QuestX;
import com.adamki11s.reputation.ReputationManager;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class QuestLoader {

	final SyncConfiguration config;

	volatile QuestTask[] tasks;

	String questName, startText, endText;

	int nodes, rewardExp, rewardRep, rewardGold, fwRadius, fwSectors;
	ItemStack[] rewardItems;

	volatile HashMap<String, Integer> playerProgress = new HashMap<String, Integer>();
	volatile HashMap<Integer, String> nodeCompleteText = new HashMap<Integer, String>();
	volatile HashMap<String, QuestTask> currentTask = new HashMap<String, QuestTask>();

	String[] addPerms, remPerms, playerCmds, serverCmds;
	EntityKillTracker ekt;
	NPCKillTracker nkt;
	boolean apAdd, apRem, execPlayerCommand, execServerCommand, fireWorks;

	public QuestLoader(File f) throws InvalidQuestException {
		this.config = new SyncConfiguration(f);
		this.load();
	}

	void load() throws InvalidQuestException{
		this.config.read();

		this.questName = config.getString("NAME", "QuestName");

		int i = 0;

		while (config.doesKeyExist((i + 1) + "")) {
			i++;
		}

		this.nodes = i;

		String rew = config.getString("REWARD_ITEMS", "0");

		if (!rew.equalsIgnoreCase("0")) {
			try {
				this.rewardItems = ISAParser.parseISA(rew, this.questName, true);
			} catch (InvalidISAException e) {
				this.rewardItems = null;
				e.printErrorReason();
			}
		} else {
			this.rewardItems = null;
		}

		this.rewardExp = config.getInt("REWARD_EXP", 0);

		this.rewardRep = config.getInt("REWARD_REP", 0);

		this.startText = this.config.getString("START_TEXT", "Quest Started.");

		this.endText = this.config.getString("END_TEXT", "Quest Completed.");

		this.rewardGold = config.getInt("REWARD_GOLD", 0);

		String rewPermsAdd = config.getString("REWARD_PERMISSIONS_ADD", "0");
		if (!rewPermsAdd.equalsIgnoreCase("0")) {
			this.addPerms = rewPermsAdd.split(",");
			this.apAdd = true;
		} else {
			this.apAdd = false;
		}

		String rewPermsRem = config.getString("REWARD_PERMISSIONS_REMOVE", "0");
		if (!rewPermsRem.equalsIgnoreCase("0")) {
			this.remPerms = rewPermsRem.split(",");
			this.apRem = true;
		} else {
			this.apRem = false;
		}

		String execPCMD = config.getString("EXECUTE_PLAYER_CMD", "0");
		if (!execPCMD.equalsIgnoreCase("0")) {
			this.playerCmds = execPCMD.split(",");
			this.execPlayerCommand = true;
		} else {
			this.execPlayerCommand = false;
		}

		String execSCMD = config.getString("EXECUTE_SERVER_CMD", "0");
		if (!execSCMD.equalsIgnoreCase("0")) {
			this.serverCmds = execPCMD.split(",");
			this.execServerCommand = true;
		} else {
			this.execServerCommand = false;
		}

		String fWorks = config.getString("FIREWORKS", "0");
		if (!fWorks.equalsIgnoreCase("0")) {
			this.fireWorks = true;
			String parts[] = fWorks.split(",");
			int rad, sect;

			try {
				rad = Integer.parseInt(parts[0]);
				sect = Integer.parseInt(parts[1]);
				fwRadius = rad;
				fwSectors = sect;
			} catch (NumberFormatException nfe) {
				this.fireWorks = false;
			}

		} else {
			this.fireWorks = false;
		}

		this.tasks = new QuestTask[i];
		QuestX.logDebug("Loading Quest " + questName + " with " + this.nodes + " objectives.");
		for (int c = 1; c <= this.nodes; c++) {
			// load and parse string into a QuestTask object
			String raw = this.config.getString(c + "", "");
			String qtypeEnum = raw.substring(0, raw.indexOf(":"));
			String dataString = raw.substring(raw.indexOf(":") + 1);
			QuestX.logDebug("READING---------------");
			QType qType = QType.parseType(qtypeEnum);
			if (qType == null) {
				throw new InvalidQuestException(raw, "QuestType was invalid! Got '" + qtypeEnum + "', expected (FETCH_ITEMS, KILL_ENTITIES, KILL_NPC, GOTO OR TALK_NPC)", this.questName);
			} else {
				QuestX.logDebug("Quest Type = '" + qType.toString() + "'");
			}
			QuestX.logDebug("raw = " + raw);
			QuestX.logDebug("qtypeEnum = " + qtypeEnum);
			QuestX.logDebug("dataString = " + dataString);

			try {
				this.tasks[c - 1] = QuestTaskParser.getTaskObject(questName, dataString, qType, this.questName);
			} catch (InvalidKillTrackerException e) {
				e.printCustomErrorReason(true, questName);
				throw new InvalidQuestException(raw, "QuestType '" + qtypeEnum + "' was incorrectly formatted!", this.questName);
			} catch (InvalidISAException e) {
				e.printErrorReason();
				throw new InvalidQuestException(raw, "QuestType '" + qtypeEnum + "' was incorrectly formatted!", this.questName);
			} catch (InvalidQuestException q) {
				q.printErrorReason();
				throw new InvalidQuestException(raw, "QuestType '" + qtypeEnum + "' was incorrectly formatted!", this.questName);
			}

			// this.tasks[c - 1] = new
			QuestX.logDebug("QUEST TASK LOAD LOOOP-----------");
		}

		QuestX.logDebug("QUEST LOAD COMPLETE");
	}

	public String getProgress(String player) {
		return ("(" + this.playerProgress.get(player) + "/" + this.nodes + ")");
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

	public boolean isAwardingItems() {
		return (this.rewardItems != null);
	}

	public String[] getAddPerms() {
		return this.addPerms;
	}

	public String[] getRemPerms() {
		return this.remPerms;
	}

	public boolean isAwardGold() {
		return (this.rewardGold > 0);
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

	public synchronized void playerStartedQuest(String p) {
		File cur = FileLocator.getCurrentQuestFile();
		SyncConfiguration cfg = new SyncConfiguration(cur);
		cfg.read();

		if (!cfg.doesKeyExist(p)) {
			cfg.MergeRWArrays();
			cfg.add(p, this.getName());
			cfg.write();
		}

		this.loadAndCheckPlayerProgress(p);
		this.currentTask.put(p, this.tasks[playerProgress.get(p) - 1].getClonedInstance());// We
																							// only
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

	private synchronized void playerFinishedQuest(String p) {
		File cur = FileLocator.getCurrentQuestFile();
		SyncConfiguration cfg = new SyncConfiguration(cur);
		cfg.read();

		for (Entry<String, Object> e : cfg.getReadableData().entrySet()) {
			String n = e.getKey();
			if (!n.equalsIgnoreCase(p)) {
				cfg.add(n, e.getValue());
			}
		}

		cfg.write();
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

	public boolean isAwardExp() {
		return (this.rewardExp > 0);
	}

	public boolean isAwardRep() {
		return (this.rewardRep != 0);
	}

	public synchronized void awardPlayerOnQuestComplete(Player p) {

		// remove fromm current list
		this.playerFinishedQuest(p.getName());

		if (this.isAwardingItems()) {
			ItemStack[] rewardItems = this.rewardItems;
			for (ItemStack i : rewardItems) {
				if (i != null) {
					if ((p.getInventory().firstEmpty()) != -1) {
						p.getInventory().addItem(i);
					} else {
						p.getWorld().dropItemNaturally(p.getLocation(), i);
					}
				}
			}
		}

		if (this.isAwardExp()) {
			for (int exp = 1; exp <= this.getRewardExp(); exp++) {
				ExperienceOrb orb = p.getWorld().spawn(p.getLocation().add(0.5, 10, 0.5), ExperienceOrb.class);
				orb.setExperience(1);
			}
		}

		if (this.isAwardRep()) {

			int awardRep = this.getRewardRep();
			// QuestX.logChat(p, "Trying to award rep = " + awardRep);
			ReputationManager.updateReputation(p.getName(), awardRep);
			// adjust rep
		}

		if (this.isAwardGold() && QuestX.isEconomySupported()) {
			if (QuestX.economy.hasAccount(p.getName())) {
				QuestX.economy.bankDeposit(p.getName(), this.getRewardGold());
			} else {
				// can't award, no account
			}
		}

		if (this.isAwardingAddPerms() && QuestX.isPermissionsSupported()) {
			for (String perm : this.getAddPerms()) {
				QuestX.permission.playerAdd(p, perm);
			}
		}

		if (this.isAwardingRemPerms() && QuestX.isPermissionsSupported()) {
			for (String perm : this.getRemPerms()) {
				if (QuestX.permission.has(p, perm)) {
					QuestX.permission.playerRemove(p, perm);
				}
			}
		}

		if (this.isExecutingPlayerCmds()) {
			QuestXCMDExecutor.executeAsPlayer(p.getName(), this.getPlayerCmds());
		}

		if (this.isExecutingServerCmds()) {
			QuestXCMDExecutor.executeAsServer(this.getServerCmds());
		}

		if (this.fireWorks) {
			Location pL = p.getLocation();
			Fireworks display = new Fireworks(pL, fwRadius, fwSectors);
			display.circularDisplay();
		}

		File cur = FileLocator.getCurrentQuestFile();
		SyncConfiguration cfg = new SyncConfiguration(cur);
		cfg.read();
		cfg.MergeRWArrays();
		cfg.getWriteableData().remove(p.getName());
		cfg.write();
	}

	public void loadAndCheckPlayerProgress(String p) {
		File f = FileLocator.getQuestProgressionPlayerFile(questName, p);
		SyncConfiguration c = new SyncConfiguration(f);
		if (f.exists()) {
			c.read();
			this.playerProgress.put(p, c.getInt("P", 1));
			// QuestX.logMSG("'" + p + "' progress loaded = " + c.getInt("P"));
		} else {
			c.createFileIfNeeded();
			c.add("P", 1);
			c.write();
			this.playerProgress.put(p, 1);
		}
		if (!this.isQuestComplete(p)) {
			this.setPlayerTask(p);
		}
	}

	public void cancel(String p) {
		File cur = FileLocator.getCurrentQuestFile();
		SyncConfiguration cfg = new SyncConfiguration(cur);

		cfg.read();

		for (Entry<String, Object> val : cfg.getReadableData().entrySet()) {
			if (!val.getKey().equalsIgnoreCase(p)) {
				cfg.add(val.getKey(), val.getValue());
			}
		}

		cfg.write();
	}

	public QuestTask getPlayerQuestTask(String p) {
		return this.currentTask.get(p);
	}

	public int getCurrentQuestNode(String p) {
		return this.playerProgress.get(p);
	}

	void setPlayerTask(String p) {
		this.currentTask.put(p, this.tasks[this.playerProgress.get(p) - 1].getClonedInstance());
		QuestTask qtLocal = this.currentTask.get(p);
		if (qtLocal.isGoto()) {
			QuestX.logMSG("GOTO TASK LOADED!");
			GotoLocationController.addLocationTask(p, (GotoLocationTask) qtLocal.getData());
		}
	}

	public synchronized void setTaskComplete(Player player) {
		this.incrementTaskProgress(player);
		if (player != null) {
			QuestX.logChat(player, "Quest task completed!");
		}
	}

	public boolean isQuestComplete(String player) {
		return this.playerProgress.get(player) > this.nodes;
	}

	@SuppressWarnings("deprecation")
	public void incrementTaskProgress(Player p) {
		QuestTask qt = this.currentTask.get(p.getName());

		if (qt.isItemStacks()) {
			qt.removeItems(p);
			p.updateInventory();
		}

		int current = this.playerProgress.get(p.getName()) + 1;
		this.playerProgress.put(p.getName(), current);
		if (current <= +this.nodes) {
			this.currentTask.put(p.getName(), this.tasks[current - 1].getClonedInstance());
		} else {
			this.awardPlayerOnQuestComplete(p);
		}
		SyncConfiguration c = new SyncConfiguration(FileLocator.getQuestProgressionPlayerFile(questName, p.getName()));
		c.add("P", current);
		c.write();

	}

}
