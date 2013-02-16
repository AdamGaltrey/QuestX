package com.adamki11s.quests;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.exceptions.InvalidKillTrackerException;
import com.adamki11s.exceptions.InvalidQuestException;
import com.adamki11s.npcs.tasks.EntityKillTracker;
import com.adamki11s.npcs.tasks.ISAParser;
import com.adamki11s.npcs.tasks.NPCKillTracker;
import com.adamki11s.npcs.tasks.NPCTalkTracker;
import com.adamki11s.quests.locations.GotoLocationTask;
import com.adamki11s.questx.QuestX;

public class QuestTaskParser {

	public static synchronized QuestTask getTaskObject(String quest, String input, QType type, String cause) throws InvalidKillTrackerException, InvalidISAException, InvalidQuestException {
		Object o = null;
		QuestX.logDebug("QTYPE = " + type.toString());
		QuestX.logDebug("ENTERING SWITCH");
		QuestX.logDebug("SWITCHING CHECKS = " + type);

		int lio = input.lastIndexOf(":");
		String completeNodeText = input.substring(lio + 1);
		String npcName = input.substring((input.substring(0, lio).lastIndexOf(":") + 1), lio);
		String inputData = input.substring(0, (input.substring(0, lio).lastIndexOf(":")));
		QuestX.logDebug("CNT = " + completeNodeText + ", npcn = " + npcName + " inputData = " + inputData);

		if (type == QType.FETCH_ITEMS) {
			o = getInventoryObject(inputData, cause);
		} else if (type == QType.KILL_ENTITIES) {
			o = getEntityKillObject(inputData);
		} else if (type == QType.KILL_NPC) {
			o = getNPCKillObject(inputData);
		} else if (type == QType.GOTO) {
			o = getLocationObject(quest, inputData);
		} else {
			o = new NPCTalkTracker(inputData);
		}

		QuestX.logDebug("LEFT SWITCH");
		QuestX.logDebug("Starting task parse***********");

		return new QuestTask(type, o, npcName, completeNodeText);
	}

	static ItemStack[] getInventoryObject(String input, String cause) throws InvalidISAException {
		QuestX.logDebug("Parsing isa array");
		return ISAParser.parseISA(input, cause, true);
	}

	static EntityKillTracker getEntityKillObject(String input) throws InvalidKillTrackerException {
		return new EntityKillTracker(input);
	}

	static NPCKillTracker getNPCKillObject(String input) {
		return new NPCKillTracker(input);
	}
	
	static GotoLocationTask getLocationObject(String quest, String input) throws InvalidQuestException{
		return new GotoLocationTask(quest, input);
	}

}
