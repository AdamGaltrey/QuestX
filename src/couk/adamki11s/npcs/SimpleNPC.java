package couk.adamki11s.npcs;

import net.minecraft.server.v1_4_6.Entity;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.Packet;
import net.minecraft.server.v1_4_6.Packet5EntityEquipment;
import net.minecraft.server.v1_4_6.WorldServer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;

import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

//import net.minecraft.server.EntityLiving;

import couk.adamki11s.ai.RandomMovement;

public class SimpleNPC {

	final String name;
	final ChatColor nameColour;
	final Location rootLocation;
	final boolean moveable, attackable, aggressive;
	final int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks;

	RandomMovement randMovement;

	final NPCHandler handle;

	HumanNPC npc;
	boolean isSpawned = false, underAttack = false;

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

		handle.registerNPC(this);
	}

	public void setBoots(ItemStack item) {
		this.npc.getInventory().setBoots(item);
		this.updateArmor(1, item);
	}
	
	public void setLegs(ItemStack item) {
		this.npc.getInventory().setLeggings(item);
		this.updateArmor(2, item);
	}
	
	public void setChestplate(ItemStack item) {
		this.npc.getInventory().setChestplate(item);
		this.updateArmor(3, item);
	}
	
	public void setHelmet(ItemStack item) {
		this.npc.getInventory().setHelmet(item);
		this.updateArmor(4, item);
	}
	
	public void damageNPC(Player p, int damage){
		//set under attack and change AI
		//+ drop loot on death
		//AI state = run/fight depending on character and on player he is fighting
		health -= damage;
		if(health <= 0){
			p.sendMessage("You killed NPC '" + this.getName() + "'.");
			this.destroyNPCObject();
		}
	}

	public void updateArmor(int slot, org.bukkit.inventory.ItemStack itm) {
		net.minecraft.server.v1_4_6.ItemStack i = CraftItemStack.asNMSCopy(itm);
		Packet p = new Packet5EntityEquipment(this.npc.getEntity().id, slot, i);
		((WorldServer) this.npc.getEntity().world).tracker.a(this.npc.getEntity(), p);
	}

	public HumanNPC getHumanNPC() {

		return this.npc;
	}

	public boolean doesNPCIDMatch(String id) {
		return ((HumanNPC) this.handle.getNPCManager().getNPC(id)).getName().equalsIgnoreCase(this.npc.getName());
	}

	public void spawnNPC() {
		if (!isSpawned) {
			this.npc = (HumanNPC) this.handle.getNPCManager().spawnHumanNPC(this.name, this.rootLocation);
			isSpawned = true;
			if (moveable) {
				this.randMovement = new RandomMovement(this.npc, this.rootLocation, this.minPauseTicks, this.maxPauseTicks, this.maxVariation);
			}
		}
	}

	public boolean isNPCSpawned() {
		return this.isSpawned;
	}
	
	public boolean isUnderAttack(){
		return this.underAttack;
	}

	public void despawnNPC() {
		if (isSpawned) {
			this.handle.getNPCManager().despawnHumanByName(this.name);
			this.randMovement = null;
		}
	}

	public void destroyNPCObject() {
		this.despawnNPC();
		UniqueNameRegister.removeName(name);
		this.handle.removeNPC(this);
	}

	public void moveTick() {
		this.randMovement.move();
	}

	public void moveTo(Location l) {
		this.npc.walkTo(l);
	}

	public void lookAt(Location l) {
		this.npc.lookAtPoint(l);
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
