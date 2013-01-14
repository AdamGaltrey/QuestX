package couk.adamki11s.npcs;

import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

public class NPCHandler {
	
	final NPCManager npc;
	
	public NPCHandler(JavaPlugin main){
		npc = new NPCManager(main);
	}
	
	public NPCManager getNPCManager(){
		return this.npc;
	}
	
	public HumanNPC getNPCByName(String name){
		return (HumanNPC) this.npc.getHumanNPCByName(name);
	}

}
