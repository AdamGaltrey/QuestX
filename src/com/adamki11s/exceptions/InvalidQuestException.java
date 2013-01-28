package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class InvalidQuestException extends Exception {

	final String line, reason, quest;

	public InvalidQuestException(String line, String reason, String quest) {
		this.line = line;
		this.reason = reason;
		this.quest = quest;
	}

	public void printErrorReason() {
		QuestX.logError("Error while parsing quest file for quest '" + this.quest + "'.");
		QuestX.logError("String : " + this.line);
		QuestX.logError("Reason : " + this.reason);
	}

}
