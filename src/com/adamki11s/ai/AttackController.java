package com.adamki11s.ai;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;
import com.adamki11s.reputation.Reputation;
import com.topcat.npclib.entity.HumanNPC;

public class AttackController {

	HashMap<HumanNPC, Player> target = new HashMap<HumanNPC, Player>();

	final NPCHandler handle;

	public AttackController(NPCHandler handle) {
		this.handle = handle;
	}

	public void run() {
		ArrayList<SimpleNPC> process = new ArrayList<SimpleNPC>();
		for (SimpleNPC npc : handle.getNPCs()) {
			if (npc.isUnderAttack() && npc.isNPCSpawned()) {
				process.add(npc);
			}
		}

		for (SimpleNPC npc : process) {
			this.retalliate(npc.getAggressor(), npc);
		}
	}

	synchronized void retalliate(Player p, SimpleNPC npc) {
		// if(p is inside npc area) then attack
		
		if(p == null || npc == null){
			return;
		}

		Location loc = p.getLocation();
		double var = ((double) npc.getMaxVariation()) * (npc.getRetalliationMultiplier());
		
		Location root = npc.getSpawnedLocation();
		Location bl = new Location(loc.getWorld(), root.getBlockX() - var, root.getBlockY() - var, root.getBlockZ() - var), tr = new Location(loc.getWorld(), root.getBlockX() + var,
				root.getBlockY() + var, root.getBlockZ() + var);

		Location target = p.getLocation().subtract(0, 1, 0);
		
		

		if (loc.getBlockX() > bl.getBlockX() && loc.getBlockY() > bl.getBlockY() && loc.getBlockZ() > bl.getBlockZ() && loc.getBlockX() < tr.getBlockX() && loc.getBlockY() < tr.getBlockY()
				&& loc.getBlockZ() < tr.getBlockZ()) {

			// QuestX.logChat(p, "In attack zone! Size = " +
			// npc.getRetalliationMultiplier() + ", var = " + var);

			/*if (npc.getHumanNPC().isPathFindComplete()) {
				npc.moveTo(p.getLocation().subtract(0, 1, 0));
			}*/
			
			//npc.moveTo(target);
			
			int eCode = npc.getHumanNPC().getEndCode();
			
			//if can't find path cancel
			if(eCode == -1 || eCode == -2){
				//QuestX.logChat(p, "Path not found, cancelling attack");
				npc.unAggro();
				npc.moveTick();
				return;
			}
			
			if(npc.isPathFindingComplete()){
				npc.moveTo(target);
			} else {
				Location e = npc.getHumanNPC().getEndLocation();
				if(e == null){
					npc.stopPathFinding();
					npc.moveTo(target);
				} else {
					double dist = target.distance(e);
					if(dist > 1.8){
						//if distance between end and player is too large re-calculate.
						npc.stopPathFinding();
						npc.moveTo(target);
					} else {
						//do nothing
					}
				}
			}
			
			Location l = p.getLocation();
			l.add(0, 1, 0);
			
			npc.lookAt(l);
			
				
			//npc.lookAt(target.add(0, 1, 0));	

			if (npc.getHumanNPC().getBukkitEntity().getLocation().distance(p.getLocation()) < 2.5) {
				// QuestX.logChat(p, "NPC HIT YOU");
				npc.getHumanNPC().animateArmSwing();
				
				if((p.getHealth() - npc.getDamageMod()) <= 0){
					if(!npc.isPathFindingComplete()){
						npc.stopPathFinding();
					}
					npc.unAggro();
					npc.moveTick();
				}
				
				p.damage(npc.getDamageMod());
				
				if(!npc.isPathFindingComplete()){
					npc.stopPathFinding();
				}
			}

		} else {
			// QuestX.logChat(p, "Left attack zone NPC aggro lost!");
			npc.unAggro();
			npc.moveTick();
			
		}

		// else if aggressor has left area stop attacking
	}
}
