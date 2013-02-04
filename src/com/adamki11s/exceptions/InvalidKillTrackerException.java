package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class InvalidKillTrackerException extends Exception{
	
	final String line, reason;

	public InvalidKillTrackerException(String line, String reason) {
		this.line = line;
		this.reason = reason;
	}

	public void printCustomErrorReason(boolean quest, String cause){
		QuestX.logError("##### InvalidKillTrackerException #####");
		if(quest){
		QuestX.logError("Error while parsing quest file for quest '" + cause + "'.");
		} else {
			QuestX.logError("Error while parsing task file for NPC '" + cause + "'.");
		}
		QuestX.logError("String : " + this.line);
		QuestX.logError("Reason : " + this.reason);
		QuestX.logError("##### InvalidKillTrackerException #####");
	}

}
