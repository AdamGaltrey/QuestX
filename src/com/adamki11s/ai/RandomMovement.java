package com.adamki11s.ai;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.adamki11s.ai.dataset.MovementData;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.pathing.Tile;
import com.adamki11s.pathing.preset.PathingCache;
import com.adamki11s.pathing.preset.PresetPath;
import com.adamki11s.questx.QuestX;

public class RandomMovement {

	final SimpleNPC npc;
	MovementData md;

	boolean newMDScheduled = false;

	Location rootPoint, currentPoint, targetPoint;
	int minPauseTicks, maxPauseTicks, maxVariation, pauseTicks;

	public RandomMovement(SimpleNPC npc, Location rootPoint, int minPauseTicks, int maxPauseTicks, int maxVariation) {

		md = new MovementData(rootPoint, minPauseTicks, maxPauseTicks, maxVariation);

		this.npc = npc;
		this.rootPoint = rootPoint;
		this.currentPoint = rootPoint;
		this.maxPauseTicks = maxPauseTicks;
		this.minPauseTicks = minPauseTicks;
		this.maxVariation = maxVariation;
		this.generateNewMovement();// link to null
	}

	public void purgeCache() {
		this.md.purgeCache();
	}

	public synchronized void move() {
		this.currentPoint = this.npc.getHumanNPC().getBukkitEntity().getLocation();

		this.currentPoint.subtract(0, 1, 0);

		// if (this.currentPoint.distance(this.targetPoint) == 0) {

		if (!this.npc.isWalkingCustomPath()) {
			if (this.npc.getHumanNPC().isPathFindComplete()) {

				if (!this.newMDScheduled) {
					this.newMDScheduled = true;
					Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable() {
						public void run() {
							if (npc.isMoveable() && npc.isNPCSpawned() && !npc.isUnderAttack() && !npc.isConversing()) {
								generateNewMovement();
							}
						}
					}, pauseTicks);
				}
			}
		} else {
			if (this.npc.getHumanNPC().isPathFindComplete()) {
				if (!this.newMDScheduled) {
					final PresetPath path = npc.getPresetPath();
					if (path == null) {
						System.out.println("Preset path is null");
					} else {
						System.out.println("Preset path is NOT null");
					}
					this.pauseTicks = path.getTickDelay();
					this.newMDScheduled = true;
					Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable() {
						public void run() {
							if (npc.isMoveable() && npc.isNPCSpawned() && !npc.isUnderAttack() && !npc.isConversing()) {
								PathingCache cache = path.getPathingCache();
								if (cache == null) {
									System.out.println("Preset cache is null");
								} else {
									System.out.println("Preset cache is NOT null");
								}
								Tile[] tls = path.getPathingCache().getWalkPath();
								ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(tls));
								for (Tile t : tiles) {
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										p.sendBlockChange(t.getLocation(npc.getHumanNPC().getBukkitEntity().getLocation()), Material.GOLD_BLOCK, (byte) 0);
									}
								}
								npc.getHumanNPC().presetPathFindTo(tiles);
							}
						}
					}, pauseTicks);
				}
			}

		}
	}

	public void setMovementScheduled(boolean v) {
		this.newMDScheduled = v;
	}

	public boolean isMovementScheduled() {
		return this.newMDScheduled;
	}

	public synchronized void generateNewMovement() {
		QuestX.logDebug("Generating MData for '" + this.npc.getName() + "'");
		md.generate();
		Location old = targetPoint;
		this.targetPoint = md.getEndPoint();

		if (old != null && targetPoint != null) {
			QuestX.logDebug("Distance = " + old.distance(targetPoint));
		}

		this.pauseTicks = md.getPauseTicks();
		// npc.getHumanNPC().walkTo(this.targetPoint);

		if (targetPoint.getBlock().getTypeId() == 0) {
			QuestX.logDebug("Target is air!");
		}

		if (currentPoint.getBlock().getTypeId() == 0) {
			QuestX.logDebug("Current is air!");
		}

		npc.getHumanNPC().pathFindTo(currentPoint, targetPoint);
		// npc.getHumanNPC().lookAtPoint(this.targetPoint);
		this.newMDScheduled = false;
	}

}
