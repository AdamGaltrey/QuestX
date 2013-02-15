package com.adamki11s.quests.locations;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class GotoLocationTask {

	private final int x, z, range, rangeCheckVariation;
	private final String world;
	
	//marked as in chunk range
	private boolean marked;

	public GotoLocationTask(Location l, int range) {
		Chunk c = l.getChunk();

		this.x = c.getX();
		this.z = c.getZ();

		this.world = l.getWorld().getName();

		this.range = range;
		
		this.rangeCheckVariation = (int) (Math.ceil((double)range / 16D) * 2);
	}

	public boolean isAtLocation(Location l) {
		int tx = l.getBlockX() + range, ty = l.getBlockY() + range, tz = l.getBlockZ() + range, bx = l.getBlockX() - range, by = l.getBlockY() - range, bz = l.getBlockZ() - range;
		return (l.getWorld().getName().equalsIgnoreCase(world) && tx >= l.getBlockX() && l.getBlockX() >= bx && ty >= l.getBlockY() && l.getBlockY() >= by && tz >= l.getBlockZ() && l
				.getBlockZ() >= bz);
	}

	public boolean isInCheckRange(Location l) {
		Chunk c = l.getChunk();
		int xDist = this.abs(c.getX() - x), zDist = this.abs(c.getZ() - z);
		//start checking if within double the range
		return (rangeCheckVariation > (xDist + zDist));
	}
	
	private int markCheckCount = 0;
	
	public boolean isMarked(){
		markCheckCount++;
		if(markCheckCount > 15){
			//set chunk to be remarked in case the player has deviated from the search area
			this.setMarked(false);
		}
		return this.marked;
	}
	
	public void setMarked(boolean mark){
		this.marked = mark;
	}

	private int abs(int i) {
		return (i < 0 ? -i : i);
	}

}
