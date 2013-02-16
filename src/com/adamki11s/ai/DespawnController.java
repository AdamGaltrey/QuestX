package com.adamki11s.ai;

import java.util.ArrayList;
import java.util.HashSet;

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
			currentTicks = 0;
			ArrayList<String> desp = new ArrayList<String>();
			for (SimpleNPC npc : handle.getNPCs()) {
				npc.updateUntouchedTicks(20 * 60);
				if (npc.shouldBeDespawned()) {
					desp.add(npc.getName());
				}
			}
			
			for(String n : desp){
				SimpleNPC npc = handle.getSimpleNPCByName(n);
				if(n != null){
					npc.despawnNPC();
				}
			}
		}
	}

}
