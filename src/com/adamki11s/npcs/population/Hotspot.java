package com.adamki11s.npcs.population;

import java.io.Serializable;
import java.util.HashSet;

@SuppressWarnings("serial")
public class Hotspot implements Serializable{
	
	final private int cx, cy, cz, range, maxSpawns;
	final private String tag, worldName;
	
	HashSet<String> spawnedNPCs = new HashSet<String>();
	
	public Hotspot(int cx, int cy, int cz, int range, int maxSpawns, String tag, String worldName) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.range = range;
		this.maxSpawns = maxSpawns;
		this.tag = tag;
		this.worldName = worldName;
	}

	public int getCx() {
		return cx;
	}

	public int getCy() {
		return cy;
	}

	public int getCz() {
		return cz;
	}

	public int getRange() {
		return range;
	}
	
	public int getMaxSpawns(){
		return this.maxSpawns;
	}
	
	public void addNPC(String npc){
		if(!this.spawnedNPCs.contains(npc)){
			spawnedNPCs.add(npc);
		}
	}
	
	public void removeNPC(String npc){
		if(this.spawnedNPCs.contains(npc)){
			spawnedNPCs.remove(npc);
		}
	}
	
	public int getSpawnedNPCS(){
		return this.spawnedNPCs.size();
	}
	
	public boolean canSpawnMore(){
		return (this.spawnedNPCs.size() < this.maxSpawns);
	}

	public String getTag() {
		return tag;
	}
	
	public String getWorldName(){
		return this.worldName;
	}
	
	

}
