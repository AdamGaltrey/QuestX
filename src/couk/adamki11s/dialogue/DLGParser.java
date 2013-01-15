package couk.adamki11s.dialogue;

import java.io.File;
import java.util.ArrayList;

import couk.adamki11s.ai.dataset.GenericRepLevel;
import couk.adamki11s.dialogue.triggers.Trigger;
import couk.adamki11s.dialogue.triggers.TriggerType;
import couk.adamki11s.io.SyncWriter;

public class DLGParser {

	final File f;
	final SyncWriter io;

	public DLGParser(File f) {
		this.f = f;
		this.io = new SyncWriter(f);
	}

	public Conversation parse() {
		io.read();
		ArrayList<String> lines = io.getReadableContents();
		// Load all single dialogue items first then we can add replys
		for (String line : lines) {
			int firstHashIndex = line.indexOf("#");
			int secondHashIndex = line.indexOf("#", firstHashIndex);
			String responseType = (line.substring(firstHashIndex, secondHashIndex));
			if (responseType.equalsIgnoreCase("reply")) {
				continue;
			} else {
				// SingleDialogueItem Create
				String[] parts = line.split("#");
				String nodeID = parts[0];
				int options = Integer.parseInt(parts[2]);
				String[] speechOptions = new String[options];
				String[] gTagIds = new String[options];
				String[] trigIds = new String[options];
				for(int i = 1; i <= options; i++){
					speechOptions[i - 1] = parts[2 + i];
					gTagIds[i - 1] = parts[2 + (options) + i];
					trigIds[i - 1] = parts[2 + (options * 2) + i];
				}
				
				GenericRepLevel[] gRepLevels = new GenericRepLevel[options];
				TriggerType[] triggerTypes = new TriggerType[options];
				SingleDialogueItem[] dialogues = new SingleDialogueItem[options];
				
				for(int i = 1; i <= options; i++){
					gRepLevels[i - 1] = GenericRepLevel.parseRepLevel(gTagIds[i - 1]);
					triggerTypes[i - 1] = TriggerType.parseTriggerType(trigIds[i - 1]);
					dialogues[i - 1] = new SingleDialogueItem(speechOptions[i - 1], gRepLevels[i - 1], triggerTypes[i - 1]);
				}
				
				
			}
		}
		return null;
	}

	/*
	 * 
	 * FORMAT ------
	 * DialogueSet_ID#DialogueAction#OptionsNumber#"speech1"#"speech2"
	 * #"speech3"#GTAG1#GTAG2#GTAG3#TRIG1#TRIG2#TRIG3
	 * 
	 * DialogueAction : say,
	 * reply Speech : String of text Generic Tag : e[evil], b[bad], o[ordinary],
	 * g[good], h[hero], a[any] Trigger Tag : n[none], q[quest], t[task],
	 * e[end_convo] ------
	 */

	/*
	 * 
	 * 
	 * 0#say#2#"Hello There."#"Bye!"#a,a#n,e
	 * 
	 * 0#reply#2#"Hi there %pname%, what can i do for you?","Clear out criminal"
	 * #a,b#n,e
	 * 
	 * 01#say#1#"Where am I?"#a#n
	 * 
	 * 01#reply#1#"The World of Minecraft"#a#n
	 * 
	 * 011#say#2#"Ok, thanks"#"Goodbye friend"#a,a#e,e
	 */

}
