package couk.adamki11s.dialogue;

public class DialogueSet{
	
	private final DialogueItem[] items;
	private final String dialogueID;
	private final DialogueResponse response;
	
	public DialogueSet(DialogueItem[] items, DialogueResponse response, String dialogueID){
		this.items = items;
		this.response = response;
		this.dialogueID = dialogueID;
	}

	public DialogueItem[] getItems() {
		return items;
	}

	public String getDialogueID() {
		return dialogueID;
	}

	public DialogueResponse getResponse() {
		return response;
	}
	
	

}
