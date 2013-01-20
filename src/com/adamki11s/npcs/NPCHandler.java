package com.adamki11s.npcs;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.npcs.population.NPCChunkData;
import com.adamki11s.npcs.population.NPCWorldData;
import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

public class NPCHandler {

	final NPCManager npc;

	volatile HashSet<SimpleNPC> npcList = new HashSet<SimpleNPC>();

	final HashSet<NPCWorldData> npcWorldData = new HashSet<NPCWorldData>();

	HashMap<String, HashSet<NPCChunkData>> npcChunkData = new HashMap<String, HashSet<NPCChunkData>>();

	public NPCHandler(JavaPlugin main, String worlds[]) {
		npc = new NPCManager(main);
		for (String w : worlds) {
			npcWorldData.add(new NPCWorldData(w));
			npcChunkData.put(w, new HashSet<NPCChunkData>());
		}
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

	public void addChunkData(String world, NPCChunkData npcCD) {
		this.npcChunkData.get(world).add(npcCD);
	}

	public void registerNPC(SimpleNPC npc) {
		this.npcList.add(npc);
	}

	public void removeNPC(SimpleNPC npc) {
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
