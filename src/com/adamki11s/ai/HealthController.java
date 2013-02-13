package com.adamki11s.ai;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;

public class HealthController {

	final NPCHandler handle;

	public HealthController(NPCHandler handle) {
		this.handle = handle;
	}
	
	public void run(){
		for (SimpleNPC npc : handle.getNPCs()) {
			if (!npc.isUnderAttack() && npc.isNPCSpawned() && npc.isAttackable() && (npc.getMaxHealth() != npc.getHealth())) {
				npc.restoreHealth(1);
			}
		}
	}

}
