package com.topcat.npclib.entity;

import java.util.LinkedList;

import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.AStar.InvalidPathException;
import com.adamki11s.pathing.PathingResult;
import com.adamki11s.pathing.Tile;
import com.adamki11s.questx.QuestX;
import com.topcat.npclib.NPCManager;

public class NPC {

	private Entity entity;
	private int taskid;
	private Location e;

	public NPC(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void removeFromWorld() {
		try {
			entity.world.removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}

	public void teleportTo(Location l) {
		getBukkitEntity().teleport(l);
	}

	private LinkedList<Tile> walkNodes = new LinkedList<Tile>();

	private void move(Location i) {

		if (walkNodes == null) {
			this.stopPathFind();
			QuestX.logDebug("walk nodes are null");
		} else if (i == null) {
			this.stopPathFind();
			QuestX.logDebug("Location init is null");
		} else {

			QuestX.logDebug("Running " + walkNodes.size() + " nodes...");

			if (walkNodes != null && walkNodes.size() > 0) {

				Tile t = walkNodes.removeFirst();

				Location target = new Location(i.getWorld(), t.getX(i), t.getY(i) + 1, t.getZ(i));

				Tile lookTile;

				if (walkNodes.size() > 2) {
					lookTile = walkNodes.get(2);
				} else if (walkNodes.size() != 0) {
					lookTile = walkNodes.getLast();
				} else {
					lookTile = null;
				}

				i.getWorld().playEffect(target, Effect.STEP_SOUND, Material.AIR.getId());

				double newYaw = 0, newPitch = 0;
				
				if (lookTile != null && e != null) {
					
					
					
					Location npcLoc = ((LivingEntity) getEntity().getBukkitEntity()).getEyeLocation();
					double xDiff = lookTile.getX(i) - npcLoc.getX();
					double yDiff = lookTile.getY(i) - (npcLoc.getY() - 1);
					double zDiff = lookTile.getZ(i) - npcLoc.getZ();
					double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
					double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
					 newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
					newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
					if (zDiff < 0.0) {
						newYaw = newYaw + Math.abs(180 - newYaw) * 2;
					}
				}

				if (walkNodes != null && walkNodes.size() != 0) {
					getEntity().setPositionRotation(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, (float)newPitch, (float)newYaw);
				} else {
					getEntity().setPositionRotation(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, (float)newPitch, (float)newYaw);
				}
				
				getEntity().yaw = (float) (newYaw - 90);
				getEntity().pitch = (float) newPitch;

				((EntityPlayer) getEntity()).az =(float) (newYaw - 90);

			} else {
				this.stopPathFind();
				QuestX.logDebug("walk nodes expended");
			}

		}
	}

	public boolean isPathFindComplete() {
		if (this.walkNodes == null || e == null) {
			return true;
		}
		return (this.walkNodes.size() == 0);
	}

	public Location getEndLocation() {
		return this.e;
	}

	public void stopPathFind() {
		Bukkit.getServer().getScheduler().cancelTask(taskid);
		taskid = 0;
		e = null;
		this.walkNodes = null;
	}

	private PathingResult result;

	public PathingResult getPathingResult() {
		return this.result;
	}

	public synchronized void pathFindTo(final Location init, Location end) {

		if (taskid != 0) {
			this.stopPathFind();
			//QuestX.logError("Task did not cancel correctly.");
			return;
		}

		if (!this.isPathFindComplete()) {
			//QuestX.logError("Cannot start movement, old path is incomplete.");
			return;
		}

		while (init.getBlock().getTypeId() == 0) {
			init.subtract(0, 1, 0);
		}

		while (end.getBlock().getTypeId() == 0) {
			end.subtract(0, 1, 0);
		}

		AStar astarPath;
		
		try {
			astarPath = new AStar(init, end, 2000);
			walkNodes = astarPath.iterate();
			this.result = astarPath.getPathingResult();
		} catch (InvalidPathException e1) {
			//start or end is air
			return;
		}
		
		if(result != PathingResult.SUCCESS || walkNodes == null){
			return;
		}


		e = end;

		taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(NPCManager.plugin, new Runnable() {
			@Override
			public void run() {
				// moveToNextTile(init);
				move(init);
			}
		}, 0L, 5L);
	}

}