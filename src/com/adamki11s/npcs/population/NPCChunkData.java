package com.adamki11s.npcs.population;

import com.adamki11s.io.WorldConfigData;

public class NPCChunkData {
	
	final int x, z;
	
	int spawnCount = 0;
	
	public NPCChunkData(int x, int z) {
		this.x = x;
		this.z = z;
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

}
