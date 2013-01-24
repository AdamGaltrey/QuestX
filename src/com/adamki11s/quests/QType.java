package com.adamki11s.quests;

public enum QType {

	FETCH_ITEMS,
	KILL_ENTITIES,
	KILL_NPC,
	TALK_NPC;
	
	public static QType parseType(String type){
		if(type.equalsIgnoreCase("FETCH_ITEMS")){
			return FETCH_ITEMS;
		} else if(type.equalsIgnoreCase("KILL_ENTITIES")){
			return KILL_ENTITIES;
		} else if(type.equalsIgnoreCase("KILL_NPC")){
			return KILL_NPC;
		}  else if(type.equalsIgnoreCase("TALK_NPC")){
			return TALK_NPC;
		} else {
			return null;
		}
	}
	
}
