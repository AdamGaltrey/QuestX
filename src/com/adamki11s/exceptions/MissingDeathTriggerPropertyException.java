package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class MissingDeathTriggerPropertyException extends Exception {
	
final String npc, prop;
	
	public MissingDeathTriggerPropertyException(String npc, String property){
		this.npc = npc;
		this.prop = property;
	}
	
	public void printErrorReason(){
		QuestX.logError("##### MissingDeathTriggerPropertyException #####");
		QuestX.logError("Missing property '" + this.prop + "' for NPC '" + this.npc + "'.");
		QuestX.logError("##### MissingDeathTriggerPropertyException #####");
	}

}
