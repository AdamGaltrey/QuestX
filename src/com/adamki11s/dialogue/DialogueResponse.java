package com.adamki11s.dialogue;

import com.adamki11s.dialogue.triggers.Trigger;
import com.adamki11s.reputation.GenericRepLevel;


public class DialogueResponse {

	private final String[] responses;

	public String[] getResponses() {
		return responses;
	}

	public DialogueResponse(String[] responses) {
		this.responses = responses;
	}

}
