package couk.adamki11s.dialogue;

public class DialogueSet {
	
	final DialogueItem[] items;
	final String dialogueID;
	
	public DialogueSet(DialogueItem[] items, String dialogueID){
		this.items = items;
		this.dialogueID = dialogueID;
	}
	
	public DialogueSet getNextDialogueSet(int arraySlot){
		//load (dialogueID + arraySlot)
		return null;
	}

}
