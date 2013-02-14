package com.adamki11s.npcs.triggers.action;

import org.bukkit.entity.Player;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.questx.QuestX;

public class NPCAttackPlayerAction implements Action {

	private boolean isActive = true, attackPlayer = true;

	final String npc;
	final NPCHandler handle;

	public NPCAttackPlayerAction(NPCHandler handle, String npc, String data) {
		this.npc = npc;
		this.handle = handle;
		attackPlayer = Boolean.parseBoolean(data);
	}

	@Override
	public void implement(Player p) {
		if (this.attackPlayer) {
			SimpleNPC n = this.handle.getSimpleNPCByName(npc);
			if (n == null) {
				this.isActive = false;
				QuestX.logError("NPC '" + npc + "' could not be located. Setting disabled for custom_trigger.");
				return;
			} else {
				if (!n.isUnderAttack()) {
					n.setAggro(p);
				}
			}
		}
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
