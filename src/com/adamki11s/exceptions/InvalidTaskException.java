package com.adamki11s.exceptions;

import com.adamki11s.questx.QuestX;

@SuppressWarnings("serial")
public class InvalidTaskException extends Exception {

	final String line, reason, npc;

	public InvalidTaskException(String line, String reason, String npc) {
		this.line = line;
		this.reason = reason;
		this.npc = npc;
	}

	public void printErrorReason() {
		QuestX.logError("Error while parsing task file for NPC '" + this.npc + "'.");
		QuestX.logError("String : " + this.line);
		QuestX.logError("Reason : " + this.reason);
	}

}