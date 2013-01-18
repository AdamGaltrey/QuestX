package couk.adamki11s.npcs.tasks;

import java.util.HashSet;

public class TaskRegister {
	
	static HashSet<TaskManager> managers = new HashSet<TaskManager>();
	
	
	
	public static void registerManager(TaskManager tm){
		managers.add(tm);
	}
	
	public static void unloadManagerByName(String playerName){
		
	}

}
