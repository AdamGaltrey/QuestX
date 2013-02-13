package com.adamki11s.ai.dataset;

import java.util.ArrayList;
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

		if (this.rootPoint == null) {
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

		ArrayList<Offset> blackList = new ArrayList<Offset>();

		boolean allow = false;

		do {
			dx = r.nextInt(maxVariation * 2) - maxVariation;
			dy = r.nextInt(maxVariation * 2) - maxVariation;
			dz = r.nextInt(maxVariation * 2) - maxVariation;
			it++;
			if (it > this.failsafeIterations) {
				QuestX.logDebug("ITERATION FAILSAFE TRIGGERED.");
				break;
			}
			if (it % 1000 == 0) {
				QuestX.logDebug("Delta's = (" + dx + ", " + dy + ", " + dz);
			}

			Offset o = new Offset((short) dx, (short) dy, (short) dz);

			allow = true;

			for (Offset b : blackList) {
				if (b.equals(o)) {
					allow = false;
				}
			}

			if (allow && !canMoveHere(w, dx, dy, dz, rx, ry, rz)) {
				blackList.add(o);
				allow = false;
			} else {
				// allow stays true so break out of while loop
			}

		} while (!allow);

		if (it > this.failsafeIterations) {
			QuestX.logDebug("Movement generation failed, MAX iterations");
			this.endPoint = this.rootPoint;
			return;
		} else {
			QuestX.logDebug("Generated movement after " + it + " iterations");
			this.endPoint = new Location(w, (rx + dx), (ry + dy), (rz + dz));
			this.pauseTicks = r.nextInt(maxPauseTicks - minPauseTicks) + minPauseTicks;
		}
	}

	private boolean canMoveHere(World w, int dx, int dy, int dz, int rx, int ry, int rz) {
		if (dx == 0 && dy == 0 && dz == 0) {
			QuestX.logDebug("dx, dy and dx = 0!");
			return false;
		}

		Block b = w.getBlockAt((rx + dx), (ry + dy), (rz + dz));// block it
		// stands on
		/*
		 * return (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1,
		 * 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0 &&
		 * b.getRelative(1, 0, 0).getTypeId() == 0 && b.getRelative(-1, 0,
		 * 0).getTypeId() == 0 && b.getRelative(0, 0, 1).getTypeId() == 0 &&
		 * b.getRelative(0, 0, -1).getTypeId() == 0);
		 */

		/*
		 * if((!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1,
		 * 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0 &&
		 * b.getRelative(1, 0, 0).getTypeId() == 0 && b.getRelative(-1, 0,
		 * 0).getTypeId() == 0 && b.getRelative(0, 0, 1).getTypeId() == 0 &&
		 * b.getRelative(0, 0, -1).getTypeId() == 0)){
		 */

		/*
		 * if (!b.isLiquid() && this.isSolid(b.getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) &&
		 * b.getRelative(0, 2, 0).getTypeId() == 0 &&
		 * canBlockBeWalkedThrough(b.getRelative(1, 0, 0).getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(-1, 0, 0).getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(0, 0, 1).getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(0, 0, -1).getTypeId())) { //
		 * b.setType(Material.EMERALD_BLOCK); return true; } else {
		 * 
		 * return false; }
		 */

		/*
		 * if((b.getTypeId() != 0) && (b.getRelative(0, 1, 0).getTypeId()) == 0
		 * && (b.getRelative(0, 2, 0).getTypeId() == 0)){ return true; } else {
		 * return false; }
		 */

		return this.isSolid(b.getTypeId()) && this.canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) && b.getRelative(0, 2, 0).getTypeId() == 0;

		/*
		 * if (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1,
		 * 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0 &&
		 * b.getRelative(1, 1, 0).getTypeId() == 0 && b.getRelative(-1, 1,
		 * 0).getTypeId() == 0 && b.getRelative(0, 1, 1).getTypeId() == 0 &&
		 * b.getRelative(0, 1, -1).getTypeId() == 0) {
		 * b.setType(Material.EMERALD_BLOCK); return true; } else {
		 * 
		 * return false; }
		 */

		/*
		 * return if(!b.isLiquid() && this.isSolid(b.getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) &&
		 * b.getRelative(0, 2, 0).getTypeId() == 0 &&
		 * canBlockBeWalkedThrough(b.getRelative(1, 0, 0).getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(-1, 0, 0).getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(0, 0, 1).getTypeId()) &&
		 * canBlockBeWalkedThrough(b.getRelative(0, 0, -1).getTypeId()));
		 */
	}

	public Location getEndPoint() {
		return this.endPoint;
	}

	public int getPauseTicks() {
		return this.pauseTicks;
	}

	private boolean isSolid(int i) {
		return (i != 10 && i != 11 && i != 51 && i != 59 && i != 65 && i != 0);
	}

	private boolean canBlockBeWalkedThrough(int id) {
		return (id == 0 || id == 6 || id == 50 || id == 63 || id == 30 || id == 31 || id == 32 || id == 37 || id == 38 || id == 39 || id == 40 || id == 55 || id == 66 || id == 75
				|| id == 76 || id == 78);
	}

}
