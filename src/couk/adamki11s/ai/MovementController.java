package couk.adamki11s.ai;

import couk.adamki11s.npcs.NPCHandler;
import couk.adamki11s.npcs.SimpleNPC;

public class MovementController {
	
	final NPCHandler handle;
	
	public MovementController(NPCHandler handle){
		this.handle = handle;
	}
	
	public synchronized void run(){
		for(SimpleNPC npc : handle.getNPCs()){
			if(npc.isMoveable() && npc.isNPCSpawned() && !npc.isUnderAttack()){
				npc.moveTick();
			}
		}
	}

}
