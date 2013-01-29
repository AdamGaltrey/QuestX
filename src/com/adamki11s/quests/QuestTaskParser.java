package com.adamki11s.quests;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.npcs.tasks.EntityKillTracker;
import com.adamki11s.npcs.tasks.ISAParser;
import com.adamki11s.npcs.tasks.NPCKillTracker;
import com.adamki11s.npcs.tasks.NPCTalkTracker;
import com.adamki11s.questx.QuestX;

public class QuestTaskParser {

	public static synchronized QuestTask getTaskObject(String input, QType type, String cause) {
		Object o = null;
		QuestX.logMSG("QTYPE = " + type.toString());
		QuestX.logMSG("ENTERING SWITCH");
		QuestX.logMSG("SWITCHING CHECKS = " + type);
		
		int lio = input.lastIndexOf(":");
		String completeNodeText = input.substring(lio + 1);
		String npcName = input.substring((input.substring(0, lio).lastIndexOf(":") + 1), lio);
		String inputData = input.substring(0, (input.substring(0, lio).lastIndexOf(":")));
		QuestX.logMSG("CNT = " + completeNodeText + ", npcn = " + npcName + " inputData = " + inputData);
		
		if(type == QType.FETCH_ITEMS){
			o = getInventoryObject(inputData, cause);
		} else if(type == QType.KILL_ENTITIES){
			o = getEntityKillObject(inputData);
		} else if(type == QType.KILL_NPC){
			o = getNPCKillObject(inputData);
		} else {
			o = new NPCTalkTracker(inputData);
		}
		
		QuestX.logMSG("LEFT SWITCH");
		QuestX.logMSG("Starting task parse***********");
		
		
		return new QuestTask(type, o, npcName, completeNodeText);
	}

	static ItemStack[] getInventoryObject(String input, String cause) {
		QuestX.logMSG("Parsing isa array");
		try {
			return ISAParser.parseISA(input, cause, true);
		} catch (InvalidISAException e) {
			e.printErrorReason();
		}
		return null;
	}

	static EntityKillTracker getEntityKillObject(String input) {
		return new EntityKillTracker(input);
	}

	static NPCKillTracker getNPCKillObject(String input) {
		return new NPCKillTracker(input);
	}

}
