package com.adamki11s.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.npcs.population.NPCChunkData;
import com.adamki11s.npcs.population.NPCWorldData;
import com.adamki11s.npcs.population.SpawnLocationDispatcher;
import com.adamki11s.questx.QuestX;
import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;

public class NPCHandler {

	final NPCManager npc;
	final String[] worlds;

	volatile LinkedList<SimpleNPC> waiting = new LinkedList<SimpleNPC>();

	volatile HashSet<SimpleNPC> npcList = new HashSet<SimpleNPC>();

	final HashSet<NPCWorldData> npcWorldData = new HashSet<NPCWorldData>();

	HashMap<String, SpawnLocationDispatcher> spawnDispatchers = new HashMap<String, SpawnLocationDispatcher>();

	HashMap<String, HashSet<NPCChunkData>> npcChunkData = new HashMap<String, HashSet<NPCChunkData>>();

	public void addToWaitingList(SimpleNPC npc) {

		if (npc == null) {
			QuestX.logMSG("NULL npc passed into waiting list");
		} else {
			QuestX.logMSG("NPC " + npc.getName() + " added to waiting list");
			this.waiting.add(npc);

		}
		QuestX.logMSG("New list size = " + this.waiting.size());
	}

	public String[] getLoadedOrWaiting() {
		ArrayList<String> lor = new ArrayList<String>();
		for (SimpleNPC s : waiting) {
			lor.add(s.getName());
		}
		for (SimpleNPC s : npcList) {
			lor.add(s.getName());
		}
		String[] loadedorwaiting = new String[lor.size()];
		return lor.toArray(loadedorwaiting);
	}

	public String[] getWorlds() {
		return this.worlds;
	}

	public NPCHandler(JavaPlugin main, String worlds[]) {
		this.worlds = worlds;
		npc = new NPCManager(main);
		QuestX.logMSG("Checking worlds NPCHandler constructor");
		if (worlds != null) {
			QuestX.logMSG("worlds[] != null");
			for (String w : worlds) {
				QuestX.logMSG("Checking world - '" + w + "'");
				npcWorldData.add(new NPCWorldData(w));
				npcChunkData.put(w, new HashSet<NPCChunkData>());
				spawnDispatchers.put(w, new SpawnLocationDispatcher(w, this));
			}
		} else {
			QuestX.logMSG("worlds[] == null");

		}
	}

	public SpawnLocationDispatcher getDispatcher(String world) {
		return this.spawnDispatchers.get(world);
	}
	
	public boolean isWaitingQueueEmpty(){
		return (this.waiting.size() < 1);
	}

	public synchronized SimpleNPC getNextWaitingToSpawn(String w) {
		if (this.waiting.size() < 1) {
			QuestX.logMSG("0 Elements waiting, breaking...");
			return null;
		}
		
		/*QuestX.logMSG("NPC can spawn in world '" + w + "'. Generating location..");
		
		Location l = this.getDispatcher(w).getSpawnLocation();
		QuestX.logMSG("Location generated = " + l.toString());

		SimpleNPC front = this.waiting.removeFirst();
		
		//this.waiting.removeFirst();
		if (front == null) {
			QuestX.logMSG("NPC aquired from front was nulL! Check it loaded properly");
		} else {
			QuestX.logMSG("NPC " + front.getName() + " selected from waiting list!");
		}
		
		if (front == null) {
			QuestX.logMSG("Spawned npc is null, breaking...");
		} else {
			//QuestX.logMSG("Setting new npc spawn location...");
			front.setNewSpawnLocation(l);
			QuestX.logMSG("Spawning NPC...");
			front.spawnNPC();
		}*/
		return this.waiting.removeFirst();
	}

	public boolean doesNeedChunkData(Chunk c) {
		HashSet<NPCChunkData> cData = npcChunkData.get(c.getWorld().getName());
		for (NPCChunkData cd : cData) {
			if (cd.getX() == c.getX() && cd.getZ() == c.getZ()) {
				return false;
			}
		}
		return true;
	}

