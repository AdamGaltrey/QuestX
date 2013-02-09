package com.adamki11s.ai;

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

	public synchronized void run() {
		for (SimpleNPC npc : handle.getNPCs()) {
			if (npc.isUnderAttack() && npc.isNPCSpawned()) {
				this.retalliate(npc.getAggressor(), npc);
			}
		}
	}

	synchronized void retalliate(Player p, SimpleNPC npc) {
		// if(p is inside npc area) then attack

		Location loc = p.getLocation();
		double var = ((double)npc.getMaxVariation()) * (npc.getRetalliationMultiplier()); //Adds var * RetalMultiplier to the attack radius
		Location root = npc.getSpawnedLocation();
		Location bl = new Location(loc.getWorld(), root.getBlockX() - var, root.getBlockY() - var, root.getBlockZ() - var), tr = new Location(loc.getWorld(), root.getBlockX()
				+ var, root.getBlockY() + var, root.getBlockZ() + var);

		if (loc.getBlockX() > bl.getBlockX() && loc.getBlockY() > bl.getBlockY() && loc.getBlockZ() > bl.getBlockZ() && loc.getBlockX() < tr.getBlockX()
				&& loc.getBlockY() < tr.getBlockY() && loc.getBlockZ() < tr.getBlockZ()) {

			//QuestX.logChat(p, "In attack zone! Size = " + npc.getRetalliationMultiplier() + ", var = " + var);
			
			npc.moveTo(p.getLocation());
			npc.lookAt(p.getLocation());

			if (npc.getHumanNPC().getBukkitEntity().getLocation().distance(p.getLocation()) < 2) {
				//QuestX.logChat(p, "NPC HIT YOU");
				npc.getHumanNPC().animateArmSwing();
				p.damage(npc.getDamageMod());
			}

		} else {
			//QuestX.logChat(p, "Left attack zone NPC aggro lost!");
			npc.unAggro();
			npc.moveTick();
		}

		// else if aggressor has left area stop attacking
	}

}
