package couk.adamki11s.npcs;

public class MovementController {
	
	final NPCHandler handle;
	
	public MovementController(NPCHandler handle){
		this.handle = handle;
	}
	
	public synchronized void run(){
		for(SimpleNPC npc : handle.getNPCs()){
			if(npc.isMoveable() && npc.isSpawned){
				npc.moveTick();
			}
		}
	}

}
