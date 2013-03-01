package com.adamki11s.npcs.population;

import java.util.Arrays;
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

	private static HashMap<String, Hotspot> hotspots = new HashMap<String, Hotspot>();

	private static volatile SyncObjectIO io = new SyncObjectIO(FileLocator.getHotspotFile());
	
	public static String[] getAlphabeticalHotspots(){
		String[] alph = new String[hotspots.size()];
		int c = 0;
		for(Entry<String, Hotspot> e : hotspots.entrySet()){
			alph[c] = e.getKey();
			c++;
		}
		Arrays.sort(alph, String.CASE_INSENSITIVE_ORDER);
		return alph;
	}

	public static synchronized void createHotspot(Hotspot h) {
		hotspots.put(h.getTag(), h);
		io.read();
		io.clearWriteArray();
		io.insertWriteableData(io.getReadableData());
		io.add(h.getTag(), h);
		io.write();
	}

	public static synchronized void editHotspot(String name, int range, int maxSpawns) {
		Hotspot r = hotspots.get(name);
		Hotspot newHS = new Hotspot(r.getCx(), r.getCy(), r.getCz(), range, maxSpawns, r.getTag(), r.getWorldName());
		hotspots.remove(name);
		io.read();
		io.clearWriteArray();
		for (SyncWrapper wrap : io.getReadableData()) {
			if (wrap.getTag().equalsIgnoreCase(name)) {
				io.add(name, newHS);
				hotspots.put(name, newHS);
			} else {
				io.add(wrap);
			}
		}
		io.write();
	}

	public static synchronized void deleteHotspot(String name) {
		hotspots.remove(name);
		io.read();
		io.clearWriteArray();
		for (SyncWrapper wrap : io.getReadableData()) {
			if (!wrap.getTag().equalsIgnoreCase(name)) {
				io.add(wrap);
			}
		}
		io.write();
	}

	public static boolean doesHotspotExist(String name) {
		return hotspots.containsKey(name);
	}

	public static synchronized void loadHotspots() {
		io.read();
		for (SyncWrapper wrap : io.getReadableData()) {
			Object o;
			if ((o = wrap.getObject()) instanceof Hotspot) {
				QuestX.logDebug("Loading hotspot " + wrap.getTag());
				Hotspot h = (Hotspot) o;
				hotspots.put(h.getTag(), h);
			}
		}
	}

	public static boolean areHotspotsFull() {
		if (hotspots.size() < 1) {
			return true;
		}
		for (Entry<String, Hotspot> e : hotspots.entrySet()) {
			if (e.getValue().canSpawnMore()) {
				QuestX.logDebug("Hotspots are not full!");
				return false;
			}
		}
		QuestX.logDebug("Hotspots are full, spawn as normal.");
		return true;
	}

	public static synchronized Location getSpawnLocation(String name) {
		Hotspot h = getNextFreeHotspot();
		h.addNPC(name);
		return getSpawnLocation(h);
	}

	public static World getNextWorldForSpawn() {
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
		Block b = l.getBlock().getRelative(0, -1, 0);
		return (!b.isLiquid() && b.getTypeId() != 0 && b.getRelative(0, 1, 0).getTypeId() == 0 && b.getRelative(0, 2, 0).getTypeId() == 0 && b.getRelative(1, 0, 0).getTypeId() == 0
				&& b.getRelative(-1, 0, 0).getTypeId() == 0 && b.getRelative(0, 0, 1).getTypeId() == 0 && b.getRelative(0, 0, -1).getTypeId() == 0);
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
