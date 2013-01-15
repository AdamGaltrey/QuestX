package couk.adamki11s.dialogue;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

import couk.adamki11s.dialogue.triggers.Trigger;
import couk.adamki11s.dialogue.triggers.TriggerType;
import couk.adamki11s.events.ConversationRegister;
import couk.adamki11s.io.FileLocator;

public class Conversation {

	ConversationData convoData;
	DialogueSet[] dialogue;
	String currentNode = "1";
	boolean conversing = false, indexSelected = false;

	public void respond(String s) {
		int i = 1;
		try {
			i = Integer.parseInt(s);
			this.selectSpeechOption(i);
		} catch (NumberFormatException ex) {
			this.convoData.getPlayer().sendMessage("Invalid option! Select an index. Number only. Eg '1'");
		}
	}

	public Conversation(Player p, HumanNPC npc) {
		this.convoData = new ConversationData(p, npc);
	}

	public void loadConversation() {
		// Find file
		// Invoke DLG Parser
		DLGParser parse = new DLGParser(this, FileLocator.getNPCDlgFile(this.convoData.getNpc().getName()));
		this.dialogue = parse.parse();
	}

	public void startConversation() {
		this.conversing = true;
		this.displaySpeechOptions();
		ConversationRegister.playersConversing.add(this);
	}

	public void endConversation() {
		this.conversing = false;
		ConversationRegister.playersConversing.remove(this);
	}

	public void displaySpeechOptions() {
		DialogueSet d = this.getDialogeSetFromNode(currentNode);
		DialogueItem[] items = d.getItems();
		Player p = this.convoData.getPlayer();
		int count = 1;
		for (DialogueItem di : items) {
			if (di.doesPlayerHaveRequiredRepLevel(p.getName())) {
				p.sendMessage("[#" + count + "] " + di.getSay());
			} else {
				p.sendMessage("[#" + count + "] Unavailable");
			}
			count += 1;
		}
	}

	public void selectSpeechOption(int index) {
		DialogueSet d = this.getDialogeSetFromNode(currentNode);
		DialogueItem[] items = d.getItems();
		DialogueItem selected = items[index - 1];
		Player p = this.convoData.getPlayer();
		if (selected.doesPlayerHaveRequiredRepLevel(p.getName())) {
			Trigger selTrigger = selected.getTrigger();

			DialogueResponse dr = d.getResponse();
			String response = dr.getResponses()[index - 1];
			p.sendMessage("[" + this.convoData.getNpc().getName() + "] " + response);
			System.out.println("Current node = " + this.currentNode);
			this.currentNode = this.currentNode + index;
			System.out.println("Current node = " + this.currentNode);
			if (selTrigger.getTriggerType() == TriggerType.END) {
				this.endConversation();
				return;
			} else {
				this.displaySpeechOptions();
			}
		} else {
			p.sendMessage("You must have at least " + items[index - 1].getRequriedRep().getMinRep() + " reputation.");
		}
	}

	DialogueSet getDialogeSetFromNode(String node) {
		for (DialogueSet dSet : dialogue) {
			if (dSet.getDialogueID().equalsIgnoreCase(node)) {
				return dSet;
			}
		}
		return null;
	}

	public ConversationData getConvoData() {
		return this.convoData;
	}

}
