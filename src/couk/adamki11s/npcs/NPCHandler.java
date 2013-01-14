package couk.adamki11s.npcs;

import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;

import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

public class NPCHandler {
	
	final NPCManager npc;
	
	HashSet<SimpleNPC> npcList = new HashSet<SimpleNPC>();
	
	public void registerNPC(SimpleNPC npc){
		this.npcList.add(npc);
	}
	
	public void removeNPC(SimpleNPC npc){
		this.npcList.remove(npc);
	}
	
	public HashSet<SimpleNPC> getNPCs(){
		return this.npcList;
	}
	
	public NPCHandler(JavaPlugin main){
		npc = new NPCManager(main);
	}
	
	public NPCManager getNPCManager(){
		return this.npc;
	}
	
	public HumanNPC getNPCByName(String name){
		return (HumanNPC) this.npc.getHumanNPCByName(name);
	}
	
	public SimpleNPC getSimpleNPCByID(String id){
		for(SimpleNPC snpc : this.npcList){
			if(snpc.doesNPCIDMatch(id)){
				return snpc;
			}
		}
		return null;
	}

}
