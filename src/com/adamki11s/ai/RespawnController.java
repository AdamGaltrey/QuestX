package com.adamki11s.ai;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.io.LoadNPCTemplate;
import com.adamki11s.npcs.io.NPCTemplate;
import com.adamki11s.npcs.population.HotspotManager;
import com.adamki11s.questx.QuestX;

public class RespawnController {

	final NPCHandler handle;
	final long timeBetweenSpawns = 200L, timeBetweenNewLoads = 100L;
	long counterBetweenSpawns = 0, counterBetweenLoads = 0;

	public RespawnController(NPCHandler handle) {
		this.handle = handle;
	}

	public void run(int tickRate) {
		for (SimpleNPC npc : handle.getNPCs()) {
			npc.updateWaitedSpawnTicks(tickRate);
		}

		counterBetweenSpawns += tickRate;
		counterBetweenLoads += tickRate;

		if (counterBetweenLoads > this.timeBetweenNewLoads) {
			String[] preloaded = this.handle.getLoadedOrWaiting();

			boolean areAllLoaded = true;

			if (preloaded == null || preloaded.length < 1) {
				areAllLoaded = false;
			}

			for (File f : new File(FileLocator.npc_data_root).listFiles()) {
				for (String pre : preloaded) {
					if (!pre.equalsIgnoreCase(f.getName())) {
						areAllLoaded = false;
					}
				}
			}

			if (!areAllLoaded) {

				String npcNameToLoad;
				for (File f : new File(FileLocator.npc_data_root).listFiles()) {
					boolean loaded = false;
					for (String pre : preloaded) {
						if (f.getName().equalsIgnoreCase(pre) || f.getName().contains(".zip")) {
							loaded = true;
						}
					}
					if (!loaded) {
						QuestX.logDebug("Trying to load '" + f.getName());
						npcNameToLoad = f.getName();
						LoadNPCTemplate tempLoader = new LoadNPCTemplate(npcNameToLoad);
						tempLoader.loadProperties();
						if (tempLoader.wantsToLoad()) {
							NPCTemplate temp = tempLoader.getLoadedNPCTemplate();
							temp.addSimpleNPCToWaitingList(this.handle);
							QuestX.logDebug("Added NPC '" + npcNameToLoad + "' to waiting list");
							break;
						} else {
							QuestX.logDebug("NPC '" + npcNameToLoad + "' load=false, finding another");
							loaded = true;
						}
						// load into waiting list
					}
				}

			} else {
				QuestX.logDebug("All NPC's are loaded, skipping");
			}
			this.counterBetweenLoads = 0;

		}

		if (counterBetweenSpawns > this.timeBetweenSpawns) {
			QuestX.logDebug("Checking to make a new spawn");
			if (this.handle.isWaitingQueueEmpty()) {
				QuestX.logDebug("No spawns in line, breaking");
			} else {
				QuestX.logDebug("Spawns in line, checking worlds");
				// spawn
				for (String w : this.handle.getWorlds()) {
					if (this.handle.canNPCBeSpawned(w)) {

						// SimpleNPC spawn =
						// this.handle.getNextWaitingToSpawn(w);

						QuestX.logDebug("NPC can spawn in world '" + w + "'. Generating location..");

						final Location l;
						
						SimpleNPC front;
						
						boolean fullHS = HotspotManager.areHotspotsFull();

						if (!fullHS) {
							QuestX.logDebug("########### Hot spots are not full!");
							front = this.handle.getNextWaitingToSpawn(HotspotManager.getNextWorldForSpawn().getName());
							l = HotspotManager.spawnNPC(front.getName());
						} else {
							QuestX.logDebug("########### Hot spots are full!");
							l = this.handle.getDispatcher(w).getSpawnLocation();
							front = this.handle.getNextWaitingToSpawn(l.getWorld().getName());
						}

						QuestX.logDebug("Location generated = " + l.toString());

						

						// this.waiting.removeFirst();
						if (front == null) {
							QuestX.logDebug("NPC aquired from front was nulL! Check it loaded properly");
						} else {
							QuestX.logDebug("NPC " + front.getName() + " selected from waiting list!");
							QuestX.logDebug("Setting spawn location...");
							front.setNewSpawnLocation(l);
							QuestX.logDebug("Spawn set correctly!");
							QuestX.logDebug("Spawning NPC...");
							front.spawnNPC();
						}

					} else {
						QuestX.logDebug("NPC Cannot spawn in world '" + w + "'");
					}
				}

			}
			counterBetweenSpawns = 0;
		}
	}

}
