package com.adamki11s.npcs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.npcs.population.NPCChunkData;
import com.adamki11s.npcs.population.NPCWorldData;
import com.adamki11s.npcs.population.SpawnLocationDispatcher;
import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;

public class NPCHandler {

	final NPCManager npc;
	
	volatile LinkedList<SimpleNPC> waiting = new LinkedList<SimpleNPC>();

	volatile HashSet<SimpleNPC> npcList = new HashSet<SimpleNPC>();

	final HashSet<NPCWorldData> npcWorldData = new HashSet<NPCWorldData>();
	
	HashMap<String, SpawnLocationDispatcher> spawnDispatchers = new HashMap<String, SpawnLocationDispatcher>();

	HashMap<String, HashSet<NPCChunkData>> npcChunkData = new HashMap<String, HashSet<NPCChunkData>>();

	public NPCHandler(JavaPlugin main, String worlds[]) {
		npc = new NPCManager(main);
		if (worlds != null) {
			for (String w : worlds) {
				npcWorldData.add(new NPCWorldData(w));
				npcChunkData.put(w, new HashSet<NPCChunkData>());
				spawnDispatchers.put(w, new SpawnLocationDispatcher(w, this));
			}
		}
	}
	
	public SpawnLocationDispatcher getDispatcher(String world){
		return this.spawnDispatchers.get(world);
	}
	
	public SimpleNPC getNextWaitingToSpawn(){
		if(this.waiting.size() < 1){
			return null;
		}
		SimpleNPC front = this.waiting.getFirst();
		this.waiting.removeFirst();
		return front;
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
	
	public boolean canNPCBeSpawned(String world){
		for (NPCWorldData npcWD : this.npcWorldData) {
			if (npcWD.getWorld().equalsIgnoreCase(world)) {
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
	
	void incrementSpawnCount(Chunk c){
		HashSet<NPCChunkData> cData = npcChunkData.get(c.getWorld().getName());
		if(cData == null){
			System.out.println("NPCChunkData is null");
			return;
		}
		for (NPCChunkData cd : cData) {
			if (cd.getX() == c.getX() && cd.getZ() == c.getZ()) {
				cd.increaseSpawnCount();
			}
		}
	}
	
	void decrementSpawnCount(Chunk c){
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
	
	public void registerNPCSpawn(SimpleNPC npc){
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
