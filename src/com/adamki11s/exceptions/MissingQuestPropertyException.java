package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class MissingQuestPropertyException extends Exception{
	
	final String quest, prop;
	
	public MissingQuestPropertyException(String quest, String property){
		this.quest = quest;
		this.prop = property;
	}
	
	public void printErrorReason(){
		QuestX.logError("##### MissingQuestPropertyException #####");
		QuestX.logError("Missing property '" + this.prop + "' in quest file for Quest '" + this.quest + "'.");
		QuestX.logError("##### MissingQuestPropertyException #####");
	}

}
