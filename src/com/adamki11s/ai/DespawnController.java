package com.adamki11s.ai;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;

public class DespawnController {
	
	int currentTicks;
	
	final NPCHandler handle;

	public DespawnController(NPCHandler handle) {
		this.handle = handle;
	}

	
	public void run(int tickRate){
		currentTicks += tickRate;
		if(currentTicks >= (20 * 60)){
			QuestX.logDebug("Updating untouched values every min");
			currentTicks = 0;
			for (SimpleNPC npc : handle.getNPCs()) {
				npc.updateUntouchedTicks(20 * 60);
			}
		}
	}

}
