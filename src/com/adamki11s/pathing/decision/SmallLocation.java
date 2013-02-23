package com.adamki11s.pathing.decision;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.adamki11s.io.WorldConfigData;

public class SmallLocation {

	private int x, z;
	private String world;

	public SmallLocation(String w, int x, int z) {
		this.world = w;
		this.x = x;
		this.z = z;
	}
	
	public SmallLocation(Location l) {
		this.world = l.getWorld().getName();
		this.x = l.getBlockX();
		this.z = l.getBlockZ();
	}

	public void update(Location l) {
		if (l != null) {
			String wn = l.getWorld().getName();
			if (!world.equalsIgnoreCase(wn)) {
				world = wn;
			}
			x = l.getBlockX();
			z = l.getBlockZ();
		}
	}
	
	
	public boolean isLocationInPathingRange(Location l){
		//false if different worlds
		if(!this.world.equalsIgnoreCase(l.getWorld().getName())){
			return false;
		} else {
			int dx = this.abs(l.getBlockX() - x), dz = this.abs(l.getBlockZ() - z);
			int dist = (int) Math.sqrt((dx * dx) + (dz * dz));
			//default is 80 blocks of x-z distance euclidean
			return dist < WorldConfigData.getNpcActvityRange();
		}
	}
	
	private int abs(int i){
		return (i < 0 ? -i : i);
	}

	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

}
