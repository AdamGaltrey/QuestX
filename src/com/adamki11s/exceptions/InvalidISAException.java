package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

public class InvalidISAException extends Exception {

	final String line, reason, cause;
	final boolean quest;

	public InvalidISAException(String line, String reason, String cause, boolean quest) {
		this.line = line;
		this.reason = reason;
		this.cause = cause;
		this.quest = quest;
	}

	public void printErrorReason() {
		if (!quest) {
			QuestX.logError("Error while parsing ItemStack Array String for NPC '" + this.cause + "'.");
		} else {
			QuestX.logError("Error while parsing ItemStack Array String for Quest '" + this.cause + "'.");
		}
		QuestX.logError("String : " + this.line);
		QuestX.logError("Reason : " + this.reason);
	}

}
