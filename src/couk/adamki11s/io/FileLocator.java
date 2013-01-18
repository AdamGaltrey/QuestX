package couk.adamki11s.io;

import java.io.File;

public class FileLocator {
	
	public static final String root = "plugins" + File.separator + "QuestX",
	config_root = root + File.separator + "Configuration",
	data_root = root + File.separator + "Data",
	npc_data_root = data_root + File.separator + "NPCs",
	
	dlgFile = "dialogue.dlg", propertyFile = "properties.txt", taskScript = "task.qxs",questScript = "quest_link.qxs", taskProgression = "task_progression.txt", taskKills = "task_kills.txt";
	
	public static File getNPCRootDir(String npcName){
		return new File(npc_data_root + File.separator + npcName);
	}
	
	public static File getNPCDlgFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + dlgFile);
	}
	
	public static boolean doesNPCDlgFileExist(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + dlgFile).exists();
	}
	
	public static File getNPCPropertiesFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + propertyFile);
	}
	
	public static File getNPCTaskFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + taskScript);
	}
	
	public static File getNPCTaskProgressionFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + "Progression" + taskProgression);
	}
	
	public static File getNPCTaskKillsFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + "Progression" + taskKills);
	}
	
	public static File getNPCQuestLinkFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + questScript);
	}

}
