package com.adamki11s.npcs.population;

public class NPCWorldData {
	
	final String world;
	final int maxSpawns = WorldConfigData.getMaxSpawnsPerWorld();
	int currentSpawns;
	
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
		return (this.currentSpawns < WorldConfigData.maxSpawnsPerWorld);
	}

}
