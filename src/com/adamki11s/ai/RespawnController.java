package com.adamki11s.ai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;

public class RespawnController {
	
	final NPCHandler handle;
	final long timeBetweenSpawns = 200L;
	long counter = 0;

	public RespawnController(NPCHandler handle) {
		this.handle = handle;
	}
	
	public void run(int tickRate){
		for(SimpleNPC npc : handle.getNPCs()){
			npc.updateWaitedSpawnTicks(tickRate);
		}
		counter += tickRate;
		if(counter > this.timeBetweenSpawns){
			//spawn
			for(World w : Bukkit.getServer().getWorlds()){
				if(this.handle.canNPCBeSpawned(w.getName())){
					Location l = this.handle.getDispatcher(w.getName()).getSpawnLocation();
					
				}
			}
			counter = 0;
		}
	}

}
