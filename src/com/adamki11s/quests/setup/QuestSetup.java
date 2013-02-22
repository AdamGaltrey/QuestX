package com.adamki11s.quests.setup;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.entity.Player;

import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.loading.FixedLoadingTable;
import com.adamki11s.quests.QuestManager;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class QuestSetup {

	public static HashSet<String> settingUp = new HashSet<String>();

	String name, failSetupReason;
	boolean setup;
	int nodes = 0, currentNode = 0;

	final NPCHandler handle;

	HashMap<Integer, NPCQuestSpawner> setupGuide = new HashMap<Integer, NPCQuestSpawner>();

	public QuestSetup(String name, NPCHandler handle) {
		this.name = name;
		this.handle = handle;
		if (!QuestManager.hasQuestBeenSetup(name)) {
			if (!settingUp.contains(name)) {
				this.setup = true;
				settingUp.add(name);
				this.load();
			} else {
				failSetupReason = "Someone else is setting up this Quest.";
				this.setup = false;
			}
		} else {
			failSetupReason = "Quest has already been setup, no setup file exists.";
			this.setup = false;
		}
	}

	public boolean isSetupComplete() {
		return (this.currentNode >= this.nodes);
	}
	
	public void sendInitialMessage(Player p){
		String npcName = setupGuide.get(currentNode + 1).getNpcName();
		QuestX.logChat(p, "Setup progress (" + currentNode + "/" + nodes + ")");
		QuestX.logChat(p, "Choose spawn location for npc '" + npcName + "'.");
	}

	public void setupSpawn(Player p) {
		String npcName = setupGuide.get(currentNode + 1).getNpcName();
		QuestX.logDebug("Current NPC setup name = " + npcName);

		if (this.currentNode == this.nodes - 1) {
			QuestX.logChat(p, "Setup complete!");
		} else {
			QuestX.logChat(p, "Setup progress (" + currentNode + "/" + nodes + ")");
			QuestX.logChat(p, "Choose spawn location for npc '" + npcName + "'.");
		}

		
		FixedLoadingTable.addFixedNPCSpawn(p, npcName, p.getLocation(), handle);
		
		this.currentNode++;
	}

	public boolean canSetup() {
		return this.setup;
	}

	public String getFailSetupReason() {
		return this.failSetupReason;
	}

	public void removeFromList() {
		QuestX.logDebug("~~~~~~ Removing Setup FILE");
		settingUp.remove(this.name);
		File f = new File(FileLocator.quest_data_root + File.separator + this.name + File.separator + "setup.qxs");
		f.delete();
	}

	void load() {
		File setupFile = new File(FileLocator.quest_data_root + File.separator + name + File.separator + "setup.qxs");
		SyncConfiguration cfg = new SyncConfiguration(setupFile);
		cfg.read();
		String setupName = cfg.getString("Name", name);
		if (!setupName.equalsIgnoreCase(this.name)) {
			this.setup = false;
			this.failSetupReason = "Quest setup file name does not match the quest name. Expected '" + name + "', got '" + setupName + "'";
			return;
		} else {
			int i = 0;
			while (cfg.doesKeyExist((i + 1) + "")) {
				i++;
			}
			this.nodes = i;

			for (int c = 1; c <= this.nodes; c++) {
				String raw = cfg.getString(c + "", "");
				String[] splits = raw.split("#");
				String npcName = splits[0];
				String desc = splits[1];
				QuestX.logDebug("Loaded npcName = " + npcName + ", desc = " + desc);
				setupGuide.put(c, new NPCQuestSpawner(npcName, desc));
			}
		}
		/*
		 * String npcName = args[1]; boolean suc =
		 * FixedLoadingTable.addFixedNPCSpawn(p, npcName, p.getLocation(),
		 * handle); if (suc) {
		 * this.handle.getSimpleNPCByName(npcName).spawnNPC(); }
		 */

	}

}
