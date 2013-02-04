package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class MissingTaskPropertyException extends Exception{
	
	final String npc, prop;
	
	public MissingTaskPropertyException(String npc, String property){
		this.npc = npc;
		this.prop = property;
	}
	
	public void printErrorReason(){
		QuestX.logError("##### MissingTaskPropertyException #####");
		QuestX.logError("Missing property '" + this.prop + "' in task file for NPC '" + this.npc + "'.");
		QuestX.logError("##### MissingTaskPropertyException #####");
	}

}
