package com.adamki11s.io;

import java.io.File;

import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class GeneralConfigData {

	static boolean tagAPISupport, checkUpdates, autoDLUpdates, notifyAdmin, extractNPC, extractQuest;

	public static void load() {
		File genConfig = FileLocator.getGeneralConfig();
		SyncConfiguration conf = new SyncConfiguration(genConfig);
		conf.read();
		tagAPISupport = conf.getBoolean("TAG_API_SUPPORT", false);
		checkUpdates = conf.getBoolean("CHECK_UPDATES", true);
		autoDLUpdates = conf.getBoolean("AUTO_DOWNLOAD_UPDATES", false);
		notifyAdmin = conf.getBoolean("NOTIFY_ADMIN", true);
		
		boolean edited = false;

		if (!conf.doesKeyExist("EXTRACT_NPC_PAYLOAD")) {
			edited = true;
			conf.add("EXTRACT_NPC_PAYLOAD", true);
			conf.addComment("Whether to extract the NPC's included with QuestX, note if you set this to false you will not have any NPCs but"
					+ " the ones you create yourself, unless you extract the NPCs from the payload file yourself.");
			extractNPC = true;
		} else {
			extractNPC = conf.getBoolean("EXTRACT_NPC_PAYLOAD", true);
		}

		if (!conf.doesKeyExist("EXTRACT_QUEST_PAYLOAD")) {
			edited = true;
			conf.add("EXTRACT_QUEST_PAYLOAD", true);
			conf.addComment("Whether to extract the Quests and related NPCs included with QuestX, note if you set this to false you will not have any Quests but"
					+ " the ones you create yourself, unless you extract the Quests from the payload file yourself.");
			extractQuest = true;
		} else {
			extractQuest = conf.getBoolean("EXTRACT_QUEST_PAYLOAD", true);
		}
		
		if(edited){
			conf.MergeRWArrays();
			conf.write();
		}

		conf.add("EXTRACT_NPC_PAYLOAD", true);
		conf.add("EXTRACT_QUEST_PAYLOAD", true);
	}

	public static boolean isNotifyAdmin() {
		return notifyAdmin;
	}

	public static boolean isTagAPISupported() {
		return tagAPISupport;
	}

	public static boolean isCheckingUpdates() {
		return checkUpdates;
	}

	public static boolean isAutoDLUpdates() {
		return autoDLUpdates;
	}

	public static boolean isExtractNPCPayload() {
		return extractNPC;
	}

	public static boolean isExtractQuestPayload() {
		return extractQuest;
	}

}
