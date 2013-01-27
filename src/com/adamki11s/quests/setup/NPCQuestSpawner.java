package com.adamki11s.quests.setup;

public class NPCQuestSpawner {
	
	final String npcName, description;
	
	public NPCQuestSpawner(String npcName, String description){
		this.npcName = npcName;
		this.description = description;
	}

	public String getNpcName() {
		return npcName;
	}

	public String getDescription() {
		return description;
	}

}
