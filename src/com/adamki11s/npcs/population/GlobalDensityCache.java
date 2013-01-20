package com.adamki11s.npcs.population;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.sql.SyncSQL;

public class GlobalDensityCache {

	static HashMap<String, Long> gDensities = new HashMap<String, Long>();

	public static void updateGlobalDensity(SyncSQL sql, String[] worlds) {
		for (String w : worlds) {
			String query = "SELECT density FROM " + w;
			long gDensity = 0;
			try {
				ResultSet set = sql.sqlQuery(query);
				while (set.next()) {
					gDensity += set.getLong("density");
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

}