	public boolean canNPCBeSpawned(String world) {
		// QuestX.logMSG("Checking can npc be spawned in world - " + world);
		// QuestX.logMSG("World data size = " + this.npcWorldData.size());
		for (NPCWorldData npcWD : this.npcWorldData) {
			// QuestX.logMSG("Looping NPC world data - '" + npcWD.getWorld() +
			// "'");
			if (npcWD.getWorld().equalsIgnoreCase(world)) {
				// QuestX.logMSG("Matched world - " + world);
				// QuestX.logMSG("Can spawn more = " + npcWD.canSpawnMore());
				return npcWD.canSpawnMore();
			}
		}
		return false;
	}

	public boolean canNPCBeSpawned(Chunk c) {
		for (NPCWorldData npcWD : this.npcWorldData) {
			if (npcWD.getWorld().equalsIgnoreCase(c.getWorld().getName())) {
				if (npcWD.canSpawnMore()) {
					if (this.doesNeedChunkData(c)) {
						this.addChunkData(c.getWorld().getName(), new NPCChunkData(c.getX(), c.getZ()));
					} else {
						for (NPCChunkData cd : npcChunkData.get(c.getWorld().getName())) {
							if (cd.getX() == c.getX() && cd.getZ() == c.getZ()) {
								return cd.canSpawnMore();
							}
						}
					}
				}
			}
		}
		return false;
	}

	void incrementSpawnCount(Chunk c) {
		HashSet<NPCChunkData> cData = npcChunkData.get(c.getWorld().getName());
		if (cData == null) {
			System.out.println("NPCChunkData is null");
			return;
		}
		for (NPCChunkData cd : cData) {
			if (cd.getX() == c.getX() && cd.getZ() == c.getZ()) {
				cd.increaseSpawnCount();
			}
		}
	}

	void decrementSpawnCount(Chunk c) {
		HashSet<NPCChunkData> cData = npcChunkData.get(c.getWorld().getName());
		for (NPCChunkData cd : cData) {
			if (cd.getX() == c.getX() && cd.getZ() == c.getZ()) {
				cd.decreaseSpawnCount();
			}
		}
	}

	public void addChunkData(String world, NPCChunkData npcCD) {
		this.npcChunkData.get(world).add(npcCD);
	}

	public void registerNPC(SimpleNPC npc) {
		this.npcList.add(npc);
	}

	public void registerNPCSpawn(SimpleNPC npc) {
		this.incrementSpawnCount(npc.getHumanNPC().getBukkitEntity().getLocation().getChunk());
	}

	public void removeNPC(SimpleNPC npc) {
		this.decrementSpawnCount(npc.getHumanNPC().getBukkitEntity().getLocation().getChunk());
		this.npcList.remove(npc);
	}

	public HashSet<SimpleNPC> getNPCs() {
		return this.npcList;
	}

	public NPCManager getNPCManager() {
		return this.npc;
	}

	public HumanNPC getNPCByName(String name) {
		return (HumanNPC) this.npc.getHumanNPCByName(name);
	}

	public SimpleNPC getSimpleNPCByEntity(Entity e) {
		String npcID = this.getNPCManager().getNPCIdFromEntity(e);
		SimpleNPC snpc = this.getSimpleNPCByID(npcID);
		return snpc;
	}

	public SimpleNPC getSimpleNPCByID(String id) {
		for (SimpleNPC snpc : this.npcList) {
			if (snpc.doesNPCIDMatch(id)) {
				return snpc;
			}
		}
		return null;
	}

	public SimpleNPC getSimpleNPCByName(String name) {
		for (SimpleNPC simpNPC : this.npcList) {
			if (name.equalsIgnoreCase(simpNPC.getName())) {
				return simpNPC;
			}
		}
		return null;
	}

}
