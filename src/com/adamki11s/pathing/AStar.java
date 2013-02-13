package com.adamki11s.pathing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import net.minecraft.server.v1_4_R1.AxisAlignedBB;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.entity.Player;

import com.adamki11s.questx.QuestX;

public class AStar {

	private final int sx, sy, sz, ex, ey, ez;
	private final World w;
	private int endCode;

	private ArrayList<Tile> open = new ArrayList<Tile>();
	private ArrayList<Tile> closed = new ArrayList<Tile>();

	private void addToOpenList(Tile t, boolean modify) {
		Tile mod = null;
		for (Tile o : open) {
			if (o.equals(t)) {
				mod = o;
			}
		}

		if (mod == null) {
			// not on list add as normal
			t.calculateBoth(sx, sy, sz, ex, ey, ez, false);
			open.add(t);
		} else if (modify) {
			// already on list, if we are forcibly modifying then add the newest
			// copy
			t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
			open.remove(mod);
			open.add(t);
		}
	}

	private void addToClosedList(Tile t) {
		for (Tile c : closed) {
			if (c.equals(t)) {
				return;
			}
		}
		closed.add(t);
	}

	private final int maxIterations;

	public AStar(Location start, Location end, int maxIterations) {

		this.w = start.getWorld();
		this.sx = start.getBlockX();
		this.sy = start.getBlockY();
		this.sz = start.getBlockZ();
		this.ex = end.getBlockX();
		this.ey = end.getBlockY();
		this.ez = end.getBlockZ();

		this.maxIterations = maxIterations;

		short s = 0;
		Tile t = new Tile(s, s, s, null);
		t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
		this.open.add(t);
		this.processAdjacentTiles(t);
	}
	
	public Location getEndLocation(){
		return new Location(w, ex, ey, ez);
	}
	
	public int getEndCode(){
		return this.endCode;
	}

	public LinkedList<Tile> iterate() {
		// while not at end
		Tile current = null;

		int iterations = 0;

		while (canContinue()) {

			iterations++;

			if (iterations > this.maxIterations) {
				this.endCode = -2;
				QuestX.logDebug("Pathinder exceeded max iterations!");
				break;
			}

			// get lowest F cost square on open list
			current = this.getLowestFTile();

			// process tiles
			this.processAdjacentTiles(current);
		}

		if (endCode == -1 || endCode == -2) {
			// no path found = -1
			// max iterations exceeded = -2
			return null;
		} else {
			// path found
			LinkedList<Tile> routeTrace = new LinkedList<Tile>();
			Tile parent;

			routeTrace.add(current);

			while ((parent = current.getParent()) != null) {
				routeTrace.add(parent);
				current = parent;
			}

			Collections.reverse(routeTrace);

			return routeTrace;
		}
	}

	private boolean canContinue() {
		// check if open list is empty, if it is no path has been found
		if (open.size() == 0) {
			endCode = -1;
			return false;
		} else {
			for (Tile c : closed) {
				if (((c.getX() + sx) == ex) && ((c.getY() + sy) == ey) && ((c.getZ() + sz) == ez)) {
					// end tile is on closed list
					endCode = 0;
					return false;
				}
			}

			// tile is not on closed list and open list has items, keep
			// searching...
			return true;
		}
	}

	private Tile getLowestFTile() {
		double f = 0;
		Tile drop = null;

		// get lowest F cost square
		for (Tile t : open) {
			if (f == 0) {
				t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
				f = t.getF();
				drop = t;
			} else {
				t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
				double posF = t.getF();
				if (posF < f) {
					f = posF;
					drop = t;
				}
			}
		}

		// drop from open list and add to closed
		this.open.remove(drop);

		this.addToClosedList(drop);

		return drop;
	}

	private boolean isOnClosedList(Tile t) {
		for (Tile c : closed) {
			if (c.equals(t)) {
				return true;
			}
		}
		return false;
	}

	// pass in the current tile as the parent
	private void processAdjacentTiles(Tile current) {

		// set of possible walk to locations adjacent to current tile
		HashSet<Tile> possible = new HashSet<Tile>(26);

		// System.out.println("Processing 3x3x3 grid.");

		for (byte x = -1; x <= 1; x++) {
			for (byte y = -1; y <= 1; y++) {
				for (byte z = -1; z <= 1; z++) {

					if (x == 0 && y == 0 && z == 0) {
						continue;// don't check current square
					}

					Tile t = new Tile((short) (current.getX() + x), (short) (current.getY() + y), (short) (current.getZ() + z), current);

					if (this.isOnClosedList(t)) {
						// ignore tile
						continue;
					} else {
						// continue to further checks
					}

					// only process the tile if it can be walked on
					if (this.isTileWalkable(t)) {
						t.calculateBoth(sx, sy, sz, ex, ey, ez, true);
						possible.add(t);
					} else {
						// tile is not walkable so ignore
					}

				}
			}
		}

		for (Tile t : possible) {
			// get the reference of the object in the array
			Tile openRef = null;
			if ((openRef = this.isOnOpenList(t)) == null) {
				// not on open list, so add
				this.addToOpenList(t, false);
			} else {
				// is on open list, check if path to that square is better using
				// G cost

				if (t.getG() < openRef.getG()) {
					// System.out.println("Tile on open list has worse G than current");
					// if current path is better, change parent
					openRef.setParent(current);
					// force updates of F, G and H values.
					openRef.calculateBoth(sx, sy, sz, ex, ey, ez, true);
				} else {
					// System.out.println("Tile on open list has better G than current, ignoring.");
				}

			}
		}

	}

	private Tile isOnOpenList(Tile t) {
		for (Tile o : open) {
			if (o.equals(t)) {
				return o;
			}
		}
		return null;
	}

	private boolean isTileWalkable(Tile t) {
		Location l = new Location(w, (sx + t.getX()), (sy + t.getY()), (sz + t.getZ()));
		Block b = l.getBlock();
		int i = b.getTypeId();

		// lava, fire, wheat and ladders cannot be walked on, and of course air
		if (i != 10 && i != 11 && i != 51 && i != 59 && i != 65 && i != 0 && !canBlockBeWalkedThrough(i)) {
			// make sure the blocks above are air
			if (canBlockBeWalkedThrough(b.getRelative(0, 1, 0).getTypeId()) && b.getRelative(0, 2, 0).getTypeId() == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean canBlockBeWalkedThrough(int id){
		return(id == 0 || id == 6 || id == 50 || id == 63 || id == 30 || id == 31 || id == 32 || id == 37 || id == 38 || id == 39 || id == 40 || id == 55 || id == 66 || id == 75 || id == 76 || id == 78);
	}

}
