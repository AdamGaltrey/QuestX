package com.adamki11s.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.bundle.LocaleBundle;
import com.adamki11s.npcs.NPCHandler;
import com.adamki11s.npcs.SimpleNPC;
import com.adamki11s.npcs.loading.FixedLoadingTable;
import com.adamki11s.pathing.Tile;
import com.adamki11s.pathing.preset.PresetPathCreation;
import com.adamki11s.questx.QuestX;

public class PlayerInteract implements Listener {

	private static HashMap<String, PresetPathCreation> creating = new HashMap<String, PresetPathCreation>();

	private static NPCHandler handle;

	public PlayerInteract(Plugin p, NPCHandler h) {
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
		handle = h;
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent evt) {
		final Player p = evt.getPlayer();	
		if (evt.getAction() == Action.RIGHT_CLICK_BLOCK && isCreatingPresetPath(p.getName()) && evt.getClickedBlock() != null) {
			creating.get(p.getName()).setLocation(p, evt.getClickedBlock().getLocation());
		}
	}

	public static boolean isCreatingPresetPath(String p) {
		return creating.containsKey(p);
	}

	public static void startCreatingPresetPath(Player p, String n) {
		if (isCreatingPresetPath(p.getName())) {
			QuestX.logChatError(p, ChatColor.RED + LocaleBundle.getString("already_creating_path"));
			return;
		} else {
			SimpleNPC npc = handle.getSimpleNPCByName(n);
			if (npc == null) {
				QuestX.logChatError(p, ChatColor.RED + LocaleBundle.getString("npc_not_loaded_or_exist"));
				return;
			} else {
				if (!npc.isSpawnFixed()) {
					QuestX.logChatError(p, ChatColor.RED + LocaleBundle.getString("no_fixed_spawn"));
					return;
				} else {
					if(FixedLoadingTable.presetNPCs.contains(n)){
						QuestX.logChatError(p, ChatColor.RED + LocaleBundle.getString("already_has_path"));
						return;
					}
					creating.put(p.getName(), new PresetPathCreation(npc.getFixedLocation(), n, p.getWorld().getName()));
					QuestX.logChat(p, ChatColor.GREEN + LocaleBundle.getString("set_points"));
				}
			}
		}
	}

	public static void cancelCreatingPath(Player p) {
		if (isCreatingPresetPath(p.getName())) {
			PresetPathCreation path = creating.get(p.getName());
			path.resetBlockStates(p);
			creating.remove(p.getName());
			QuestX.logChat(p, ChatColor.RED + LocaleBundle.getString("path_creation_cancel"));
		} else {
			QuestX.logChatError(p, ChatColor.RED + LocaleBundle.getString("not_creating_path"));
		}
	}

	public static void finaliseCreatingPath(Player p, NPCHandler handle) {
		if (isCreatingPresetPath(p.getName())) {
			
			PresetPathCreation path = creating.get(p.getName());
			
			path.resetBlockStates(p);

			path.createPath(p, handle);

			creating.remove(p.getName());
			QuestX.logChat(p, ChatColor.GREEN + LocaleBundle.getString("path_creation_complete"));
			
			SimpleNPC npc = handle.getSimpleNPCByName(path.getNPC());
			if(npc != null){
				npc.destroyNPCObject();
			}
			
			FixedLoadingTable.spawnFixedNPC(handle, path.getNPC());
		} else {
			QuestX.logChatError(p, ChatColor.RED + LocaleBundle.getString("not_creating_path"));
		}
	}

}
