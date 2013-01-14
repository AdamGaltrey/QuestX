package couk.adamki11s.npcs;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class BanditNPC extends SimpleNPC{

	public BanditNPC(NPCHandler handle, String name, ChatColor nameColour, Location rootLocation, boolean moveable, boolean attackable, boolean aggressive, int minPauseTicks,
			int maxPauseTicks, int maxVariation, int health, int respawnTicks) {
		super(handle, name, nameColour, rootLocation, moveable, attackable, aggressive, minPauseTicks, maxPauseTicks, maxVariation, health, respawnTicks);
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
