package com.adamki11s.reputation;

import java.util.HashMap;

public class ReputationManager {

	private static final ReputationIO io = new ReputationIO();

	static HashMap<String, Reputation> rep = new HashMap<String, Reputation>();

	public static void loadPlayerReputation(String name) {
		if (!rep.containsKey(name)) {
			if (!io.doesPlayerHaveRepFile(name)) {
				io.createPlayerRepFile(name);
				rep.put(name, new Reputation(name, 0));
			} else {
				int r = io.getPlayerRep(name);
				rep.put(name, new Reputation(name, r));
			}
		}
	}
	
	public static void updateReputation(String name, int amount){
		io.updatePlayerRep(name, amount);
		rep.get(name).addRep(amount);
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
