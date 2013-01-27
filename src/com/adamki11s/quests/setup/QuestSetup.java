package com.adamki11s.quests.setup;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;

import com.adamki11s.io.FileLocator;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class QuestSetup {

	public static HashSet<String> settingUp = new HashSet<String>();

	String name, failSetupReason;
	boolean setup;

	final File setupFile = new File(FileLocator.quest_data_root + File.separator + this.name + File.separator + "setup.qxs");

	LinkedList<NPCQuestSpawner> setupGuide = new LinkedList<NPCQuestSpawner>();

	public QuestSetup(String name) {
		this.name = name;
		if (this.setupFile.exists()) {
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

	public boolean canSetup() {
		return this.setup;
	}

	public String getFailSetupReason() {
		return this.failSetupReason;
	}

	public void removeFromList() {
		settingUp.remove(this.name);
	}

	void load() {
		SyncConfiguration cfg = new SyncConfiguration(this.setupFile);
		cfg.read();
	}

}
