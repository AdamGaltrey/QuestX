package com.adamki11s.reputation;

import java.util.HashMap;

import com.adamki11s.questx.QuestX;

public class ReputationManager {

	private static final ReputationIO io = new ReputationIO();

	static volatile HashMap<String, Reputation> rep = new HashMap<String, Reputation>();

	public static void loadPlayerReputation(String name) {
		if (!rep.containsKey(name)) {
			if (!io.doesPlayerHaveRepFile(name)) {
				QuestX.logMSG("Loaded default(0) rep for player " + name);
				io.createPlayerRepFile(name);
				rep.put(name, new Reputation(name, 0));
			} else {
				QuestX.logMSG("Loaded rep for player " + name);
				int r = io.getPlayerRep(name);
				rep.put(name, new Reputation(name, r));
			}
		}
	}

	public static void updateReputation(String name, int amount) {
		io.updatePlayerRep(name, amount);
		if (rep.containsKey(name)) {
			QuestX.logMSG("REP CONTAINS PLAYER NAME");
			rep.get(name).addRep(amount);
		} else {
			QuestX.logMSG("REP DOES NOT CONTAIN PLAYER NAME");
			loadPlayerReputation(name);
			rep.get(name).addRep(amount);
		}
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
