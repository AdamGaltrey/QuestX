package com.adamki11s.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class ItemStackDrop {
	
	final ItemStackProbability[] isp;
	
	public ItemStackDrop(ItemStackProbability[] isp){
		this.isp = isp;
	}
	
	public ItemStack[] getDrops(){
		List<ItemStack> dropList = new ArrayList<ItemStack>();
		for(ItemStackProbability prob : this.isp){
			ItemStack result = prob.getDrop();
			if(result != null){
				dropList.add(result);
			}
		}
		ItemStack[] dropArray = new ItemStack[dropList.size()];
		return dropList.toArray(dropArray);
	}
	

}
