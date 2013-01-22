package com.adamki11s.ai;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.adamki11s.ai.dataset.MovementData;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;
import com.topcat.npclib.entity.HumanNPC;

public class RandomMovement {

	final SimpleNPC npc;
	MovementData md;

	volatile boolean newMDScheduled = false;

	volatile Location rootPoint, currentPoint, targetPoint;
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
		if (this.currentPoint.distance(this.targetPoint) == 0) {

			if (!this.newMDScheduled) {
				this.newMDScheduled = true;
				QuestX.logMSG("Destination reached, scheduling new movement");
				Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable() {
					public void run() {
						if (npc.isMoveable() && npc.isNPCSpawned() && !npc.isUnderAttack() && !npc.isConversing()) {
							generateNewMovement();
						}
					}
				}, pauseTicks);
			} else {
				QuestX.logMSG("New movement has already been scheduled!");
			}
		}
	}

	public synchronized void generateNewMovement() {
		// QuestX.logMSG("Generating MData for '" + this.npc.getName() + "'");
		md.generate();
		this.targetPoint = md.getEndPoint();
		this.pauseTicks = md.getPauseTicks();
		npc.getHumanNPC().walkTo(this.targetPoint);// throwing null
		npc.getHumanNPC().lookAtPoint(this.targetPoint);
		this.newMDScheduled = false;
	}

}
