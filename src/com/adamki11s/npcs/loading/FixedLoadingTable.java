package com.adamki11s.npcs.loading;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.io.LoadNPCTemplate;
import com.adamki11s.npcs.io.NPCTemplate;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.objects.SyncObjectIO;
import com.adamki11s.sync.io.objects.SyncWrapper;
import com.adamki11s.sync.io.serializable.SyncLocation;

public class FixedLoadingTable {

	static HashMap<String, Location> fixedSpawns = new HashMap<String, Location>();

	final static SyncObjectIO loader = new SyncObjectIO(FileLocator.getNPCFixedSpawnsFile());

	public static void spawnFixedNPCS(NPCHandler handle) {
		QuestX.logMSG("registering npc spawns");
		loader.read();
		QuestX.logMSG("wrapper length = " + loader.getReadableData().size());
		for (SyncWrapper wrapper : loader.getReadableData()) {
			if (wrapper.getTag().equalsIgnoreCase("NPC_COUNT")) {
				continue;
			}
			System.out.println("Wrapper tag = " + wrapper.getTag());
			String npcName = wrapper.getTag();
			SyncLocation sl = (SyncLocation) wrapper.getObject();
			Location spawnLocation = sl.getBukkitLocation();
			LoadNPCTemplate tempLoader = new LoadNPCTemplate(npcName);
			if (tempLoader.wantsToLoad()) {
				tempLoader.loadProperties();
				NPCTemplate template = tempLoader.getLoadedNPCTemplate();
				template.registerSimpleNPCFixedSpawn(handle, spawnLocation);
				fixedSpawns.put(npcName, spawnLocation);
			} else {
				QuestX.logMSG("NPC " + npcName + ", was not loaded because settings define NO load.");
			}
		}
	}

	public static void addFixedNPCSpawn(Player p, String npcName, Location l) {
		if (!FileLocator.doesNPCNameExist(npcName)) {
			QuestX.logChat(p, ChatColor.RED + "There is no NPC created with this name!");
			return;
		} else {
			if (fixedSpawns.containsKey(npcName)) {
				QuestX.logChat(p, "A fixed spawn location for this NPC already exists");
				return;
			}
			loader.read();
			for (SyncWrapper wrap : loader.getReadableData()) {
				loader.add(wrap.getTag(), wrap.getObject());
			}
			loader.add(npcName, new SyncLocation(l));
			loader.write();
			loader.clearReadArray();
			loader.clearWriteArray();

			fixedSpawns.put(npcName, l);

			QuestX.logChat(p, "Fixed spawn created successfully for NPC '" + npcName + "'.");
		}
	}

}
