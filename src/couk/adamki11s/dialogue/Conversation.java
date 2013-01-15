package couk.adamki11s.dialogue;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

import couk.adamki11s.io.FileLocator;

public class Conversation {

	ConversationData convoData;
	DialogueSet[] dialogue;

	public Conversation(Player p, HumanNPC npc) {
		this.convoData = new ConversationData(p, npc);
	}
	
	public void loadConversation(){
		//Find file
		//Invoke DLG Parser
		DLGParser parse = new DLGParser(this, FileLocator.getNPCDlgFile(this.convoData.getNpc().getName()));
		this.dialogue = parse.parse();
	}
	
	public ConversationData getConvoData(){
		return this.convoData;
	}

}
