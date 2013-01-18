package couk.adamki11s.io.npc;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import couk.adamki11s.data.ItemStackDrop;
import couk.adamki11s.io.FileLocator;
import couk.adamki11s.io.NPCTag;
import couk.adamki11s.io.SyncConfiguration;
import couk.adamki11s.io.SyncWriter;

public class CreateNPC {

	final String npc_root = FileLocator.npc_data_root;
	final File folder, progFolder;

	public static boolean isNameUnique(String name) {
		File f = new File(FileLocator.npc_data_root + File.separator + name);
		return (!f.exists());
	}

	final ChatColor nameColour;
	final String name;

	public CreateNPC(String name, ChatColor nameColour) {
		this.name = name;
		this.nameColour = nameColour;
		this.folder = new File(npc_root + File.separator + name);
		this.progFolder = new File(npc_root + File.separator + name + File.separator + "Progression");
	}

	String inventDrops, gear;
	boolean moveable, attackable, aggressive, load;
	int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks, maxHealth, damageMod;
	double retalliationMultiplier;

	public void setProperties(boolean moveable, boolean attackable, boolean aggressive, boolean load, int minPauseTicks, int maxPauseTicks, int maxVariation,
			int respawnTicks, int maxHealth, int damageMod, double retalliationMultiplier, String inventDrops, String gear) {
		this.moveable = moveable;
		this.attackable = attackable;
		this.aggressive = aggressive;
		this.load = load;
		this.minPauseTicks = minPauseTicks;
		this.maxPauseTicks = maxPauseTicks;
		this.maxVariation = maxVariation;
		this.respawnTicks = respawnTicks;
		this.maxHealth = maxHealth;
		this.damageMod = damageMod;
		this.retalliationMultiplier = retalliationMultiplier;
		this.inventDrops = inventDrops;
		this.gear = gear;
	}

	public void createNPCFiles() {
		folder.mkdirs();
		progFolder.mkdirs();

		File prop = FileLocator.getNPCPropertiesFile(this.name), task = FileLocator.getNPCTaskFile(this.name), dlg = FileLocator.getNPCDlgFile(this.name);

		try {
			prop.createNewFile();
			task.createNewFile();
			dlg.createNewFile();
		} catch (IOException iox) {
			iox.printStackTrace();
		}

		SyncConfiguration syncConfig = new SyncConfiguration(prop);

		syncConfig.add(NPCTag.LOAD.toString(), "true");
		syncConfig.add(NPCTag.NAME.toString(), this.name);
		syncConfig.add(NPCTag.CHAT_COLOUR.toString(), this.nameColour.getChar());
		syncConfig.add(NPCTag.MOVEABLE.toString(), this.moveable);
		syncConfig.add(NPCTag.ATTACKABLE.toString(), this.attackable);
		syncConfig.add(NPCTag.AGGRESSIVE.toString(), this.aggressive);
		syncConfig.add(NPCTag.MIN_PAUSE_TICKS.toString(), this.minPauseTicks);
		syncConfig.add(NPCTag.MAX_PAUSE_TICKS.toString(), this.maxPauseTicks);
		syncConfig.add(NPCTag.MAX_VARIATION.toString(), this.maxVariation);
		syncConfig.add(NPCTag.RESPAWN_TICKS.toString(), this.respawnTicks);
		syncConfig.add(NPCTag.MAX_HEALTH.toString(), this.maxHealth);
		syncConfig.add(NPCTag.DAMAGE_MODIFIER.toString(), this.damageMod);
		syncConfig.add(NPCTag.RETALLIATION_MULTIPLIER.toString(), this.retalliationMultiplier);
		syncConfig.add(NPCTag.INVENTORY_DROPS.toString(), this.inventDrops);
		syncConfig.add(NPCTag.GEAR.toString(), this.gear);

		syncConfig.write();

		SyncWriter write = new SyncWriter(dlg);
		write.addString("1#say#1#\"Give me a task!\"#a#t");
		write.addString("1#reply#1#\"Okay!\"");
		write.write();

		syncConfig = new SyncConfiguration(task);
		syncConfig.add("TASK_NAME", "Fetch, Kill, Return");
		syncConfig.add("TASK_DESCRIPTION", "Fetch 5 stone and kill 7 cows");
		syncConfig.add("FETCH_ITEMS", "1:0:5");// Format -->
												// <id>:<data>:<quantity>,
		syncConfig.add("KILL_ENTITIES", EntityType.COW.toString() + ":7");
		
		syncConfig.add("INCOMPLETE_TASK_SPEECH", "Talk to me when you have finished the task!");
		syncConfig.add("COMPLETE_TASK_SPEECH", "Congratulations, enjoy your reward!");
		
		syncConfig.add("REWARD_ITEMS", "0");// item stacks
		syncConfig.add("REWARD_EXP", "0");// reward exp
		syncConfig.add("REWARD_REP", "0");// reward reputation
		syncConfig.write();

	}

}
