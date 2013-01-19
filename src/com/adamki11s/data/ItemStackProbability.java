package com.adamki11s.data;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class ItemStackProbability {
	
	final ItemStack is;
	final int probability;
	
	//10,000 - 1
	public ItemStackProbability(ItemStack is, int probability){
		this.is = is;
		this.probability = probability;
	}
	
	public ItemStack getDrop(){
		Random r = new Random();
		int chance = r.nextInt(9999) + 1;
		if(this.probability - chance >= 0){
			return this.is;
		} else {
			return null;
		}
	}

}
