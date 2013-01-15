package couk.adamki11s.dialogue;

public class DialogueSet{
	
	final DialogueItem[] items;
	final String dialogueID;
	final DialogueResponse response;
	
	public DialogueSet(DialogueItem[] items, DialogueResponse response, String dialogueID){
		this.items = items;
		this.response = response;
		this.dialogueID = dialogueID;
	}
	
	public DialogueSet getNextDialogueSet(int arraySlot){
		//load (dialogueID + arraySlot)
		return null;
	}

}
