package couk.adamki11s.npcs;

import java.util.HashSet;

public class UniqueNameRegister {
	
	public static HashSet<String> npcNames = new HashSet<String>();
	
	public static boolean isNameUnique(String name){
		return !(npcNames.contains(name));
	}
	
	public static void addNPCName(String name){
		npcNames.add(name);
	}
	
	public static void removeName(String name){
		if(!isNameUnique(name)){
			npcNames.remove(name);
		}
	}

}
