package com.adamki11s.ai.dataset;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MovementData {

	Location rootPoint, currentPoint, endPoint, tl, br;
	int minPauseTicks, maxPauseTicks, maxVariation, pauseTicks;

	public MovementData(Location rootPoint, Location currentPoint, int minPauseTicks, int maxPauseTicks, int maxVariation) {
		this.rootPoint = rootPoint;
		tl = rootPoint;
		br = rootPoint;
		this.currentPoint = currentPoint;
		this.minPauseTicks = minPauseTicks;
		this.maxPauseTicks = maxPauseTicks;// max server ticks until the npc
											// will request a new movement
											// packet
		this.maxVariation = maxVariation; // Maximum distance the npc
											// will move from the root location
		tl.subtract(maxVariation, maxVariation, maxVariation);
		br.add(maxVariation, maxVariation, maxVariation);
	}

	final int failsafeIterations = 50;

	public void generate() {
		Random r = new Random();
		int rx = rootPoint.getBlockX(), ry = rootPoint.getBlockY(), rz = rootPoint.getBlockZ(), dx, dy, dz;
		World w = this.rootPoint.getWorld();
		int it = 0;
		do {
			dx = r.nextInt(maxVariation * 2) - maxVariation;
			dy = r.nextInt(maxVariation * 2) - maxVariation;
			dz = r.nextInt(maxVariation * 2) - maxVariation;
			it++;
			if (it > this.failsafeIterations) {
				break;
			}
		} while (!canMoveHere(w, dx, dy, dz, rx, ry, rz));

		if (it > this.failsafeIterations) {
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
		Block b = w.getBlockAt((rx + dx), (ry + dy) - 1, (rz + dz));
		// b.setType(Material.EMERALD_BLOCK);
		return (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1, 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0 && this.isHighestNonAirBlock(b));
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
