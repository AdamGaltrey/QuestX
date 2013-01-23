package com.adamki11s.npcs.io;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import com.adamki11s.data.ItemStackDrop;
import com.adamki11s.data.ItemStackProbability;
import com.adamki11s.io.FileLocator;
import com.adamki11s.io.NPCTag;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.configuration.SyncConfiguration;


public class LoadNPCTemplate {

	final String npc_root = FileLocator.npc_data_root;
	final File folder;

	final String name;

	public LoadNPCTemplate(String name) {
		this.name = name;
		this.folder = new File(npc_root + File.separator + name);
	}

	boolean unload = false;

	ChatColor nameColour;
	String inventDrops, gear;

	ItemStackDrop itemStackDrop;
	ItemStack[] npcGear;

	NPCTemplate npcTemplate;

	boolean moveable, attackable, aggressive, load;
	int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks, maxHealth, damageMod;
	double retalliationMultiplier;

	public boolean wantsToLoad() {
		File prop = new File(folder + File.separator + FileLocator.propertyFile);
		if (prop.exists()) {
			SyncConfiguration conf = new SyncConfiguration(prop);
			conf.read();
			return conf.getBoolean(NPCTag.LOAD.toString());
		}
		return false;
	}
	
	public NPCTemplate getLoadedNPCTemplate(){
		return this.npcTemplate;
	}

	public void loadProperties() {
		File prop = FileLocator.getNPCPropertiesFile(name);
		//File prop = new File(folder + File.separator + FileLocator.propertyFile);
		if (prop.exists()) {
			SyncConfiguration conf = new SyncConfiguration(prop);
			conf.read();

			this.nameColour = ChatColor.getByChar(conf.getString(NPCTag.CHAT_COLOUR.toString()));

			this.moveable = conf.getBoolean(NPCTag.MOVEABLE.toString());
			this.attackable = conf.getBoolean(NPCTag.ATTACKABLE.toString());
			System.out.println("Is attackable = " + this.attackable);
			this.aggressive = conf.getBoolean(NPCTag.AGGRESSIVE.toString());

			this.minPauseTicks = conf.getInt(NPCTag.MIN_PAUSE_TICKS.toString());
			this.maxPauseTicks = conf.getInt(NPCTag.MAX_PAUSE_TICKS.toString());
			this.maxVariation = conf.getInt(NPCTag.MAX_VARIATION.toString());
			this.respawnTicks = conf.getInt(NPCTag.RESPAWN_TICKS.toString());
			this.maxHealth = conf.getInt(NPCTag.MAX_HEALTH.toString());
			this.damageMod = conf.getInt(NPCTag.DAMAGE_MODIFIER.toString());

			this.retalliationMultiplier = conf.getDouble(NPCTag.RETALLIATION_MULTIPLIER.toString());

			this.inventDrops = conf.getString(NPCTag.INVENTORY_DROPS.toString());// id,data,quantity,chance#
			this.gear = conf.getString(NPCTag.GEAR.toString());// boots,legs,chest,helm,arm

			String[] inventDropsToParse = this.inventDrops.split("#");

			ItemStackProbability[] ispDrops = new ItemStackProbability[inventDropsToParse.length];

			for (int i = 0; i < inventDropsToParse.length; i++) {
				ispDrops[i] = this.parseISP(inventDropsToParse[i]);
			}

			this.itemStackDrop = new ItemStackDrop(ispDrops);
			this.npcGear = this.parseGear(gear);

			if (!unload) {
				// create template npc
				this.npcTemplate = new NPCTemplate(this.name, nameColour, moveable, attackable, aggressive, minPauseTicks, maxPauseTicks, maxVariation, maxHealth, respawnTicks, itemStackDrop,
						npcGear, damageMod, retalliationMultiplier);
			}

		} else {
			QuestX.logMSG("Could not locate properties file for NPC '" + name + "'.");
		}
	}

	// public ItemStackProbability(ItemStack is, int probability){

	ItemStack[] parseGear(String gear) {
		String[] parts = gear.split(",");
		ItemStack[] g = new ItemStack[5];
		try {
			for (int i = 0; i < 5; i++) {
				g[i] = new ItemStack(Integer.parseInt(parts[i]));
			}
		} catch (NumberFormatException nfe) {
			QuestX.logMSG("NPC " + this.name + " has invalid data in field 'GEAR'.");
			QuestX.logMSG("NPC " + this.name + " unloaded.");
			unload = true;
		}
		return g;
	}

	ItemStackProbability parseISP(String isp) {
		String[] parts = isp.split(",");
		int id = 0, data = 0, quantity = 0, probability = 0;
		try {
			id = Integer.parseInt(parts[0]);
			data = Integer.parseInt(parts[1]);
			quantity = Integer.parseInt(parts[2]);
			probability = Integer.parseInt(parts[3]);
		} catch (NumberFormatException nfe) {
			QuestX.logMSG("NPC " + this.name + " has invalid data in field 'INVENTORY_DROPS'.");
			QuestX.logMSG("NPC " + this.name + " unloaded.");
			unload = true;
		}
		if (probability < 0 || probability > 10000) {
			QuestX.logMSG("NPC " + this.name + " has invalid data in field 'INVENTORY_DROPS'. Probability must be between 0-10000");
			QuestX.logMSG("NPC " + this.name + " unloaded.");
			unload = true;
		}

		ItemStack drop = new ItemStack(id, quantity);
		drop.getData().setData((byte) data);
		return new ItemStackProbability(drop, probability);
	}

}
