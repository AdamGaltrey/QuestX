package com.adamki11s.npcs.triggers.action;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.npcs.tasks.ISAParser;
import com.adamki11s.questx.QuestX;

public class PlayerGiveItemsAction implements Action {

	private ItemStack[] giveItems;

	private boolean isActive = true;

	public PlayerGiveItemsAction(String npc, String data) {
		try {
			this.giveItems = ISAParser.parseISA(data, npc, false);
		} catch (InvalidISAException e) {
			isActive = false;
			e.printErrorReason();
			QuestX.logError("Error found in custom_trigger file for NPC '" + npc + "'");
		}
	}

	@Override
	public void implement(Player p) {
		if (giveItems != null) {
			for (ItemStack i : giveItems) {
				if (i != null) {
					if (p.getInventory().firstEmpty() != -1) {
						p.getInventory().addItem(i);
					} else {
						p.getWorld().dropItemNaturally(p.getLocation(), i);
					}
				}
			}
		}

	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
