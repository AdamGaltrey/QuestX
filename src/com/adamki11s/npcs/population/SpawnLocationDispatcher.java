package com.adamki11s.npcs.population;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.questx.QuestX;

public class SpawnLocationDispatcher {

	long gDensity;
	final String world;
	final NPCHandler handle;
	final Random r = new Random();

	public SpawnLocationDispatcher(String world, NPCHandler handle) {
		this.world = world;
		this.handle = handle;
	}

	void updateDensity() {
		// Does not call SQL, simply updates the value held in cache class which
		// updates itself periodically.
		this.gDensity = GlobalDensityCache.getGlobalDensity(this.world);
	}

	boolean isDispatchingSpawns() {
		return (this.handle.canNPCBeSpawned(this.world));
	}
	
	//boolean isWithinRange

	public Location getSpawnLocation() {
		QuestX.logMSG("Getting spawn location");
		this.updateDensity();
		LinkedHashMap<NPCChunkData, Long> nodes = GlobalDensityCache.getNodes(this.world);
		NPCChunkData cd = null;
		boolean canSpawn = false;
		do {
			long randLong = this.getRandomLong(0, this.gDensity, r);	
			QuestX.logMSG("rand long = " + randLong);
			for (Entry<NPCChunkData, Long> set : nodes.entrySet()) {
				if (set.getValue() > randLong) {
					QuestX.logMSG("Greater value than random, setting chunk density");
					//must be prob as values are sorted by density, lowest -> highest
					cd = set.getKey();
					if(cd.canSpawnMore()){
						canSpawn = true;
					}
					break;
				}
			}
			if(!canSpawn){
				nodes.remove(cd);//Remove from the list for this check
			}
		} while (!canSpawn);
		
		World w = Bukkit.getServer().getWorld(this.world);
		Location spawn = new Location(w, 0, 0, 0);
		
		do{
			int x = (cd.getX() * 16) + (r.nextInt(15) + 1),
			y = r.nextInt(55) + 45 ,//45-100 
			/*
			 * Temporary solution to stop dynamic spawning in caves.
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			z = (cd.getZ() * 16) + (r.nextInt(15) + 1);
			spawn.setX(x);
			spawn.setY(y);
			spawn.setZ(z);
		} while(!this.canMoveHere(spawn)); 
		
		return spawn;
		
	}
	
	private boolean canMoveHere(Location l) {
		Block b = l.getBlock().getRelative(0, -1, 0);//block it stands on
		//b.setType(Material.EMERALD_BLOCK);
		return (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1, 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0);
	}

	long getRandomLong(long lower, long max, Random r) {
		long number = lower + (long) (r.nextDouble() * (max - lower));
		return number;
	}

}
