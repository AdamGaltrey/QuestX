package com.adamki11s.pathing.preset;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockState {

	private final int x, y, z, id;
	private final byte data;
	
	public BlockState(Block b){
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.id = b.getTypeId();
		this.data = b.getData();
	}

	public void restoreBlock(Player p){
		p.sendBlockChange(new Location(p.getWorld(), x, y, z), id, data);
	}

}
