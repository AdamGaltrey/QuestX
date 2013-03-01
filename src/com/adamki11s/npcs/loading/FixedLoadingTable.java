package com.adamki11s.npcs.loading;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.display.FixedSpawnsDisplay;
import com.adamki11s.display.PresetPathsDisplay;
import com.adamki11s.io.FileLocator;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.io.LoadNPCTemplate;
import com.adamki11s.npcs.io.NPCTemplate;
import com.adamki11s.pathing.preset.PresetPath;
import com.adamki11s.questx.QuestX;
import com.adamki11s.sync.io.objects.SyncObjectIO;
import com.adamki11s.sync.io.objects.SyncWrapper;
import com.adamki11s.sync.io.serializable.SyncLocation;

public class FixedLoadingTable {

	static volatile HashMap<String, Location> fixedSpawns = new HashMap<String, Location>();

	private final static SyncObjectIO loader = new SyncObjectIO(FileLocator.getNPCFixedSpawnsFile());

	private final static SyncObjectIO io = new SyncObjectIO(FileLocator.getNPCPresetPathingFile());

	public static HashSet<String> presetNPCs = new HashSet<String>();

	public static String[] getFixedSpawns() {
		HashSet<String> ret = new HashSet<String>(fixedSpawns.size());
		for (Entry<String, Location> e : fixedSpawns.entrySet()) {
			ret.add(e.getKey());
		}
		String[] toSort = ret.toArray(new String[ret.size()]);
		Arrays.sort(toSort, String.CASE_INSENSITIVE_ORDER);
		return toSort;
	}

	public static void addPresetPath(String npcName, PresetPath path, NPCHandler handle) {
		io.read();
		for (SyncWrapper wrap : io.getReadableData()) {
			io.add(wrap.getTag(), wrap.getObject());
		}
		io.add(npcName, path);
		io.write();
		presetNPCs.add(npcName);

		SimpleNPC rem = handle.getSimpleNPCByName(npcName);
		if (rem != null) {
			rem.destroyNPCObject();
		}

		LoadNPCTemplate tmp = new LoadNPCTemplate(npcName);

		tmp.loadProperties();

		SimpleNPC npc = tmp.getLoadedNPCTemplate().registerSimpleNPCFixedSpawn(handle, fixedSpawns.get(npcName));

		npc.setPresetPath(path);

		PresetPathsDisplay.updateSoftReference();
	}

