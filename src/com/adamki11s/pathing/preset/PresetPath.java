package com.adamki11s.pathing.preset;

import java.io.Serializable;

import org.bukkit.Location;

import com.adamki11s.pathing.Tile;
import com.adamki11s.sync.io.serializable.SyncLocation;

public class PresetPath implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String npc;
	private final Tile[] nodes;
	private final SyncLocation spawn;
	private final int tickDelay;
	
	private transient PathingCache pCache;
	
	public PresetPath(String npc, SyncLocation spawn, Tile[] nodes, int tickDelay){
		this.npc = npc;
		this.spawn = spawn;
		this.nodes = nodes;
		//time to wait before moving to the next node after reaching the destination node.
		this.tickDelay = tickDelay;
		this.pCache = new PathingCache(spawn.getBukkitLocation(), nodes);
	}
	
	public PathingCache getPathingCache(){
		return this.pCache;
	}
	
	

}
