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
import couk.adamki11s.data.ItemStackDrop;
import couk.adamki11s.dialogue.Conversation;
import couk.adamki11s.events.ConversationRegister;
import couk.adamki11s.io.FileLocator;

public class SimpleNPC {

	final String name;
	final ChatColor nameColour;
	final Location rootLocation;
	final boolean moveable, attackable, aggressive;
	final int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks, maxHealth, damageMod;
	final double retalliationMultiplier;
	final ItemStackDrop inventory;

	int waitedSpawnTicks = 0;

	RandomMovement randMovement;

	Conversation c;

	final NPCHandler handle;

	HumanNPC npc;
	boolean isSpawned = false, underAttack = false;
	
	final ItemStack[] gear;//boots 1, legs 2, chest 3, head 4, arm 5

	int health;

	public SimpleNPC(NPCHandler handle, String name, ChatColor nameColour, Location rootLocation, boolean moveable, boolean attackable, boolean aggressive, int minPauseTicks,
			int maxPauseTicks, int maxVariation, int health, int respawnTicks, ItemStackDrop inventory, ItemStack[] gear, int damageMod, double retalliationMultiplier) {
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
		this.maxHealth = health;
		this.respawnTicks = respawnTicks;
		this.handle = handle;
		this.inventory = inventory;
		this.gear = gear;
		this.damageMod = damageMod;
		this.retalliationMultiplier = retalliationMultiplier;

		handle.registerNPC(this);
	}

	public double getRetalliationMultiplier() {
		return retalliationMultiplier;
	}

	public int getDamageMod() {
		return damageMod;
	}

	public Conversation getC() {
		return c;
	}

	public ItemStack[] getGear() {
		return gear;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public ItemStackDrop getInventory() {
		return inventory;
	}

	public int getWaitedSpawnTicks() {
		return waitedSpawnTicks;
	}

	public RandomMovement getRandMovement() {
		return randMovement;
	}

	public NPCHandler getHandle() {
		return handle;
	}

	public HumanNPC getNpc() {
		return npc;
	}

	public boolean isSpawned() {
		return isSpawned;
	}

	public boolean isConversing() {
		if (this.c == null) {
			return false;
		} else {
			return this.c.isConversing();
		}
	}
	
	public void setItemInHand(ItemStack item) {
		this.npc.getInventory().setItemInHand(item);
		this.updateArmor(0, item);
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

	Player aggressor;

	public Player getAggressor() {
		return this.aggressor;
	}

	public void unAggro() {
		this.aggressor = null;
		this.underAttack = false;
	}

	public void damageNPC(Player p, int damage) {
		health -= damage;
		this.aggressor = p;
		this.underAttack = true;
		if(this.isConversing()){
			ConversationRegister.endPlayerNPCConversation(c.getConvoData().getPlayer());
		}
		if (health <= 0) {
			p.sendMessage("You killed NPC '" + this.getName() + "'. NPC will respawn in " + this.respawnTicks / 20 + " seconds.");
			for (ItemStack i : this.inventory.getDrops()) {
				p.getWorld().dropItemNaturally(this.npc.getBukkitEntity().getLocation(), i);
			}
			this.despawnNPC();
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

	public void updateWaitedSpawnTicks(int ticks) {
		if (!this.isNPCSpawned()) {
			this.waitedSpawnTicks += ticks;
			if (this.waitedSpawnTicks >= this.respawnTicks) {
				this.spawnNPC();
			}
		}
	}

	public void interact(Player p) {
		if (!this.isConversing() && !this.isUnderAttack()) {
			if (!FileLocator.doesNPCDlgFileExist(this.getName())) {
				p.sendMessage(ChatColor.AQUA + "[QuestX] " + ChatColor.RED + "No dialogue.dlg file found or it is empty!");
			} else {
				c = new Conversation(p, this);
				c.loadConversation();
				c.startConversation();
				Location pl = p.getLocation();
				this.getHumanNPC().lookAtPoint(new Location(pl.getWorld(), pl.getX(), pl.getY() + 1, pl.getZ()));
				System.out.println("Conversing = " + this.isConversing());
			}
		}
	}

	public boolean doesNPCIDMatch(String id) {
		return ((HumanNPC) this.handle.getNPCManager().getNPC(id)).getName().equalsIgnoreCase(this.npc.getName());
	}

	public void spawnNPC() {
		if (!isSpawned) {

			this.health = this.maxHealth;
			this.waitedSpawnTicks = 0;
			System.out.println("Spawning NPC " + this.getName());
			this.npc = (HumanNPC) this.handle.getNPCManager().spawnHumanNPC(this.name, this.rootLocation);
			isSpawned = true;
			this.setBoots(this.gear[0]);
			this.setLegs(this.gear[1]);
			this.setChestplate(this.gear[2]);
			this.setHelmet(this.gear[3]);
			this.setItemInHand(this.gear[4]);
			if (moveable) {
				this.randMovement = new RandomMovement(this, this.rootLocation, this.minPauseTicks, this.maxPauseTicks, this.maxVariation);
			}
		}
	}

	public boolean isNPCSpawned() {
		return this.isSpawned;
	}

	public boolean isUnderAttack() {
		return this.underAttack;
	}

	public void despawnNPC() {
		if (isSpawned) {
			this.isSpawned = false;
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
