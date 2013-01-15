package couk.adamki11s.dialogue;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.dialogue.triggers.Trigger;

public class DialogueResponse {
	
	final String[] responses;
	final GenericRepLevel[] repRequired;
	final Trigger[] triggers;
	
	public DialogueResponse(String[] responses, GenericRepLevel[] repRequired, Trigger[] triggers) {
		this.responses = responses;
		this.repRequired = repRequired;
		this.triggers = triggers;
	}

}
