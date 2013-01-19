package com.adamki11s.ai;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;

public class RespawnController {
	
	final NPCHandler handle;

	public RespawnController(NPCHandler handle) {
		this.handle = handle;
	}
	
	public void run(int tickRate){
		for(SimpleNPC npc : handle.getNPCs()){
			npc.updateWaitedSpawnTicks(tickRate);
		}
	}

}
