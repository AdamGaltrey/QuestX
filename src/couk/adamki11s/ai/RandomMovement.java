package couk.adamki11s.ai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.topcat.npclib.entity.HumanNPC;
import couk.adamki11s.ai.dataset.MovementData;
import couk.adamki11s.npcs.SimpleNPC;
import couk.adamki11s.questx.QuestX;

public class RandomMovement {

	final SimpleNPC npc;
	MovementData md;

	Location rootPoint, currentPoint, targetPoint;
	int minPauseTicks, maxPauseTicks, maxVariation, pauseTicks;

	public RandomMovement(SimpleNPC npc, Location rootPoint, int minPauseTicks, int maxPauseTicks, int maxVariation) {
		md = new MovementData(rootPoint, currentPoint, minPauseTicks, maxPauseTicks, maxVariation);

		this.npc = npc;
		this.rootPoint = rootPoint;
		this.currentPoint = npc.getHumanNPC().getBukkitEntity().getLocation();
		this.maxPauseTicks = maxPauseTicks;
		this.minPauseTicks = minPauseTicks;
		this.maxVariation = maxVariation;
		this.generateNewMovement();
	}

	public synchronized void move() {
		this.currentPoint = this.npc.getHumanNPC().getBukkitEntity().getLocation();
		if (this.currentPoint.distance(this.targetPoint) == 0) {
			Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable() {
				public void run() {
					if (npc.isMoveable() && npc.isNPCSpawned() && !npc.isUnderAttack() && !npc.isConversing()) {
						generateNewMovement();
					}
				}
			}, pauseTicks);
		}
	}

	public synchronized void generateNewMovement() {
		md.generate();
		this.targetPoint = md.getEndPoint();
		this.pauseTicks = md.getPauseTicks();
		npc.getHumanNPC().walkTo(this.targetPoint);
		npc.getHumanNPC().lookAtPoint(this.targetPoint);
	}

}
