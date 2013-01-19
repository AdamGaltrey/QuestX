package com.adamki11s.npcs.spawning;

import org.bukkit.Location;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;

public class NPCSpawnController {
	
	public static Location getRespawnLocation(SimpleNPC npc){
		if(npc.isSpawnFixed()){
			return npc.getFixedLocation();
		} else {
			//dynamic loading
			return null;
		}
	}

}
