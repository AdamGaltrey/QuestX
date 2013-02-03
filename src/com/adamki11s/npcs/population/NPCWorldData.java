package com.adamki11s.npcs.population;

import com.adamki11s.io.WorldConfigData;

public class NPCWorldData {
	
	final String world;
	final int maxSpawns = WorldConfigData.getMaxSpawnsPerWorld();
	int currentSpawns = 0;
	
	public NPCWorldData(String world){
		this.world = world;
	}
	
	public String getWorld(){
		return this.world;
	}
	
	public void incrementSpawnCount(){
		this.currentSpawns++;
	}
	
	public void decrementSpawnCount(){
		this.currentSpawns--;
	}
	
	public boolean canSpawnMore(){
		return (this.currentSpawns < WorldConfigData.getMaxSpawnsPerWorld());
	}

}
