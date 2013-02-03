package com.adamki11s.reputation;

import java.util.HashMap;

import org.bukkit.ChatColor;

import com.adamki11s.io.GeneralConfigData;
import com.adamki11s.questx.QuestX;

public class ReputationManager {

	private static final ReputationIO io = new ReputationIO();

	static volatile HashMap<String, Reputation> rep = new HashMap<String, Reputation>();
	static HashMap<String, ChatColor> colours = new HashMap<String, ChatColor>();

	public static void loadPlayerReputation(String name) {
		if (!rep.containsKey(name)) {
			if (!io.doesPlayerHaveRepFile(name)) {
				QuestX.logDebug("Loaded default(0) rep for player " + name);
				io.createPlayerRepFile(name);
				rep.put(name, new Reputation(name, 0));
			} else {
				QuestX.logDebug("Loaded rep for player " + name);
				int r = io.getPlayerRep(name);
				rep.put(name, new Reputation(name, r));
			}
		}
		updateColourCache(name);
	}

	public static void updateReputation(String name, int amount) {
		io.updatePlayerRep(name, amount);
		if (rep.containsKey(name)) {
			QuestX.logDebug("REP CONTAINS PLAYER NAME");
			rep.get(name).addRep(amount);
		} else {
			QuestX.logDebug("REP DOES NOT CONTAIN PLAYER NAME");
			loadPlayerReputation(name);
			rep.get(name).addRep(amount);
		}
		updateColourCache(name);
	}

	private static void updateColourCache(String name) {
		if (QuestX.tagAPIEnabled && GeneralConfigData.isTagAPISupported()) {
			ChatColor c = rep.get(name).getChatColour();
			if (c != ChatColor.RESET) {
				colours.put(name, c);
			}
		}
	}

	public static HashMap<String, ChatColor> getNamesToColour() {
		return colours;
	}

	public static void unloadPlayerReputation(String name) {
		if (rep.containsKey(name)) {
			rep.remove(name);
		}
	}

	public static Reputation getPlayerReputation(String name) {
		loadPlayerReputation(name);
		return rep.get(name);
	}

}
