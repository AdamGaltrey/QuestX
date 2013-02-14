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
		 * PLAYER_GIVE_ITEMS:<id>,<data>,<quantity>#etc...
		 * 
		 * DAMAGE_PLAYER:<amount>
		 * 
		 * SET_VELOCITY:x,y,z
		 */
	}

}
