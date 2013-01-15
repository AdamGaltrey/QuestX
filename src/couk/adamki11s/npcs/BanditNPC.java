package couk.adamki11s.npcs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import couk.adamki11s.data.ItemStackDrop;

public class BanditNPC extends SimpleNPC{

	



	public BanditNPC(NPCHandler handle, String name, ChatColor nameColour, Location rootLocation, boolean moveable, boolean attackable, boolean aggressive, int minPauseTicks,
			int maxPauseTicks, int maxVariation, int health, int respawnTicks, ItemStackDrop inventory, ItemStack[] gear, int damageMod, double retalliationMultiplier) {
		super(handle, name, nameColour, rootLocation, moveable, attackable, aggressive, minPauseTicks, maxPauseTicks, maxVariation, health, respawnTicks, inventory, gear, damageMod,
				retalliationMultiplier);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isAggressive(){
		return true;
	}
	
	@Override
	public ChatColor getNameColour() {
		return ChatColor.RED;
	}
	

}
