package couk.adamki11s.npcs;

import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.NPCManager;

public class NPCHandler {
	
	final NPCManager npc;
	
	public NPCHandler(JavaPlugin main){
		npc = new NPCManager(main);
	}
	
	public NPCManager getNPCManager(){
		return this.npc;
	}

}
