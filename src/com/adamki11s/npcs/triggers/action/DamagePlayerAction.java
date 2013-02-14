package com.adamki11s.npcs.triggers.action;

import org.bukkit.entity.Player;

import com.adamki11s.questx.QuestX;

public class DamagePlayerAction implements Action{
	
	private boolean isActive = true;
	
	private int damage = 0;
	
	public DamagePlayerAction(String npc, String data){
		try{
			damage = Integer.parseInt(data);
		} catch (NumberFormatException nfe){
			QuestX.logError("Player damage was not an integer for NPC '" + npc + "' in custom_trigger file. Setting disabled.");
			this.isActive = false;
 			return;
		}
	}

	@Override
	public void implement(Player p) {
		if(damage > 0){
			p.damage(damage);
		}
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
