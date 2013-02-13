package com.adamki11s.npcs.triggers;

import java.io.File;

import com.adamki11s.io.FileLocator;
import com.adamki11s.sync.io.configuration.SyncConfiguration;

public class CustomAction {
	
	private File f;
	
	final String npcName;
	
	//auto ends conversation using this trigger
	
	public CustomAction(String npcName){
		this.npcName = npcName;
	}
	
	public void load(){
		File f = FileLocator.getCustomTriggerFile(this.npcName);
		SyncConfiguration io = new SyncConfiguration(f);
		io.read();
		
		/*
		 * Properties
		 * 
		 * LIGHTNING:<STRIKES>#<RANGE>#<TICK_DELAY>
		 * 
		 * SPAWN:<MOB NAME>,<QUANTITY>#etc...
		 * SPAWN_RANGE:<BLOCK RANGE>
		 * TARGET_PLAYER:<Boolean>
		 * 
		 * TELEPORT_PLAYER:<world>,<x>,<y>,<z>#delay
		 * 
		 * NPC_ATTACK_PLAYER:<Boolean>
		 * 
		 * PLAYER_GIVE_ITEMS:<id>,<data>,<quantity>#etc...
		 * 
		 * DAMAGE_PLAYER:<amount>
		 * 
		 * PLAYER_POTION_EFFECT:<effect>#<clear previous effects AS boolean>
		 * 
		 * SET_VELOCITY:x,y,z
		 * 
		 * INVOKE_QUEST:<boolean>
		 * INVOKE_TASK:<boolean>
		 */
	}

}
