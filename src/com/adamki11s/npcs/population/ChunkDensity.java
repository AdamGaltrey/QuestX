package com.adamki11s.npcs.population;

public class ChunkDensity {
	
	final int x, z;
	int density = 1;
	
	public ChunkDensity(int x, int z){
		this.x = x;
		this.z = z;
	}
	
	public void increment(){
		this.density++;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getZ(){
		return this.z;
	}
	
	public int getIncrement(){
		return this.density;
	}
	
	public boolean isDuplicate(int x, int z){
		return (this.getX() == x && this.getZ() == z);
	}

}
