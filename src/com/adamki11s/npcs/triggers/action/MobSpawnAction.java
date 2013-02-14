package com.adamki11s.npcs.triggers.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.adamki11s.ai.dataset.MovementData;
import com.adamki11s.questx.QuestX;

public class MobSpawnAction implements Action {

	/*
	 * SPAWN_MOBS:<MOB NAME>,<QUANTITY>#etc... SPAWN_MOB_RANGE:<BLOCK RANGE>
	 * SPAWN_COOLDOWN_MINUTES:<minutes> DESPAWN_MOB_SECONDS:<ticks>
	 * MOBS_TARGET_PLAYER:<Boolean>
	 */

	private boolean isActive = true;

	private HashMap<EntityType, Integer> mobs = new HashMap<EntityType, Integer>();

	private short blockRange, cooldownMinutes;
	private int despawnSeconds;

	private boolean targetPlayer = false;

	public MobSpawnAction(String npc, String[] data) {
		if (data.length != 5) {
			QuestX.logError("Invalid number of arugaments for Mobspawn, check all values have been entered");
			this.isActive = false;
			return;
		}

		// parse mobs to spawn
		String[] ents = data[0].split("#");
		for (String parse : ents) {
			String[] components = parse.split(",");
			EntityType e = EntityType.valueOf(components[0]);
			if (e == null) {
				QuestX.logError("Could not parse entity type for NPC '" + npc + "' in custom_trigger file.");
				this.isActive = false;
				return;
			}
			// types.add(e);
			int k;
			try {
				k = Integer.parseInt(components[1]);
			} catch (NumberFormatException nfe) {
				QuestX.logError("Number of mobs to spawn was invalid for NPC '" + npc + "' in custom_trigger file.");
				this.isActive = false;
				return;
			}
			this.mobs.put(e, k);
		}

		// parse spawn range, cooldownMinutes and despawnSeconds
		try {
			blockRange = Short.parseShort(data[1]);
			cooldownMinutes = Short.parseShort(data[2]);
			despawnSeconds = Integer.parseInt(data[3]);
		} catch (NumberFormatException nfe) {
			QuestX.logError("Invalid mob spawn integer property for NPC '" + npc + "' in custom_trigger file.");
			this.isActive = false;
			return;
		}

		targetPlayer = Boolean.parseBoolean(data[4]);
	}

	private long timestamp = -1;

	@Override
	public void implement(Player p) {
		if (this.cooldownMinutes != 0) {
			if (timestamp == -1) {
				timestamp = System.currentTimeMillis();
				// execute
			} else {
				long curTime = System.currentTimeMillis();
				long timeDifSeconds = ((curTime - timestamp) / 1000);
				int minuteDifference = (int) Math.floor((timeDifSeconds / 60));
				if (minuteDifference > this.cooldownMinutes) {
					this.spawn(p);
					// execute
				} else {
					// cooldown not completed, return
					return;
				}
			}
		} else {
			this.spawn(p);
			// execute as normal
		}
	}

	/*
	 * SPAWN_MOBS:<MOB NAME>,<QUANTITY>#etc... SPAWN_MOB_RANGE:<BLOCK RANGE>
	 * 
	 * DESPAWN_MOB_SECONDS:<ticks> MOBS_TARGET_PLAYER:<Boolean>
	 */

	private void spawn(Player p) {
		final World w = p.getWorld();
		final ArrayList<Entity> spawnedEntities = new ArrayList<Entity>();
		for (Entry<EntityType, Integer> e : this.mobs.entrySet()) {
			int a = e.getValue();
			for (int cur = 0; cur < a; cur++) {
				Entity bukEntity = w.spawnEntity(this.getRandomLocation(p.getLocation()), e.getKey());
				if(this.targetPlayer){
					Creature creature = (Creature) bukEntity;
					creature.setTarget(p);
				}
				if (this.despawnSeconds > 0) {
					spawnedEntities.add(bukEntity);
				}
			}
		}

		if (this.despawnSeconds > 0) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(QuestX.p, new Runnable() {
				
				public void run() {
					for(Entity e : spawnedEntities){
						if(e != null && !e.isDead()){
							e.remove();
						}
					}
				}
				
			}, (this.despawnSeconds * 20));
		}
	}

	Random r = new Random();

	private Location getRandomLocation(Location l) {
		MovementData md = new MovementData(l, 0, 0, this.blockRange);
		md.generate();
		Location target;
		while((target = md.getEndPoint()) == null){
			md.generate();
		}
		return target;
		/*int xO = r.nextInt(blockRange * 2) - blockRange, yO = r.nextInt(blockRange * 2) - blockRange, zO = r.nextInt(blockRange * 2) - blockRange;
		return new Location(l.getWorld(), l.getBlockX() + xO, l.getBlockY() + yO, l.getBlockZ() + zO);*/
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

}
