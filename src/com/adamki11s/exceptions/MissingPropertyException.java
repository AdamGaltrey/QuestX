package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class MissingPropertyException extends Exception{
	
	final String npc, prop;
	
	public MissingPropertyException(String npc, String property){
		this.npc = npc;
		this.prop = property;
	}
	
	public void printErrorReason(){
		QuestX.logError("Missing property '" + this.prop + "' for NPC '" + this.npc + "'.");
	}

}
