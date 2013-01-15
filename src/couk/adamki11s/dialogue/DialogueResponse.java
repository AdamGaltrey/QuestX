package couk.adamki11s.dialogue;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.dialogue.triggers.Trigger;

public class DialogueResponse {

	private final String[] responses;

	public String[] getResponses() {
		return responses;
	}

	public DialogueResponse(String[] responses) {
		this.responses = responses;
	}

}
