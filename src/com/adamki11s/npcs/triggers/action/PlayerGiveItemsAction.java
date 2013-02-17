package com.adamki11s.npcs.triggers.action;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.exceptions.InvalidISAException;
import com.adamki11s.npcs.tasks.ISAParser;
import com.adamki11s.questx.QuestX;

public class PlayerGiveItemsAction implements Action {

	private ItemStack[] giveItems;

	private boolean isActive = true;

	private final int cooldownMinutes;

	private HashMap<String, Long> timestamps = new HashMap<String, Long>();

	public PlayerGiveItemsAction(String npc, String data, int cooldownMinutes) {
		this.cooldownMinutes = cooldownMinutes;
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

		boolean proceed = true;

		if (!timestamps.containsKey(p.getName())) {
			timestamps.put(p.getName(), System.currentTimeMillis());
		} else {
			long lastTime = timestamps.get(p.getName());
			long secondsDiff = (System.currentTimeMillis() - lastTime) / 1000;
			int minuteDifference = (int) Math.floor(((double) secondsDiff / 60D));
			if(minuteDifference >= this.cooldownMinutes){
				timestamps.put(p.getName(), System.currentTimeMillis());
			} else {
				proceed = false;
			}
		}

		if (proceed) {
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
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
