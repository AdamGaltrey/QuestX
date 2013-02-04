package com.adamki11s.validation;

import com.adamki11s.dialogue.DLGParser;
import com.adamki11s.exceptions.InvalidDialogueException;
import com.adamki11s.exceptions.MissingPropertyException;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.io.LoadNPCTemplate;

public class Validator {
	
	public static void validateAll(){
		
	}
	
	static void validateDialogue(String npc) throws InvalidDialogueException{
		DLGParser.validateParse(FileLocator.getNPCDlgFile(npc), npc);
	}
	
	static void validateProperties(String npc) throws MissingPropertyException{
		LoadNPCTemplate lt = new LoadNPCTemplate(npc);
		lt.loadProperties();
		lt = null;
	}

}
