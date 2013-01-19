package com.adamki11s.dialogue;

import com.adamki11s.ai.dataset.GenericRepLevel;
import com.adamki11s.dialogue.triggers.Trigger;


public class DialogueResponse {

	private final String[] responses;

	public String[] getResponses() {
		return responses;
	}

	public DialogueResponse(String[] responses) {
		this.responses = responses;
	}

}
