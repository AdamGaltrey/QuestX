package com.adamki11s.ai.dataset;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.adamki11s.questx.QuestX;

public class MovementData {

	Location rootPoint, endPoint;
	int minPauseTicks, maxPauseTicks, maxVariation, pauseTicks;

	public MovementData(Location rootPoint, int minPauseTicks, int maxPauseTicks, int maxVariation) {
		this.rootPoint = rootPoint;
		
		if(this.rootPoint == null){
			QuestX.logDebug("NULL ROOT POINT PASSED TO MD");
		}
		
		this.minPauseTicks = minPauseTicks;
		this.maxPauseTicks = maxPauseTicks;// max server ticks until the npc
											// will request a new movement
											// packet
		this.maxVariation = maxVariation; // Maximum distance the npc
											// will move from the root location
	}

	final int failsafeIterations = 1000000;

	public void generate() {
		Random r = new Random();
		int rx = rootPoint.getBlockX(), ry = rootPoint.getBlockY(), rz = rootPoint.getBlockZ(), dx, dy, dz;
		World w = this.rootPoint.getWorld();
		int it = 0;
		do {
			dx = r.nextInt(maxVariation * 2) - maxVariation;
			dy = r.nextInt(maxVariation * 2) - maxVariation;
			/*
			 * add extra checks to stop 'jumping' into caves
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			dz = r.nextInt(maxVariation * 2) - maxVariation;
			it++;
			if (it > this.failsafeIterations) {
				QuestX.logDebug("ITERATION FAILSAFE TRIGGERED.");
				break;
			}
			if(it % 1000 == 0){
				QuestX.logDebug("Delta's = (" + dx + ", " + dy + ", " + dz);
			}
		} while (!canMoveHere(w, dx, dy, dz, rx, ry, rz));

		if (it > this.failsafeIterations) {
			this.endPoint = this.rootPoint;
			return;
		} else {
			this.endPoint = new Location(w, (rx + dx), (ry + dy) - 1, (rz + dz));
			this.pauseTicks = r.nextInt(maxPauseTicks - minPauseTicks) + minPauseTicks;
		}
	}

	private boolean canMoveHere(World w, int dx, int dy, int dz, int rx, int ry, int rz) {
		if (dx == 0 && dy == 0 && dz == 0) {
			return false;
		}
		Block b = w.getBlockAt((rx + dx), (ry + dy) - 1, (rz + dz));//block it stands on
		// b.setType(Material.EMERALD_BLOCK);
		return (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1, 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0);
	}
	
	private boolean isHighestNonAirBlock(Block b){
		for(int i = 128; i > 1; i--){
			Location l = new Location(b.getWorld(),b.getX(), i, b.getZ());
			if(b.getWorld().getBlockAt(l).getTypeId() == 0){
				continue;
			} else {
				return (b.getY() == l.getY());
			}
		}
		return true;
	}

	public Location getEndPoint() {
		return this.endPoint;
	}

	public int getPauseTicks() {
		return this.pauseTicks;
	}

}
