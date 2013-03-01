package com.adamki11s.npcs.triggers.action;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.ai.dataset.MovementData;
import com.adamki11s.questx.QuestX;

public class LightningAction implements Action {

	short strikes, range, tickDelay;

	private boolean isActive = true, isDamaging = false;

	private int taskID;

	public LightningAction(String npc, String data) {
		// <STRIKES>#<RANGE>#<TICK_DELAY>#damage player
		String[] components = data.split("#");
		try {
			strikes = Short.parseShort(components[0]);
			range = Short.parseShort(components[1]);
			tickDelay = Short.parseShort(components[2]);
			isDamaging = Boolean.parseBoolean(components[3]);
		} catch (NumberFormatException nfe) {
			isActive = false;
			QuestX.logError("Error parsing value for 'LIGHTNING' for NPC '" + npc + "' in custom_trigger. Setting disabled");
		} catch (Exception e) {
			isActive = false;
			QuestX.logError("Invalid parameters for 'LIGHTNING' for NPC '" + npc + "' in custom_trigger. Setting disabled.");
		}
	}

	@Override
	public void implement(final Player p) {
		if(taskID != 0){
			return;
		}
			
		final Location c = p.getLocation();
		taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(QuestX.p, new Runnable() {

			short sCount;

			public void run() {
				sCount++;
				if (sCount <= strikes) {
					//short xO = (short) (r.nextInt(range * 2) - range), yO = (short) (r.nextInt(range * 2) - range), zO = (short) (r.nextInt(range * 2) - range);
					//Location hit = new Location(c.getWorld(), c.getX() + xO, c.getY() + yO, c.getZ() + zO);
					Location hit = getRandomLocation(c);
					if (isDamaging) {
						hit.getWorld().strikeLightning(hit);
					} else {
						hit.getWorld().strikeLightningEffect(hit);
					}
				} else {
					Bukkit.getServer().getScheduler().cancelTask(taskID);
					taskID = 0;
				}
			}

		}, tickDelay, tickDelay);
	}
	
	private Location getRandomLocation(Location l) {
		MovementData md = new MovementData(l, 0, 0, this.range);
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
