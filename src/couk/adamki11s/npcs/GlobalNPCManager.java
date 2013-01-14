package couk.adamki11s.npcs;

import java.util.HashSet;

public class GlobalNPCManager {

	protected static HashSet<SimpleNPC> npcs = new HashSet<SimpleNPC>();

	public static void registerNPC(SimpleNPC npc) {
		npcs.add(npc);
	}

	public static void removeNPC(SimpleNPC npc) {
		npcs.remove(npc);
	}

	public static SimpleNPC getNPCByName(String name) {
		for (SimpleNPC npc : npcs) {
			if (npc.getName().equalsIgnoreCase(name))
				return npc;
		}
		return null;
	}

}
