package com.adamki11s.io;

import java.io.File;

import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class WorldConfigData {

	static String[] worlds;
	static int maxSpawnsPerChunk, maxSpawnsPerWorld, untouchedDespawnMinutes;


	public static void loadWorldConfigData() {
		File f = FileLocator.getWorldConfig();
		SyncConfiguration conf = new SyncConfiguration(f);
		conf.read();
		
		String raw = conf.getString("SPAWNABLE_WORLDS");
		StringBuilder trimmed = new StringBuilder();
		for (String s : raw.split(",")) {
			trimmed.append(s.trim()).append(",");
		}
		worlds = trimmed.toString().split(",");
		for (String s : worlds) {
			QuestX.logDebug("World = '" + s + "'");
		}
		
		maxSpawnsPerChunk = conf.getInt("MAX_SPAWNS_PER_CHUNK");
		maxSpawnsPerWorld = conf.getInt("MAX_SPAWNS_PER_WORLD");
		untouchedDespawnMinutes = conf.getInt("DESPAWN_IFUNTOUCHED_MINUTES");
		
		QuestX.logDebug("Max spawns per world = " + maxSpawnsPerWorld);
	}

	public static String[] getWorlds() {
		return worlds;
	}

	public static int getMaxSpawnsPerChunk() {
		return maxSpawnsPerChunk;
	}

	public static int getMaxSpawnsPerWorld() {
		return maxSpawnsPerWorld;
	}

	public static int getUntouchedDespawnMinutes() {
		return untouchedDespawnMinutes;
	}
	
	

}
