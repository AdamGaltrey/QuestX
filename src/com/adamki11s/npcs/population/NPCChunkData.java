package com.adamki11s.npcs.population;

public class NPCChunkData {
	
	final int x, z;
	final String world;
	
	int spawnCount = 0;
	
	public NPCChunkData(int x, int z, String world) {
		this.x = x;
		this.z = z;
		this.world = world;
	}
	
	public void increaseSpawnCount(){
		this.spawnCount++;
	}
	
	public void decreaseSpawnCount(){
		this.spawnCount--;
	}
	
	public boolean canSpawnMore(){
		return (this.spawnCount < WorldConfigData.getMaxSpawnsPerChunk());
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public String getWorld() {
		return world;
	}
	
	

}
