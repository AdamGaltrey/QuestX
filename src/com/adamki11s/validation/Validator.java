package com.adamki11s.validation;

import java.io.File;

import com.adamki11s.dialogue.DLGParser;
import com.adamki11s.exceptions.InvalidDialogueException;
import com.adamki11s.exceptions.MissingPropertyException;
import com.adamki11s.exceptions.MissingTaskPropertyException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.io.LoadNPCTemplate;
import com.adamki11s.npcs.tasks.TaskLoader;
import com.adamki11s.questx.QuestX;

public class Validator {
	
	public static void validateAll(){
		//validate NPC's
		for(File f : new File(FileLocator.npc_data_root).listFiles()){
			if(f.isDirectory()){
				File dlg = FileLocator.getNPCDlgFile(f.getName()),
				props = FileLocator.getNPCPropertiesFile(f.getName()),
				task = FileLocator.getNPCTaskFile(f.getName());
				if(!dlg.exists()){
					QuestX.logError("dialogue.dlg file is missing for NPC '" + f.getName() + "'");
					continue;
				}
				if(!props.exists()){
					QuestX.logError("properties.txt file is missing for NPC '" + f.getName() + "'");
					continue;
				}
				if(!task.exists()){
					QuestX.logError("task.qxs file is missing for NPC '" + f.getName() + "'");
					continue;
				}
				try {
					validateDialogue(f.getName());
					validateProperties(f.getName());
					validateTaskFile(f.getName());
				} catch (InvalidDialogueException e) {
					e.printErrorReason();
				} catch (MissingPropertyException e) {
					e.printErrorReason();
				} catch (MissingTaskPropertyException e) {
					e.printErrorReason();
				}
				
			}
		}
	}
	
	static void validateDialogue(String npc) throws InvalidDialogueException{
		DLGParser.validateParse(FileLocator.getNPCDlgFile(npc), npc);
	}
	
	static void validateProperties(String npc) throws MissingPropertyException{
		LoadNPCTemplate lt = new LoadNPCTemplate(npc);
		lt.loadProperties();
		lt = null;
	}
	
	static void validateTaskFile(String npc) throws MissingTaskPropertyException{
		TaskLoader tl = new TaskLoader(FileLocator.getNPCTaskFile(npc), npc);
		tl.load();
		tl = null;
	}

}
