package com.adamki11s.io.npc;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.data.ItemStackDrop;


public class NPCTemplate {
	
	final String name;
	final ChatColor nameColour;
	final boolean moveable, attackable, aggressive;
	final int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks, maxHealth, damageMod;
	final double retalliationMultiplier;
	final ItemStackDrop inventory;
	final ItemStack[] gear;//boots 1, legs 2, chest 3, head 4, arm 5

	public NPCTemplate(String name, ChatColor nameColour, boolean moveable, boolean attackable, boolean aggressive, int minPauseTicks,
			int maxPauseTicks, int maxVariation, int health, int respawnTicks, ItemStackDrop inventory, ItemStack[] gear, int damageMod, double retalliationMultiplier) {
		this.name = name;
		this.nameColour = nameColour;
		this.moveable = moveable;
		this.attackable = attackable;
		this.aggressive = aggressive;
		this.minPauseTicks = minPauseTicks;
		this.maxPauseTicks = maxPauseTicks;
		this.maxVariation = maxVariation;
		this.maxHealth = health;
		this.respawnTicks = respawnTicks;
		this.inventory = inventory;
		this.gear = gear;
		this.damageMod = damageMod;
		this.retalliationMultiplier = retalliationMultiplier;
	}

}
