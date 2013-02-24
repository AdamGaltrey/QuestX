package com.adamki11s.pathing.preset;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.loading.FixedLoadingTable;
import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.AStar.InvalidPathException;
import com.adamki11s.pathing.Tile;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.serializable.SyncLocation;

public class PresetPathCreation {

	private final String npc, world;

	private final Location start;

	private ArrayList<Location> points = new ArrayList<Location>();
	private ArrayList<BlockState> states = new ArrayList<BlockState>();

	public PresetPathCreation(Location start, String npc, String worldName) {
		this.start = start;
		this.points.add(start);
		this.npc = npc;
		this.world = worldName;
	}

	public Location getStart() {
		return this.start;
	}

	public String getNPC() {
		return this.npc;
	}

	public String getWorld() {
		return this.world;
	}

	public void setLocation(final Player p, final Location l) {
		Location previousPoint = points.get(points.size() - 1);

		// weird bug, block change doesn't show for clicked block so delayed
		// task for clicked block change.

		try {
			AStar machine = new AStar(previousPoint, l, 3000);
			ArrayList<Tile> list = machine.iterate();

			switch (machine.getPathingResult()) {
			case SUCCESS:
				points.add(l);
				for (Tile t : list) {
					Block b = t.getLocation(previousPoint).getBlock();
					BlockState old = new BlockState(b);
					states.add(old);
					p.sendBlockChange(b.getLocation(), Material.WOOL, (byte) 5);
				}
				Bukkit.getServer().getScheduler().runTaskLater(QuestX.p, new Runnable() {
					public void run() {
						p.sendBlockChange(l, Material.WOOL, (byte) 5);
					}
				}, 3L);
				// p.sendBlockChange(l, Material.DIAMOND_BLOCK, (byte) 0);
				// p.sendBlockChange(previousPoint, Material.DIAMOND_BLOCK,
				// (byte) 0);
				QuestX.logChat(p, ChatColor.GREEN + "Path point set successfully.");
				break;
			default:
				QuestX.logChat(p, ChatColor.RED + "No path could be found to that point, please select another.");
				return;
			}
		} catch (InvalidPathException e) {
			StringBuilder error = new StringBuilder().append(ChatColor.RED).append("Invalid block selected");
			if (e.isEndNotSolid()) {
				error.append(" end block is not solid,");
			}
			if (e.isStartNotSolid()) {
				error.append(" start block is not solid,");
			}
			QuestX.logChatError(p, error.toString());
		}
	}

	public void resetBlockStates(Player p) {
		for (BlockState bs : states) {
			bs.restoreBlock(p);
		}
	}

	public void createPath(Player p, NPCHandler handle) {
		Tile[] nodes = this.getFinalTileNodes();
		PresetPath presetPath = new PresetPath(new SyncLocation(this.start), nodes);
		FixedLoadingTable.addPresetPath(npc, presetPath, handle);
		QuestX.logChat(p, ChatColor.GREEN + "Preset path set successfully.");
	}

	private Tile[] getFinalTileNodes() {
		Tile[] tiles = new Tile[this.points.size()];
		Location start = points.remove(0);
		int count = 0;
		while (this.points.size() > 0) {
			tiles[count] = this.getTile(start, this.points.remove(0));
			count++;
		}
		tiles[count] = new Tile((short) 0, (short) 0, (short) 0, null);
		return tiles;
	}

	private Tile getTile(Location start, Location l) {
		short dx = (short) -(start.getBlockX() - l.getBlockX()), dy = (short) -(start.getBlockY() - l.getBlockY()), dz = (short) -(start.getBlockZ() - l.getBlockZ());
		return new Tile(dx, dy, dz, null);
	}

}
