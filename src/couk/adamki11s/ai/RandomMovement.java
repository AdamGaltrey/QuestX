package couk.adamki11s.ai;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.topcat.npclib.entity.HumanNPC;
import couk.adamki11s.ai.dataset.MovementData;
import couk.adamki11s.questx.QuestX;

public class RandomMovement {
	
	final HumanNPC npc;
	MovementData md;
	
	Location rootPoint, currentPoint, targetPoint;
	int minPauseTicks, maxPauseTicks, maxVariation, pauseTicks;
	
	public RandomMovement(HumanNPC npc, Location rootPoint, int minPauseTicks, int maxPauseTicks, int maxVariation){
		md = new MovementData(rootPoint, currentPoint, minPauseTicks, maxPauseTicks, maxVariation);
		
		this.npc = npc;
		this.rootPoint = rootPoint;
		this.currentPoint = npc.getBukkitEntity().getLocation();
		this.maxPauseTicks = maxPauseTicks;
		this.minPauseTicks = minPauseTicks;
		this.maxVariation = maxVariation;
		this.generateNewMovement();
	}
	
	public synchronized void move(){
		this.currentPoint = this.npc.getBukkitEntity().getLocation();
		if(this.currentPoint.distance(this.targetPoint) == 0){
			Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable(){
				public void run(){
					generateNewMovement();
				}
			}, pauseTicks);
		}
	}
	
	public synchronized void generateNewMovement(){
		md.generate();
		this.targetPoint = md.getEndPoint();
		this.pauseTicks = md.getPauseTicks();
		npc.walkTo(this.targetPoint);
		npc.lookAtPoint(this.targetPoint);
	}

}
