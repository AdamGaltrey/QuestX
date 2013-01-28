package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class InvalidDialogueException extends Exception {
	
	final String line, reason, npc;
	
	public InvalidDialogueException(String line, String reason, String npc){
		this.line = line;
		this.reason = reason;
		this.npc = npc;
	}
	
	public void printErrorReason(){
		QuestX.logError("Error while parsing dialogue file for NPC '" + this.npc + "'.");
		QuestX.logError("String : " + this.line);
		QuestX.logError("Reason : " + this.reason);
	}

}
