package com.adamki11s.ai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.adamki11s.ai.dataset.MovementData;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;
import com.topcat.npclib.entity.HumanNPC;

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

	public synchronized void move() {
		this.currentPoint = this.npc.getHumanNPC().getBukkitEntity().getLocation();
		
		this.currentPoint.subtract(0, 1, 0);
		
		QuestX.logDebug("Current btype = " + this.currentPoint.getBlock().getType().toString());
		// if (this.currentPoint.distance(this.targetPoint) == 0) {
		if (this.npc.getHumanNPC().isPathFindComplete()) {

			if (!this.newMDScheduled) {
				this.newMDScheduled = true;
				QuestX.logDebug("Destination reached, scheduling new movement");
				Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable() {
					public void run() {
						if (npc.isMoveable() && npc.isNPCSpawned() && !npc.isUnderAttack() && !npc.isConversing()) {
							generateNewMovement();
						}
					}
				}, pauseTicks);
			} else {
				QuestX.logDebug("New movement has already been scheduled!");
			}
		}
	}

	public void setMovementScheduled(boolean v){
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
		
		if(targetPoint.getBlock().getTypeId() == 0){
			QuestX.logDebug("Target is air!");
		}
		
		if(currentPoint.getBlock().getTypeId() == 0){
			QuestX.logDebug("Current is air!");
		}
		
		npc.getHumanNPC().pathFindTo(currentPoint, targetPoint);
		// npc.getHumanNPC().lookAtPoint(this.targetPoint);
		this.newMDScheduled = false;
	}

}
