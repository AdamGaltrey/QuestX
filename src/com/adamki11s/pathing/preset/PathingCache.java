package com.adamki11s.pathing.preset;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Location;

import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.AStar.InvalidPathException;
import com.adamki11s.pathing.Tile;

public class PathingCache {

	private final Location start;
	private final Tile[] nodes;

	private SoftReference<LinkedList<Tile[]>> nodeLink = new SoftReference<LinkedList<Tile[]>>(new LinkedList<Tile[]>());

	// each node is a point to walk to
	public PathingCache(Location start, Tile[] nodes) {
		this.start = start;
		this.nodes = nodes;
		this.updateCache(true);
	}

	private int position = 0;

	private Object lock = new Object();

	public Tile[] getWalkPath() {
		synchronized (lock) {
			position++;
			this.updateCache(false);
			LinkedList<Tile[]> list = this.nodeLink.get();
			if (position == nodes.length) {
				// send to start
				return list.get(0);
			} else {
				// send to next node
				return list.get(position - 1);
			}
		}
	}

	public void invalidateCache() {
		synchronized (lock) {
			this.updateCache(true);
		}
	}

	private void updateCache(boolean force) {
		synchronized (lock) {
			LinkedList<Tile[]> links;
			if ((links = this.nodeLink.get()) == null || force) {
				// recreate
				links = new LinkedList<Tile[]>();
				for (int i = 0; i < nodes.length; i++) {
					// each point to walk to
					AStar machine;
					if (i == 0) {
						try {
							machine = new AStar(start, nodes[i].getLocation(start), 3000);
							ArrayList<Tile> tileLink = machine.iterate();
							links.add(tileLink.toArray(new Tile[links.size()]));
						} catch (InvalidPathException e) {
							// handle
						}
					} else {
						try {
							machine = new AStar(nodes[i - 1].getLocation(start), nodes[i].getLocation(start), 3000);
							ArrayList<Tile> tileLink = machine.iterate();
							links.add(tileLink.toArray(new Tile[links.size()]));
						} catch (InvalidPathException e) {
							// handle
						}
					}
				}
				nodeLink = new SoftReference<LinkedList<Tile[]>>(links);
			}
		}
	}

}
