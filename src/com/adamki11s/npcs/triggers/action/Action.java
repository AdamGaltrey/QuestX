package com.adamki11s.npcs.triggers.action;

import org.bukkit.entity.Player;

public interface Action {
	
	public void implement(Player p);
	
	public boolean isActive();

}
