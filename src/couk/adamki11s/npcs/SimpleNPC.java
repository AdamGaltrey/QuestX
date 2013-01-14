package couk.adamki11s.npcs;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.topcat.npclib.entity.NPC;

public class SimpleNPC {

	final String name;
	final ChatColor nameColour;
	final Location rootLocation;
	final boolean moveable, attackable, aggressive;
	final int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks;

	final NPCHandler handle;

	NPC npc;
	boolean isSpawned = false;

	int health;

	public SimpleNPC(NPCHandler handle, String name, ChatColor nameColour, Location rootLocation, boolean moveable, boolean attackable, boolean aggressive, int minPauseTicks,
			int maxPauseTicks, int maxVariation, int health, int respawnTicks) {
		UniqueNameRegister.addNPCName(name);
		this.name = name;
		this.nameColour = nameColour;
		this.rootLocation = rootLocation;
		this.moveable = moveable;
		this.attackable = attackable;
		this.aggressive = aggressive;
		this.minPauseTicks = minPauseTicks;
		this.maxPauseTicks = maxPauseTicks;
		this.maxVariation = maxVariation;
		this.health = health;
		this.respawnTicks = respawnTicks;
		this.handle = handle;
	}

	public void spawnNPC() {
		if (!isSpawned) {
			this.npc = this.handle.getNPCManager().spawnHumanNPC(this.name, this.rootLocation);
			isSpawned = true;
		}
	}

	public void despawnNPC() {
		if (isSpawned) {
			this.handle.getNPCManager().despawnHumanByName(this.name);
		}
	}

	public void destroyNPCObject() {
		UniqueNameRegister.removeName(name);
	}

	public int getRespawnTicks() {
		return this.respawnTicks;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getName() {
		return name;
	}

	public ChatColor getNameColour() {
		return nameColour;
	}

	public Location getRootLocation() {
		return rootLocation;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public boolean isAttackable() {
		return attackable;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public int getMinPauseTicks() {
		return minPauseTicks;
	}

	public int getMaxPauseTicks() {
		return maxPauseTicks;
	}

	public int getMaxVariation() {
		return maxVariation;
	}

}
