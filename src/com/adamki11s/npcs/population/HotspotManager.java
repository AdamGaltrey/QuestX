package com.adamki11s.npcs.population;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.adamki11s.io.FileLocator;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.objects.SyncObjectIO;
import com.adamki11s.sync.io.objects.SyncWrapper;

public class HotspotManager {

	private static volatile HashMap<String, Hotspot> hotspots = new HashMap<String, Hotspot>();
	
	public static synchronized void createHotspot(Hotspot h){
		hotspots.put(h.getTag(), h);
		File f = FileLocator.getHotspotFile();
		SyncObjectIO io = new SyncObjectIO(f);
		io.read();
		io.insertWriteableData(io.getReadableData());
		io.add(h.getTag(), h);
		io.write();
	}

	public static void loadHotspots() {
		File f = FileLocator.getHotspotFile();
		SyncObjectIO io = new SyncObjectIO(f);
		io.read();
		for (SyncWrapper wrap : io.getReadableData()) {
			Object o;
			if ((o = wrap.getObject()) instanceof Hotspot) {
				QuestX.logMSG("Loading hotspot " + wrap.getTag());
				Hotspot h = (Hotspot) o;
				hotspots.put(h.getTag(), h);
			}
		}
	}

	public static boolean areHotspotsFull() {
		for (Entry<String, Hotspot> e : hotspots.entrySet()) {
			if (e.getValue().canSpawnMore()) {
				QuestX.logMSG("Hotspots are not full!");
				return false;
			}
		}
		QuestX.logMSG("Hotspots are full, spawn as normal.");
		return true;
	}

	public static synchronized Location spawnNPC(String name) {
		Hotspot h = getNextFreeHotspot();
		h.addNPC(name);
		return getSpawnLocation(h);
	}
	
	public static World getNextWorldForSpawn(){
		return Bukkit.getServer().getWorld(getNextFreeHotspot().getWorldName());
	}

	private static Location getSpawnLocation(Hotspot h) {
		World w = Bukkit.getServer().getWorld(h.getWorldName());

		Location spawn = new Location(w, h.getCx(), h.getCy(), h.getCz());
		Random r = new Random();

		do {
			int x = spawn.getBlockX() + (r.nextInt(h.getRange() * 2) - h.getRange()), y = spawn.getBlockY() + (r.nextInt(h.getRange() * 2) - h.getRange()), z = spawn.getBlockZ()
					+ (r.nextInt(h.getRange() * 2) - h.getRange());
			spawn.setX(x);
			spawn.setY(y);
			spawn.setZ(z);
		} while (!canMoveHere(spawn));

		return spawn;
	}

	private static boolean canMoveHere(Location l) {
		Block b = l.getBlock().getRelative(0, -1, 0);// block it stands on
		// b.setType(Material.EMERALD_BLOCK);
		return (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1, 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0);
	}

	public static synchronized void despawnNPC(String name) {
		for (Entry<String, Hotspot> e : hotspots.entrySet()) {
			Hotspot h = e.getValue();
			h.removeNPC(name);
		}
	}

	private static Hotspot getNextFreeHotspot() {
		for (Entry<String, Hotspot> e : hotspots.entrySet()) {
			if (e.getValue().canSpawnMore()) {
				return e.getValue();
			}
		}
		return null;
	}

}
