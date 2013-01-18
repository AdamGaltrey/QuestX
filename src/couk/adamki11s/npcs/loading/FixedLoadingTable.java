package couk.adamki11s.npcs.loading;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import couk.adamki11s.io.FileLocator;
import couk.adamki11s.questx.QuestX;
import couk.adamki11s.sync.io.objects.SyncObjectIO;
import couk.adamki11s.sync.io.objects.SyncWrapper;
import couk.adamki11s.sync.io.serializable.SyncLocation;

public class FixedLoadingTable {
	
	static HashMap<String, Location> fixedSpawns = new HashMap<String, Location>();
	
	final static SyncObjectIO loader = new SyncObjectIO(FileLocator.getNPCFixedSpawnsFile());
	
	public static void registerFixedNPCSpawns(){
		loader.read();
		for(SyncWrapper wrapper : loader.getReadableData()){
			String npcName = wrapper.getTag();
			SyncLocation sl = (SyncLocation) wrapper.getObject();
			Location spawnLocation = sl.getBukkitLocation();
			fixedSpawns.put(npcName, spawnLocation);
		}
	}
	
	public static void addFixedNPCSpawn(Player p, String npcName, Location l){
		if(!FileLocator.doesNPCNameExist(npcName)){
			QuestX.logChat(p, ChatColor.RED + "There is no NPC created with this name!");
			return;
		} else {
			loader.read();
			loader.insertWriteableData(loader.getReadableData());
			loader.add(npcName, new SyncLocation(l));
			loader.write();
			loader.clearReadArray();
			loader.clearWriteArray();
			
			fixedSpawns.put(npcName, l);
			
			QuestX.logChat(p, "Fixed spawn created successfully for NPC '" + npcName + "'.");
		}
	}

}
