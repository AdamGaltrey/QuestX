package couk.adamki11s.io.npc;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import couk.adamki11s.data.ItemStackDrop;
import couk.adamki11s.io.FileLocator;
import couk.adamki11s.io.NPCTag;
import couk.adamki11s.io.SyncConfiguration;
import couk.adamki11s.io.SyncWriter;

public class CreateNPC {

	final String npc_root = FileLocator.npc_data_root;
	final File folder;

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
	}
	
	
	String inventDrops, gear;
	boolean moveable, attackable, aggressive, load;
	int minPauseTicks, maxPauseTicks, maxVariation, respawnTicks, maxHealth, damageMod;
	double retalliationMultiplier;

	public void setProperties(boolean moveable, boolean attackable, boolean aggressive, boolean load, int minPauseTicks, int maxPauseTicks, int maxVariation, int respawnTicks,
			int maxHealth, int damageMod, double retalliationMultiplier, String inventDrops, String gear) {
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
	
	public void createNPCFiles(){
		folder.mkdirs();
		File prop = new File(folder + File.separator + FileLocator.propertyFile),
		trig = new File(folder + File.separator + FileLocator.triggerScriptFile),
		dlg = new File(folder + File.separator + FileLocator.dlgFile);
		
		try{
		prop.createNewFile();
		trig.createNewFile();
		dlg.createNewFile();
		} catch (IOException iox){
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
		write.addString("1#say#1#\"Default Speech.\"#a#e");
		write.addString("1#reply#1#\"Default Response.\"");
		write.write();
		
		write = new SyncWriter(trig);
		write.addString("HEAD:NULL");
		write.write();
		
		
	}

}
