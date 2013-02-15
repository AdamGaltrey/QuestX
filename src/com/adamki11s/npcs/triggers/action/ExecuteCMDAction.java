package com.adamki11s.npcs.triggers.action;

import org.bukkit.entity.Player;

import com.adamki11s.commands.QuestXCMDExecutor;
import com.adamki11s.dialogue.dynamic.DynamicStrings;

public class ExecuteCMDAction implements Action {
	
	private String[] commands;
	
	private final boolean asPlayer;
	
	//EXECUTE_PLAYER_CMD:command 1#command 2
	//EXECUTE_SERVER_CMD:command 1#command 2
	
	public ExecuteCMDAction(String data, boolean asPlayer){
		this.asPlayer = asPlayer;
		this.commands = data.split("#");
	}

	@Override
	public void implement(Player p) {
		String[] localCopy = commands;
		for(String s : localCopy){
			s = DynamicStrings.getDynamicReplacement(s, p.getName());
		}
		if(asPlayer){
			QuestXCMDExecutor.executeAsPlayer(p.getName(), localCopy);
		} else {
			QuestXCMDExecutor.executeAsServer(localCopy);
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

}
