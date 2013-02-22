package com.adamki11s.validation;

import java.io.File;

import com.adamki11s.dialogue.DLGParser;
import com.adamki11s.exceptions.InvalidDialogueException;
import com.adamki11s.exceptions.InvalidQuestException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.io.LoadNPCTemplate;
import com.adamki11s.npcs.tasks.TaskLoader;
import com.adamki11s.quests.QuestLoader;
import com.adamki11s.questx.QuestX;

public class Validator {

	public static void validateAll() {
		// validate NPC's
		for (File f : new File(FileLocator.npc_data_root).listFiles()) {
			if (f.isDirectory()) {
				File dlg = FileLocator.getNPCDlgFile(f.getName()), props = FileLocator.getNPCPropertiesFile(f.getName()), task = FileLocator.getNPCTaskFile(f.getName());
				if (!dlg.exists()) {
					QuestX.logError("dialogue.dlg file is missing for NPC '" + f.getName() + "'");
					continue;
				}
				if (!props.exists()) {
					QuestX.logError("properties.txt file is missing for NPC '" + f.getName() + "'");
					continue;
				}
				if (!task.exists()) {
					QuestX.logError("task.qxs file is missing for NPC '" + f.getName() + "'");
					continue;
				}

				try {
					validateDialogue(f.getName());
				} catch (InvalidDialogueException e) {
					e.printErrorReason();
				}
				validateProperties(f.getName());
				validateTaskFile(f.getName());

			}
		}

		// validate quests
		for (File f : new File(FileLocator.quest_data_root).listFiles()) {
			if (f.isDirectory()) {
				File qFile = FileLocator.getQuestFile(f.getName());
				if (qFile.exists()) {
					try {
						validateQuestFile(f.getName());
					} catch (InvalidQuestException e) {
						e.printErrorReason();
					}
				} else {
					QuestX.logError("quest.qxs file is missing for Quest '" + f.getName() + "'");
					continue;
				}
			}
		}
	}

	static void validateDialogue(String npc) throws InvalidDialogueException {
		DLGParser.validateParse(FileLocator.getNPCDlgFile(npc), npc);
	}

	static void validateProperties(String npc){
		LoadNPCTemplate lt = new LoadNPCTemplate(npc);
		lt.loadProperties();
		lt = null;
	}

	static void validateTaskFile(String npc) {
		TaskLoader tl = new TaskLoader(FileLocator.getNPCTaskFile(npc), npc);
		tl.load();
		tl = null;
	}

	static void validateQuestFile(String quest) throws InvalidQuestException {
		new QuestLoader(FileLocator.getQuestFile(quest));
	}

}
