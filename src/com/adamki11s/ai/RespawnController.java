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
						QuestX.logMSG("Trying to load '" + f.getName());
						npcNameToLoad = f.getName();
						LoadNPCTemplate tempLoader = new LoadNPCTemplate(npcNameToLoad);
						tempLoader.loadProperties();
						if (tempLoader.wantsToLoad()) {
							NPCTemplate temp = tempLoader.getLoadedNPCTemplate();
							temp.addSimpleNPCToWaitingList(this.handle);
							QuestX.logMSG("Added NPC '" + npcNameToLoad + "' to waiting list");
							break;
						} else {
							QuestX.logMSG("NPC '" + npcNameToLoad + "' load=false, finding another");
							loaded = true;
						}
						// load into waiting list
					}
				}

			} else {
				QuestX.logMSG("All NPC's are loaded, skipping");
			}
			this.counterBetweenLoads = 0;

		}

		if (counterBetweenSpawns > this.timeBetweenSpawns) {
			QuestX.logMSG("Checking to make a new spawn");
			if (this.handle.isWaitingQueueEmpty()) {
				QuestX.logMSG("No spawns in line, breaking");
			} else {
				QuestX.logMSG("Spawns in line, checking worlds");
				// spawn
				for (String w : this.handle.getWorlds()) {
					if (this.handle.canNPCBeSpawned(w)) {

						// SimpleNPC spawn =
						// this.handle.getNextWaitingToSpawn(w);

						QuestX.logMSG("NPC can spawn in world '" + w + "'. Generating location..");

						final Location l;
						
						SimpleNPC front;

						if (!HotspotManager.areHotspotsFull()) {
							front = this.handle.getNextWaitingToSpawn(HotspotManager.getNextWorldForSpawn().getName());
							l = HotspotManager.spawnNPC(front.getName());
						} else {
							l = this.handle.getDispatcher(w).getSpawnLocation();
							front = this.handle.getNextWaitingToSpawn(l.getWorld().getName());
						}

						QuestX.logMSG("Location generated = " + l.toString());

						

						// this.waiting.removeFirst();
						if (front == null) {
							QuestX.logMSG("NPC aquired from front was nulL! Check it loaded properly");
						} else {
							QuestX.logMSG("NPC " + front.getName() + " selected from waiting list!");
							QuestX.logMSG("Setting spawn location...");
							front.setNewSpawnLocation(l);
							QuestX.logMSG("Spawn set correctly!");
							QuestX.logMSG("Spawning NPC...");
							front.spawnNPC();
						}

					} else {
						QuestX.logMSG("NPC Cannot spawn in world '" + w + "'");
					}
				}

			}
			counterBetweenSpawns = 0;
		}
	}

}