	public static boolean removePresetPath(Player p, String npcName, NPCHandler handle) {
		if (!FileLocator.doesNPCNameExist(npcName)) {
			if (p != null) {
				QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("no_npc_name"));
			}
			return false;
		} else {
			if (!fixedSpawns.containsKey(npcName)) {
				if (p != null) {
					QuestX.logChat(p, LocaleBundle.getString("no_fixed_spawn"));
				}
				return false;
			} else {
				if (!presetNPCs.contains(npcName)) {
					QuestX.logChat(p, LocaleBundle.getString("no_preset_path"));
					return false;
				} else {
					io.read();
					io.clearWriteArray();
					io.clearReadArray();
					for (SyncWrapper wrap : loader.getReadableData()) {
						// copy all the data read, except the npc to remove
						if (!wrap.getTag().equalsIgnoreCase(npcName)) {
							io.add(wrap);
						}
					}
					io.write();

					presetNPCs.remove(npcName);

					SimpleNPC rem = handle.getSimpleNPCByName(npcName);
					if (rem != null) {
						rem.destroyNPCObject();
					}

					LoadNPCTemplate tmp = new LoadNPCTemplate(npcName);

					tmp.loadProperties();

					SimpleNPC npc = tmp.getLoadedNPCTemplate().registerSimpleNPCFixedSpawn(handle, fixedSpawns.get(npcName));

					npc.setAllowedToMove(true);

					PresetPathsDisplay.updateSoftReference();
					QuestX.logChat(p, ChatColor.GREEN + "Preset path for NPC " + npcName + " was removed.");
					return true;
				}
			}
		}
	}

	public static String[] getPresetPaths() {
		String[] toSort = presetNPCs.toArray(new String[presetNPCs.size()]);
		Arrays.sort(toSort, String.CASE_INSENSITIVE_ORDER);
		return toSort;
	}

	public static void spawnFixedNPCS(NPCHandler handle) {
		loader.read();
		io.read();

		for (SyncWrapper wrap : io.getReadableData()) {
			if (!wrap.getTag().equalsIgnoreCase("NULL")) {
				presetNPCs.add(wrap.getTag());
			}
		}

		QuestX.logDebug("wrapper length = " + loader.getReadableData().size());
		for (SyncWrapper wrapper : loader.getReadableData()) {
			if (wrapper.getTag().equalsIgnoreCase("NPC_COUNT")) {
				continue;
			}
			String npcName = wrapper.getTag();
			if (FileLocator.doesNPCNameExist(npcName)) {
				SyncLocation sl = (SyncLocation) wrapper.getObject();
				Location spawnLocation = sl.getBukkitLocation();

				LoadNPCTemplate tempLoader = new LoadNPCTemplate(npcName);

				tempLoader.loadProperties();
				NPCTemplate template = tempLoader.getLoadedNPCTemplate();

				SimpleNPC npc = template.registerSimpleNPCFixedSpawn(handle, spawnLocation);

				npc.setAllowedToMove(false);

				if (io.doesObjectExist(npcName)) {
					PresetPath path = (PresetPath) io.getObject(npcName);
					npc.setPresetPath(path);
				}

				npc.setAllowedToMove(true);

				fixedSpawns.put(npcName, spawnLocation);

			} else {
				QuestX.logError("Tried to load NPC '" + npcName + "' but no NPC file was found.");
			}
		}
		FixedSpawnsDisplay.updateSoftReference();
		PresetPathsDisplay.updateSoftReference();
	}

	public static void spawnFixedNPC(NPCHandler handle, String name) {
		loader.read();
		io.read();
		SyncLocation sl = (SyncLocation) loader.getObject(name);
		Location spawnLocation = sl.getBukkitLocation();
		LoadNPCTemplate tempLoader = new LoadNPCTemplate(name);

		tempLoader.loadProperties();

		NPCTemplate template = tempLoader.getLoadedNPCTemplate();

		SimpleNPC npc = template.registerSimpleNPCFixedSpawn(handle, spawnLocation);

		npc.setAllowedToMove(false);

		if (io.doesObjectExist(name)) {
			PresetPath path = (PresetPath) io.getObject(name);
			npc.setPresetPath(path);
		}

		npc.setAllowedToMove(true);

	}

	public static boolean doesNPCHaveFixedSpawn(String npc) {
		return fixedSpawns.containsKey(npc);
	}

	public static final void deleteAllFixedSpawns(Player p, NPCHandler handle) {
		File spawn = FileLocator.getNPCFixedSpawnsFile(), pres = FileLocator.getNPCPresetPathingFile();
		if (spawn.canRead() && spawn.canWrite()) {
			if (spawn.exists()) {
				loader.read();
				for (SyncWrapper wrap : loader.getReadableData()) {
					SimpleNPC rem = handle.getSimpleNPCByName(wrap.getTag());
					if (rem != null) {
						rem.destroyNPCObject();
					}
				}
				spawn.delete();
				try {
					spawn.createNewFile();
					loader.add("NPC_COUNT", 0);
					loader.write();
					QuestX.logChat(p, LocaleBundle.getString("all_fxied_spawns_del"));
				} catch (IOException e) {
					QuestX.logChat(p, LocaleBundle.getString("error_del_fs_file"));
					e.printStackTrace();
				}
			} else {
				QuestX.logChat(p, LocaleBundle.getString("no_fs_file"));
			}
		} else {
			QuestX.logChat(p, LocaleBundle.getString("fs_file_locked"));
		}

		presetNPCs.clear();

		if (pres.canRead() && pres.canWrite()) {
			if (pres.exists()) {
				pres.delete();
				try {
					pres.createNewFile();
					io.clearWriteArray();
					io.add("NULL", 0);
					io.write();
					QuestX.logChat(p, LocaleBundle.getString("all_preset_del"));
				} catch (IOException e) {
					QuestX.logChat(p, LocaleBundle.getString("error_del_pp_file"));
					e.printStackTrace();
				}
			} else {
				QuestX.logChat(p, LocaleBundle.getString("no_pp_file"));
			}
		} else {
			QuestX.logChat(p, LocaleBundle.getString("pp_file_locked"));
		}

		FixedSpawnsDisplay.updateSoftReference();
		PresetPathsDisplay.updateSoftReference();
	}

	public static boolean editFixedNPCSpawn(Player p, String npcName, NPCHandler handle) {
		if (!FileLocator.doesNPCNameExist(npcName)) {
			if (p != null) {
				QuestX.logChat(p, ChatColor.RED + "There is no NPC created with this name!");
			}
			return false;
		} else {
			if (!fixedSpawns.containsKey(npcName)) {
				if (p != null) {
					QuestX.logChat(p, LocaleBundle.getString("no_fixed_spawn"));
				}
				return false;
			} else {

				SimpleNPC rem = handle.getSimpleNPCByName(npcName);
				if (rem != null) {
					rem.destroyNPCObject();
				}

				loader.read();
				io.read();
				loader.clearWriteArray();
				for (SyncWrapper wrap : loader.getReadableData()) {
					// copy all the data read, except the npc to remove, set
					// this to be edited
					if (!wrap.getTag().equalsIgnoreCase(npcName)) {
						loader.add(wrap);
					} else {
						if (p != null) {
							loader.add(wrap.getTag(), new SyncLocation(p.getLocation()));
						}
					}
				}

				loader.write();
				loader.clearReadArray();
				loader.clearWriteArray();

				io.read();
				io.clearWriteArray();
				io.clearReadArray();
				for (SyncWrapper wrap : loader.getReadableData()) {
					// copy all the data read, except the npc to remove
					if (!wrap.getTag().equalsIgnoreCase(npcName)) {
						io.add(wrap);
					}
				}
				io.write();

				if (presetNPCs.contains(npcName)) {
					QuestX.logChat(p, "The preset path for NPC '" + npcName + "' was removed.");
					presetNPCs.remove(npcName);
				}

				LoadNPCTemplate tmp = new LoadNPCTemplate(npcName);

				tmp.loadProperties();

				SimpleNPC npc = tmp.getLoadedNPCTemplate().registerSimpleNPCFixedSpawn(handle, p.getLocation());

				npc.setAllowedToMove(true);

				if (p != null) {
					QuestX.logChat(p, "The fixed spawn for NPC '" + npcName + "' was changed to your current location.");
				}

				PresetPathsDisplay.updateSoftReference();

				return true;
			}
		}
	}

	public static boolean removeFixedNPCSpawn(Player p, String npcName, NPCHandler handle) {
		if (!FileLocator.doesNPCNameExist(npcName)) {
			if (p != null) {
				QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("no_npc_name"));
			}
			return false;
		} else {
			if (!fixedSpawns.containsKey(npcName)) {
				if (p != null) {
					QuestX.logChat(p, LocaleBundle.getString("no_fixed_spawn"));
				}
				return false;
			} else {
				SimpleNPC rem = handle.getSimpleNPCByName(npcName);
				if (rem != null) {
					rem.destroyNPCObject();
				}

				loader.read();
				loader.clearWriteArray();
				for (SyncWrapper wrap : loader.getReadableData()) {
					// copy all the data read, except the npc to remove
					if (!wrap.getTag().equalsIgnoreCase(npcName)) {
						loader.add(wrap);
					}
				}
				loader.write();
				loader.clearReadArray();
				loader.clearWriteArray();

				fixedSpawns.remove(npcName);

				io.read();
				io.clearWriteArray();
				io.clearReadArray();
				for (SyncWrapper wrap : loader.getReadableData()) {
					// copy all the data read, except the npc to remove
					if (!wrap.getTag().equalsIgnoreCase(npcName)) {
						io.add(wrap);
					}
				}
				io.write();

				if (p != null) {
					QuestX.logChat(p, LocaleBundle.getString("t_fs") + npcName + LocaleBundle.getString("was_rem"));
					if (presetNPCs.contains(npcName)) {
						QuestX.logChat(p, LocaleBundle.getString("t_pp") + npcName + LocaleBundle.getString("was_rem"));
						presetNPCs.remove(npcName);
					}
				}

				FixedSpawnsDisplay.updateSoftReference();
				PresetPathsDisplay.updateSoftReference();

				return true;
			}
		}
	}

	public static boolean addFixedNPCSpawn(Player p, String npcName, Location l, NPCHandler handle) {
		if (!FileLocator.doesNPCNameExist(npcName)) {
			if (p != null) {
				QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("no_npc_name"));
			}
			return false;
		} else {
			if (fixedSpawns.containsKey(npcName)) {
				if (p != null) {
					QuestX.logChat(p, LocaleBundle.getString("yes_fixed_spawn"));
				}
				return false;
			}
			SimpleNPC remove = handle.getSimpleNPCByName(npcName);
			if (remove != null) {
				remove.destroyNPCObject();
			}

			LoadNPCTemplate tmp = new LoadNPCTemplate(npcName);

			tmp.loadProperties();

			tmp.getLoadedNPCTemplate().registerSimpleNPCFixedSpawn(handle, l);

			loader.read();
			for (SyncWrapper wrap : loader.getReadableData()) {
				loader.add(wrap.getTag(), wrap.getObject());
			}
			loader.add(npcName, new SyncLocation(l));
			loader.write();
			loader.clearReadArray();
			loader.clearWriteArray();

			fixedSpawns.put(npcName, l);

			FixedSpawnsDisplay.updateSoftReference();

			if (p != null) {
				QuestX.logChat(p, LocaleBundle.getString("fixed_spawn_success") + npcName + "'.");
			}
			return true;
		}
	}

}
