package com.adamki11s.npcs.population;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.sql.SyncSQL;

public class GlobalDensityCache {

	static HashMap<String, Long> gDensities = new HashMap<String, Long>();
	static HashMap<String, LinkedHashMap<NPCChunkData, Long>> nodes = new HashMap<String, LinkedHashMap<NPCChunkData, Long>>();

	public static void updateGlobalDensity(SyncSQL sql, String[] worlds) {
		for (String w : worlds) {
			if(!nodes.containsKey(w)){
				nodes.put(w, new LinkedHashMap<NPCChunkData, Long>());
			}
			String query = "SELECT * FROM " + w;
			long gDensity = 0;
			try {
				ResultSet set = sql.sqlQuery(query);
				while (set.next()) {
					NPCChunkData cd = new NPCChunkData(set.getInt("x"), set.getInt("z"));
					gDensity += set.getLong("density");
					nodes.get(w).put(cd, gDensity);
				}
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (gDensities.containsKey(w)) {
				if (gDensity > gDensities.get(w)) {
					gDensities.put(w, gDensity);
					QuestX.logMSG("Global Density Updated to " + gDensity);
				}
			} else {
				gDensities.put(w, gDensity);
			}
		}
	}

	public static long getGlobalDensity(String world) {
		return gDensities.get(world);
	}
	
	public static LinkedHashMap<NPCChunkData, Long> getNodes(String world){
		return nodes.get(world);
	}

}
