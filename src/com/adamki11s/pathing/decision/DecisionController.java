package com.adamki11s.pathing.decision;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;

public class DecisionController {

	final NPCHandler handle;

	public DecisionController(NPCHandler handle) {
		this.handle = handle;
	}

	// location cache, update every 30 seconds.
	SoftReference<ArrayList<SmallLocation>> plCache = new SoftReference<ArrayList<SmallLocation>>(new ArrayList<SmallLocation>());

	private Object lock = new Object();

	public void run() {

		synchronized (lock) {

			ArrayList<SmallLocation> cache = this.updateCache();

			for (SimpleNPC npc : handle.getNPCs()) {
				if (npc.isNPCSpawned()) {
					Location l = npc.getHumanNPC().getBukkitEntity().getLocation();

					small_check: for (SmallLocation sl : cache) {
						boolean inPathRange = sl.isLocationInPathingRange(l);
						// QuestX.logMSG("NPC " + npc.getName() +
						// " is in range ? = " + inPathRange +
						// ". Current state = " + npc.isAllowedToPathFind());
						if (npc.isAllowedToPathFind() != inPathRange) {
							// flip the boolean flag
							npc.invertPathingState();
							// break out, we don't need any more checks
							break small_check;
						}
					}

				}
			}

		}
	}

	private ArrayList<SmallLocation> updateCache() {
		ArrayList<SmallLocation> cache;

		if ((cache = plCache.get()) == null) {
			// QuestX.logMSG("Cache not found, populating");
			cache = new ArrayList<SmallLocation>();
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				cache.add(new SmallLocation(p.getLocation()));
			}

			plCache = new SoftReference<ArrayList<SmallLocation>>(cache);

			// QuestX.logMSG("New cache size = " + cache.size());
			return cache;
		} else {
			// we don't care which small location is mapped to which player,
			// just where they are.

			// QuestX.logMSG("Cache exists, current size = " + cache.size());

			int index = 0, cacheIndexes = cache.size() - 1;
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {

				if (index <= cacheIndexes) {
					cache.get(index).update(p.getLocation());
				} else {
					cache.add(new SmallLocation(p.getLocation()));
				}

				index++;
			}

			plCache = new SoftReference<ArrayList<SmallLocation>>(cache);

			// QuestX.logMSG("Cache updated, new size = " + cache.size());

			return cache;

		}
	}

	public void serverNoPlayersAction() {
		// caled if there are 0 players on the server.
		synchronized(lock){
			this.plCache.clear();
			//stop all npc's from pathing and clear cache
			for(SimpleNPC npc : handle.getNPCs()){
				npc.setPathingState(false);
			}
		}
	}

}
