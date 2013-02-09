package com.adamki11s.guidance;

import java.lang.ref.SoftReference;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.adamki11s.questx.QuestX;

public class LocationGuider {

	final String player, worldName;
	final int x, y, z;
	int id1, id2;

	SoftReference<World> worldRef;

	public LocationGuider(String player, String worldName, int x, int y, int z) {
		this.player = player;
		this.worldName = worldName;
		this.getWorld();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	private World getWorld() {
		if (this.worldRef != null) {
			return this.worldRef.get();
		} else {
			World w = Bukkit.getServer().getWorld(this.worldName);
			this.worldRef = new SoftReference<World>(w);
			return w;
		}
	}

	public void drawPath() {
		final Player p = Bukkit.getServer().getPlayer(this.player);

		final Location start = p.getLocation(), end = new Location(this.getWorld(), x, y, z);
		int xmod, zmod;

		if (start.getBlockX() > end.getBlockX()) {
			xmod = -1;
		} else {
			xmod = 1;
		}

		if (start.getBlockZ() > end.getBlockZ()) {
			zmod = -1;
		} else {
			zmod = 1;
		}

		final Location[] generate = new Location[10];
		final int[] old = new int[10];
		final byte[] data = new byte[10];

		World w = getWorld();

		int xmark = -1, zmark = -1;
		for (int i = 0; i < 10; i++) {

			if ((start.getBlockX() + (i * xmod)) == end.getBlockX()) {
				xmark = i;
			}

			if ((start.getBlockZ() + (i * zmod)) == end.getBlockZ()) {
				zmark = i;
			}

			if (xmark != -1 && zmark != -1) {
				generate[i] = new Location(w, start.getBlockX() + xmark, start.getBlockY(), start.getBlockZ() + zmark);
			} else if(xmark == -1 && zmark == -1){
				generate[i] = new Location(w, start.getBlockX() + (i * xmod), start.getBlockY(), start.getBlockZ() + (i * zmod));
			} else if(xmark == -1){
				generate[i] = new Location(w, start.getBlockX() + (i * xmod), start.getBlockY(), start.getBlockZ() + zmark);
			} else {
				generate[i] = new Location(w, start.getBlockX() + xmark, start.getBlockY(), start.getBlockZ() + (i * zmod));
			}

		}

		id1 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(QuestX.p, new Runnable() {
			int count = 0;

			public void run() {
				if (count > 9) {
					Bukkit.getServer().getScheduler().cancelTask(id1);
				} else {
					Location l = generate[count];
					Block b = l.getBlock();
					old[count] = b.getTypeId();
					data[count] = b.getData();
					p.sendBlockChange(l, Material.GLOWSTONE, (byte) 0);
					count++;
				}
			}
		}, 0L, 10L);

		id2 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(QuestX.p, new Runnable() {
			int count = 0;

			public void run() {
				if (count > 9) {
					Bukkit.getServer().getScheduler().cancelTask(id2);
				} else {
					Location l = generate[count];
					p.sendBlockChange(l, old[count], data[count]);
					count++;
				}
			}
		}, 30L, 10L);

	}
}
