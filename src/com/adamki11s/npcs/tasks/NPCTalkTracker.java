package com.adamki11s.npcs.tasks;

import org.bukkit.ChatColor;

public class NPCTalkTracker {
	
	final String npcName;
	private boolean completed = false;
	
	public NPCTalkTracker(String npcName){
		this.npcName = npcName;
	}
	
	public void setTalkedTo(){
		this.completed = true;
	}
	
	public boolean isCompleted(){
		return this.completed;
	}
	
	public String getNPCName(){
		return this.npcName;
	}
	
	public String sendWhoToTalkTo(){
		return ChatColor.RED + "Talk to " + npcName;
	}

}
