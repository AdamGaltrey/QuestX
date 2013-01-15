package couk.adamki11s.io;

import java.io.File;

public class FileLocator {
	
	static final String root = "plugins" + File.separator + "QuestX",
	config_root = root + File.separator + "Configuration",
	data_root = root + File.separator + "Data",
	npc_data_root = data_root + File.separator + "NPCs",
	
	dlgFile = "dialogue.dlg", propertyFile = "properties.txt", triggerScriptFile = "trigger.qxs";
	
	public static File getNPCRootDir(String npcName){
		return new File(npc_data_root + File.separator + npcName);
	}
	
	public static File getNPCDlgFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + dlgFile);
	}
	
	public static File getNPCPropertiesFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + propertyFile);
	}
	
	public static File getNPCTriggerFile(String npcName){
		return new File(npc_data_root + File.separator + npcName + File.separator + triggerScriptFile);
	}

}
