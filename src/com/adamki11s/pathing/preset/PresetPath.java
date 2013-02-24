package com.adamki11s.pathing.preset;

import java.io.Serializable;

import org.bukkit.Location;

import com.adamki11s.pathing.Tile;
import com.adamki11s.sync.io.serializable.SyncLocation;

public class PresetPath implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Tile[] nodes;
	private final SyncLocation spawn;

	public PresetPath(SyncLocation spawn, Tile[] nodes) {
		this.spawn = spawn;
		this.nodes = nodes;
		first = true;
	}

	private transient int position = 0;
	private transient boolean first = false;

	public Location getStartLocation() {
		return spawn.getBukkitLocation();
	}

	public Tile[] getNodes() {
		return nodes;
	}

	public Location getNextTarget() {
		Tile next;

		if (position == (nodes.length - 1)) {
			position = 0;
		} else {
			position++;
		}

		if (first) {		
			next = nodes[position];
		} else {
			first = true;
			position = 0;
			next = nodes[0];
		}

		return next.getLocation(getStartLocation());
	}

}
