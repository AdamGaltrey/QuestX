package couk.adamki11s.dialogue;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.entity.HumanNPC;

import couk.adamki11s.dialogue.triggers.Trigger;
import couk.adamki11s.dialogue.triggers.TriggerType;
import couk.adamki11s.events.ConversationRegister;
import couk.adamki11s.io.FileLocator;
import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;
import couk.adamki11s.npcs.tasks.TaskLoader;
import couk.adamki11s.npcs.tasks.TaskManager;
import couk.adamki11s.npcs.tasks.TaskRegister;
import couk.adamki11s.questx.QuestX;

public class Conversation {

	ConversationData convoData;
	DialogueSet[] dialogue;
	String currentNode = "1";
	boolean conversing = false, indexSelected = false;

	final NPCHandler handle = new NPCHandler((JavaPlugin) QuestX.p);

	public void respond(String s) {
		int i = 1;
		try {
			i = Integer.parseInt(s);
			this.selectSpeechOption(i);
		} catch (NumberFormatException ex) {
			this.convoData.getPlayer().sendMessage("Invalid option! Select an index. Number only. Eg '1'");
		}
	}

	public Conversation(String pName, SimpleNPC npc) {
		this.convoData = new ConversationData(pName, npc);
	}

	public void loadConversation() {
		// Find file
		// Invoke DLG Parser
		DLGParser parse = new DLGParser(this, FileLocator.getNPCDlgFile(this.convoData.getSimpleNpc().getName()));
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

	public boolean isConversing() {
		return this.conversing;
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
		QuestX.logMSG("Items length = " + items.length);
		if(index > items.length || index < 1){
			QuestX.logChat(this.getConvoData().getPlayer(), "Invalid chat option!");
			this.displaySpeechOptions();
			return;
		}
		DialogueItem selected = items[index - 1];
		Player p = this.convoData.getPlayer();
		if (selected.doesPlayerHaveRequiredRepLevel(p.getName())) {
			Trigger selTrigger = selected.getTrigger();

			DialogueResponse dr = d.getResponse();
			String response = dr.getResponses()[index - 1];
			p.sendMessage("[" + this.convoData.getSimpleNpc().getName() + "] " + response);
			System.out.println("Current node = " + this.currentNode);
			this.currentNode = this.currentNode + index;
			System.out.println("Current node = " + this.currentNode);
			System.out.println("Trigger type = " + selTrigger.getTriggerType().toString());
			if (selTrigger.getTriggerType() == TriggerType.END) {
				this.endConversation();
				return;
			} else if (selTrigger.getTriggerType() == TriggerType.TASK) {
				System.out.println("INSIDE TRIGGER code");
				System.out.println("Does player have task = " + TaskRegister.doesPlayerHaveTask(p.getName()));
				if(TaskRegister.doesPlayerHaveTask(p.getName())){
					System.out.println("In has task code");
					QuestX.logChat(p, ChatColor.RED + "You already have a task assigned!");
					QuestX.logChat(p, ChatColor.WHITE + "/questx task cancel" + ChatColor.RED + " to cancel current task.");
					this.endConversation();
					return;
				} else {
					System.out.println("In has NOT task code");
					TaskLoader tl = new TaskLoader(FileLocator.getNPCTaskFile(this.getConvoData().getSimpleNpc().getName()), this.getConvoData().getSimpleNpc().getName());
					QuestX.logMSG("Loading task...");
					tl.load();
					QuestX.logMSG("Task Loaded!");
					TaskManager manage = new TaskManager(p.getName(), tl);
					TaskRegister.registerTask(manage);
					QuestX.logChat(p, ChatColor.ITALIC + tl.getTaskName() + ChatColor.RESET + ChatColor.GREEN + " task started!");
					QuestX.logChat(p, "Task description : " + tl.getTaskDescription());
					p.sendMessage("Not recieving messages?");
					QuestX.logMSG("Not recieving msgs?");
					this.endConversation();
					return;
				}
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
