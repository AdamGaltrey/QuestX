package com.adamki11s.quests;

import org.bukkit.inventory.ItemStack;

import com.adamki11s.npcs.tasks.EntityKillTracker;
import com.adamki11s.npcs.tasks.NPCKillTracker;

public class QuestTask {
	
	final QType type;
	final Object taskData;
	
	//no node, they should be loaded and stored in order
	public QuestTask(QType type, Object taskData){
		this.type = type;
		this.taskData = taskData;
	}
	
	/*
	 * Fetch items -instanceof- ItemStack[]
	 * KILL_ENTITIES -instanceof- EntityKillTracker
	 * KILL_NPC -instanceof- NPCKillTracker
	 * TALK_NPC -instanceof- String (NPC_Name) 
	 */
	public Object getData(){
		return this.taskData;
	}
	
	public boolean isItemStacks(){
		return (this.taskData instanceof ItemStack[]);
	}
	
	public boolean isKillEntities(){
		return (this.taskData instanceof EntityKillTracker);
	}
	
	public boolean isKillNPC(){
		return (this.taskData instanceof NPCKillTracker);
	}
	
	public boolean isTalkNPC(){
		return (this.taskData instanceof String);
	}

}
