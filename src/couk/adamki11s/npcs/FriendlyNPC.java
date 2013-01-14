package couk.adamki11s.npcs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import couk.adamki11s.ai.dataset.RepLevel;
import couk.adamki11s.data.ItemStackDrop;

public class FriendlyNPC extends SimpleNPC{

	public FriendlyNPC(NPCHandler handle, String name, ChatColor nameColour, Location rootLocation, boolean moveable, boolean attackable, boolean aggressive, int minPauseTicks,
			int maxPauseTicks, int maxVariation, int health, int respawnTicks, ItemStackDrop inventory) {
		super(handle, name, nameColour, rootLocation, moveable, attackable, aggressive, minPauseTicks, maxPauseTicks, maxVariation, health, respawnTicks, inventory);
		// TODO Auto-generated constructor stub
	}

	final RepLevel willRunFrom = RepLevel.VILLAIN, willNotInteract = RepLevel.HITMAN;
	
	boolean isFleeing = false;

	
	
	public void setFleeing(boolean flee){
		this.isFleeing = flee;
	}
	
	public boolean isFleeing(){
		return this.isFleeing;
	}
	
	@Override
	public boolean isAggressive(){
		return false;
	}
	
	@Override
	public ChatColor getNameColour() {
		return ChatColor.GREEN;
	}

}
